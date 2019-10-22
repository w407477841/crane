package com.xywg.iot.modules.netty.handler;

import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.DateUtils;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.core.other.PushService;
import com.xywg.iot.modules.crane.factory.AlarmDTOFactory;
import com.xywg.iot.modules.crane.model.*;
import com.xywg.iot.modules.crane.service.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.xywg.iot.common.global.GlobalStaticConstant.DEVICE_STATE_ENABLED;


/**
 * @author hjy
 * @date 2018/12/27
 * 业务逻辑处理类
 */
@Component
public class BusinessProcessService {
    @Value("${xywg.heartbeat-time}")
    private String heartbeatTime;



    @Autowired
    private IProjectCraneService projectCraneService;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private IProjectCraneHeartbeatService projectCraneHeartbeatService;
    @Autowired
    private IProjectCraneOriginalDataService projectCraneOriginalDataService;
    @Autowired
    private IProjectCraneDetailService projectCraneDetailService;
    @Autowired
    private IProjectCraneAlarmService projectCraneAlarmService;
    @Autowired
    private IProjectInfoService  projectInfoService;
    @Autowired
    protected PushService pushService;
    @Autowired
    private HandleWarningMessage handleWarningMessage;


    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(BusinessProcessService.class);

    public void handleMessage(ChannelHandlerContext ctx, byte[] data) {
        //固定头部 从第0个字节开始占2字节
        String head = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 0, 2));
        //消息总长度
        Integer length = Integer.parseInt(DataUtil.byteToHex(data[2]), 16);
        //厂家编号
        String vendorNumber = DataUtil.byteToHex(data[3]);
        //协议版本
        String protocolVersion = DataUtil.byteToHex(data[4]);
        //消息类型
        String commandCode = DataUtil.byteToHex(data[5]);
        //设备编号 从第6个字节开始 一共4个字节
        String deviceCode = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 6, 4));
        // 校验码 倒数第五个字节为校验字节
        String checkCode = DataUtil.bytesToHexString(DataUtil.subByteArray(data, data.length - 5, data.length - 4));
        //报文结尾码 最后四个字节
        String endCode = DataUtil.bytesToHexString(DataUtil.subByteArray(data, data.length, data.length - 4));
        //真正的数据体(从第7个字节开始至倒数第6个结束,包含倒数第6个)
        String dataMessage = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 7, data.length - 5));
        DataDomain dataDomain = new DataDomain(head, length, vendorNumber, protocolVersion, commandCode, deviceCode, checkCode, endCode, dataMessage);

        //判断数据的有效性
        boolean bool = checkDataValidity(ctx, data, dataDomain);
        if (!bool) {
            ctx.close();
        }
        //开始真正业务处理
        handleServiceLogic(ctx, data, dataDomain);
    }

    /**
     * 处理业务逻辑
     */
    private void handleServiceLogic(ChannelHandlerContext ctx, byte[] data, DataDomain dataDomain) {
        String command = dataDomain.getCommandCode();
        //04 为心跳数据
        String commandHeartbeat = "04";
        if (!commandHeartbeat.equalsIgnoreCase(command)) {
            ProjectCraneOriginalData craneOriginalData = new ProjectCraneOriginalData();
            craneOriginalData.setDeviceNo(dataDomain.getDeviceCode());
            craneOriginalData.setDeviceTime(new Date());
            craneOriginalData.setOriginalData(DataUtil.bytesToHexString(data));
            //保存原始数据
            projectCraneOriginalDataService.insert(craneOriginalData);
        }

        switch (command) {
            //设备注册信息上传
            case ("00"):
                register(ctx, dataDomain);
                break;
            /*//后台注册信息下传
            case ("01"):
                break;*/
            //设备属性信息上传
            case ("02"):
                deviceBasicInfo(data, dataDomain);
                break;
            /*//后台应答信息下传
            case ("03"):

                break;*/
            //设备心跳包上传
            case ("04"):
                heartbeat(ctx, data, dataDomain);
                break;
           /* //后台应答信息下传
            case ("05"):

                break;*/
            //设备实时数据上传
            case ("06"):
                monitorData(ctx, data, dataDomain);
                break;
            //后台应答信息下传
           /* case ("07"):
                break;*/
            //设备工作循环数据上传
            case ("08"):
                circulation(data, dataDomain);
                break;
            //后台应答信息下传
            /*case ("09"):
                break;*/
            default:
                break;
        }

    }

    /**
     * 设备基础信息上报
     *
     * @param dataDomain
     */
    private void deviceBasicInfo(byte[] data, DataDomain dataDomain) {
        logger.info("Receive Device Basic Info: device--" + dataDomain.getDeviceCode() + ": " + DataUtil.bytesToHexString(data));
    }

    /**
     * 时时监控数据
     *
     * @param dataDomain
     */
    private void monitorData(ChannelHandlerContext ctx, byte[] data, DataDomain dataDomain) {
        String deviceTimeStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 10, 6));
        //8-12-28 16:00:00--->2008-12-28 16:00:00   18-12-28 16:00:00--->2018-12-28 16:00:00
        deviceTimeStr = deviceTimeStr.length() == 11 ? "200" + deviceTimeStr : "20" + deviceTimeStr;
        //采集时间
        Date deviceTime = DateUtils.parseDate(deviceTimeStr) == null ? new Date() : DateUtils.parseDate(deviceTimeStr);
        //高度
        String heightStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 16, 2));
        Double height = Integer.parseInt(heightStr, 16) / 1d;
        //幅度
        String rangeStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 18, 2));
        // BigDecimal range = new BigDecimal(Long.parseLong(rangeStr));
        Double range = Integer.parseInt(rangeStr, 16) / 1d;

        //回转角度
        String rotaryAngleStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 20, 2));
        //BigDecimal rotaryAngle = new BigDecimal(Long.parseLong(rotaryAngleStr));
        Double rotaryAngle = Integer.parseInt(rotaryAngleStr, 16) / 1d;

        //载重
        String weightStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 22, 2));
        //BigDecimal weight = new BigDecimal(Long.parseLong(weightStr));
        Double weight = Integer.parseInt(weightStr, 16) / 1d;

        //风速
        String windSpeedStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 27, 2));
        // BigDecimal windSpeed = new BigDecimal(Long.parseLong(windSpeedStr));
        Double windSpeed = Integer.parseInt(windSpeedStr, 16) / 1d;

        //倾斜
        String tiltAngleStr = DataUtil.bytesToHexString(DataUtil.subByteArray(data, 29, 2));
        //BigDecimal tiltAngle = new BigDecimal(Long.parseLong(tiltAngleStr));
        Double tiltAngle = Integer.parseInt(tiltAngleStr, 16) / 1d;

        //解析明细数据
        ProjectCraneDetail projectCraneDetail = new ProjectCraneDetail();
        projectCraneDetail.setCraneId(NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane().getId());
        projectCraneDetail.setCreateTime(new Date());
        projectCraneDetail.setDeviceNo(NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane().getDeviceNo());
        projectCraneDetail.setHeight(height);
        projectCraneDetail.setDeviceTime(deviceTime);
        projectCraneDetail.setRange(range);
        projectCraneDetail.setRotaryAngle(rotaryAngle);
        projectCraneDetail.setWeight(weight);
        projectCraneDetail.setWindSpeed(windSpeed);
        projectCraneDetail.setTiltAngle(tiltAngle);
        projectCraneDetail.setIsDel(0);
        projectCraneDetail.setCreateTime(new Date());
        //保存明细数据
        projectCraneDetailService.insert(projectCraneDetail);

        ProjectInfo projectInfo = projectInfoService.selectById(NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane().getProjectId());

        CurrentCraneData currentCraneData = CurrentCraneData.factory(projectCraneDetail, NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane());
        //将时时数据推送的web端
        //将时时数据推送到WebSocket
        commonMethod.push(redisUtil,
                NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane().getDeviceNo(),
                "crane",
                projectInfo.getUuid(),
                currentCraneData,
                projectInfo.getId().toString());

        //处理报警信息
        handleWarningMessage(ctx,data, projectCraneDetail);
    }

    /**
     * 处理报警信息
     *
     * @param data   原始字节数据
     * @param detail 明细
     */
    @SuppressWarnings("unchecked")
    private void handleWarningMessage(ChannelHandlerContext ctx,byte[] data, ProjectCraneDetail detail) {
        List<AlarmDTO> alarmInfo = new ArrayList<>();
        Map<String, Object> map = handleWarningMessage.getHandleWarningMessage(data, detail);
        List<ProjectCraneAlarm> toDbList=(List<ProjectCraneAlarm>)map.get("list");

        //移动端推送
        String warningMessage = (String) map.get("message");

        if(toDbList.size()>0){
            projectCraneAlarmService.insertBatch(toDbList);
        }
        ProjectInfo projectInfo = projectInfoService.selectById(NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane().getProjectId());

        for(ProjectCraneAlarm  alarm:toDbList){
            alarmInfo.add(AlarmDTOFactory.factory(alarm, projectInfo, "塔吊设备"));
            //推送到移动端
            pushService.pushMob("注意", AlarmDTOFactory.factoryMessage(alarm, projectInfo, "塔吊设备"), projectInfo.getUuid());
        }
        //开始推送报警信息(分别推送到移动端 和 web 端)
        ResultDTO redisData = ResultDTO.factory(alarmInfo, 21);
        //放入缓存 报警信息推送到web端
        commonMethod.push(redisUtil, detail.getDeviceNo(), "crane_alarm", projectInfo.getUuid(), redisData, "" + projectInfo.getId());
    }


    /**
     * 工作循环
     *
     * @param dataDomain
     */
    private void circulation(byte[] data, DataDomain dataDomain) {
        logger.info("Receive Device circulation Info: device--" + dataDomain.getDeviceCode() + ": " + DataUtil.bytesToHexString(data));
    }


    /**
     * 心跳处理
     */
    private void heartbeat(ChannelHandlerContext ctx, byte[] data, DataDomain dataDomain) {
//        logger.info("Receive Heartbeat Message: device--" + dataDomain.getDeviceCode() + ": " + DataUtil.bytesToHexString(data));
        ProjectCrane projectCrane = NettyChannelManage.getDeviceInfo(ctx.channel()).getProjectCrane();

        ProjectCraneHeartbeat heartbeat = new ProjectCraneHeartbeat();
        heartbeat.setCraneId(projectCrane.getId());
        heartbeat.setCreateTime(new Date());
        heartbeat.setDeviceNo(projectCrane.getDeviceNo());
        if (!redisUtil.exists("device_platform::head:qzjht:" + projectCrane.getDeviceNo())) {
            // 不存在 说明是开机
            // 上一条是关机
            projectCraneHeartbeatService.doOpenBusiness(heartbeat);
        } else {
            projectCraneHeartbeatService.updateEndTime(heartbeat);

        }
        redisUtil.setSec("device_platform:head:qzjht:" + projectCrane.getDeviceNo(), 1, 46L);
    }


    /**
     * 注册登录  能执行到此方法,说一定存在该设备且已经启用了
     *
     * @param ctx
     * @param dataDomain
     */
    private void register(ChannelHandlerContext ctx, DataDomain dataDomain) {
        //获取当前系统时间 (2018-12-28 10:11:00 ---> 181228101100)
        String dateString = DateUtils.formatDate(new Date(), "yyMMddHHmmss");
        //间隔时间
        String heartbeat = Integer.toHexString(Integer.parseInt(heartbeatTime));
        //设置回复的消息
        dataDomain.setDataMessage(dateString + heartbeat);
        //设置回复的命令码
        dataDomain.setCommandCode("01");
        //登录回复
        commonMethod.sendMessage(ctx, dataDomain, 7);

        DeviceHold deviceHold = NettyChannelManage.getDeviceInfo(ctx.channel());
        ProjectCrane projectCrane = deviceHold.getProjectCrane();
        //修改设备状态为上线
        projectCraneService.updateById(getProjectCrane(projectCrane.getId(), GlobalStaticConstant.DEVICE_STATE_ON_LINE));
    }


    /**
     * 校验数据有效性
     */
    private boolean checkDataValidity(ChannelHandlerContext ctx, byte[] data, DataDomain dataDomain) {
        String hexString = DataUtil.bytesToHexString(data);
        //校验报文头
        if (!GlobalStaticConstant.PROTOCOL_FIXED_HEAD.equalsIgnoreCase(dataDomain.getHead())) {
            logger.info("Illegal Data(Head String Error):<<" + hexString + ">>");
            return false;
        }
        //校验校验码
        if (!equalsIgnoreCaseCheckCode(dataDomain.getCheckCode(), data)) {
            logger.info("Illegal Data(Check Code Error):<<" + hexString + ">>");
            return false;
        }
        //校验设备是否存在
        ProjectCrane projectCrane = projectCraneService.selectOne(dataDomain.getDeviceCode());
        if (projectCrane == null) {
            logger.info("Illegal Data(Device Unreal):<<" + hexString + ">>");
            return false;
        }
        //校验设备是否启用
        if (DEVICE_STATE_ENABLED != projectCrane.getStatus()) {
            logger.info("Illegal Data(Device Not Enabled):<<" + hexString + ">>");
            return false;
        }

        projectCrane.setIsOnline(GlobalStaticConstant.DEVICE_STATE_ON_LINE);
        NettyChannelManage.saveChannel(ctx.channel(), projectCrane);
        return true;
    }


    /**
     * 比较校验码
     *
     * @param originalCheckCode 收到的数据原始校验码
     * @param data              原始字节
     * @return
     */
    private boolean equalsIgnoreCaseCheckCode(String originalCheckCode, byte[] data) {
        String checkCodes = getCheckCode(DataUtil.subByteArray(data, 0, data.length - 5));
        //计算得到的校验位  累加和的低字节
        String checkCode = checkCodes.substring(checkCodes.length() - 2, checkCodes.length());
        if (!originalCheckCode.equalsIgnoreCase(checkCode)) {
            return false;
        }
        return true;
    }

    /**
     * 字节累加和
     *
     * @param b
     * @return
     */
    private static String getCheckCode(byte[] b) {
        short s = 0;
        // 累加求和
        for (byte aB : b) {
            s += aB;
        }
        return DataUtil.byteToHex((byte) s);
    }


    private ProjectCrane getProjectCrane(Integer id, Integer isOnline) {
        ProjectCrane toDbCrane = new ProjectCrane();
        toDbCrane.setId(id);
        toDbCrane.setIsOnline(isOnline);
        toDbCrane.setModifyTime(new Date());
        toDbCrane.setModifyUser(-1);
        return toDbCrane;
    }

    /**
     * 设备下线
     */
    public void deviceDisconnect(ChannelHandlerContext ctx) {
        DeviceHold deviceHold = NettyChannelManage.getDeviceInfo(ctx.channel());
        //修改设备状态为离线
        projectCraneService.updateById(getProjectCrane(deviceHold.getProjectCrane().getId(), GlobalStaticConstant.DEVICE_STATE_OFF_LINE));
        //移除连接
        NettyChannelManage.removeChannel(ctx.channel());
    }


    /**
     * 将报警id转换为报警信息
     *
     * @return str
     *//*
    private String alarmCodeToMessage(Integer alarmCode) {
        String message;
        switch (alarmCode) {
            case 1:
                message = "起重量报警";
                break;
            case 2:
                message = "起重量预警";
                break;
            case 3:
                message = "幅度向内报警";
                break;
            case 4:
                message = "幅度向内预警";
                break;
            case 5:
                message = "幅度向外报警";
                break;
            case 6:
                message = "幅度向外预警";
                break;
            case 7:
                message = "高度向上报警";
                break;
            case 8:
                message = "高度向上预警";
                break;
            case 9:
                message = "力矩报警";
                break;
            case 10:
                message = "力矩预警";
                break;
            case 11:
                message = "单机防碰撞报警";
                break;
            case 12:
                message = "单机防碰撞预警";
                break;
            case 13:
                message = "多机防碰撞报警";
                break;
            case 14:
                message = "多机防碰撞预警";
                break;
            case 15:
                message = "风速报警";
                break;
            case 16:
                message = "倾角报警";
                break;
            default:
                message = "告警信息类型未定义";
                break;
        }
        return message;
    }*/
}
