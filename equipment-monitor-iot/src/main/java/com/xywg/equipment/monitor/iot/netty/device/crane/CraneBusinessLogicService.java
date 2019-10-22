package com.xywg.equipment.monitor.iot.netty.device.crane;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.DateUtils;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneCalibrationLogMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneDetailMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneSingleCollisionAvoidanceSetMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dto.CurrentCraneData;
import com.xywg.equipment.monitor.iot.modular.crane.model.*;
import com.xywg.equipment.monitor.iot.modular.crane.service.*;
import com.xywg.equipment.monitor.iot.modular.romote.handle.RemoteSetupService;
import com.xywg.equipment.monitor.iot.netty.aop.ZbusProducerHolder;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.NettyChannelManage;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.xywg.equipment.global.GlobalStaticConstant.DATA_PACKAGE_RESPONSE;
import static com.xywg.equipment.global.GlobalStaticConstant.DEVICE_STATE_OFF_LINE;
import static com.xywg.equipment.global.GlobalStaticConstant.DEVICE_TYPE_FLAG_CRANE;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.decode;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.stringToHexString;

/**
 * @author hjy
 * @date 2018/9/20
 * 环境监控 业务逻辑处理服务类(塔吊)
 */
@Component
@Slf4j
public class CraneBusinessLogicService {
    @Autowired
    private IProjectCraneService projectCraneService;
    @Autowired
    private IProjectCraneHeartbeatService projectCraneHeartbeatService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    protected XywgProerties xywgProerties;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ProjectCraneDetailMapper projectCraneDetailMapper;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    protected PushService pushService;
    @Autowired
    private RemoteSetupService remoteSetup;
    @Autowired
    private ProjectCraneCalibrationLogMapper projectCraneCalibrationLogMapper;
    @Autowired
    private ProjectCraneSingleCollisionAvoidanceSetMapper craneSingleAvoidanceSetMapper;
    @Autowired
    private IProjectCraneAlarmService projectCraneAlarmService;
    @Autowired
    private IProjectCraneMutiCollisionAvoidanceSetService mutiCollisionAvoidanceSetService;
    @Autowired
    private IProjectCraneCyclicWorkDurationService workDurationService;
    @Autowired
    private IProjectCraneDetailService projectCraneDetailService;
    @Autowired
    private ZbusProducerHolder zbusProducerHolder;
    /**
     * 登录 只包含(sn,firmware)
     *
     * @param monitorPojo 数据
     */
    public void login(Channel channel, CompleteDataPojo monitorPojo) {
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01"));
        NettyChannelManage.CHANNEL_MAP.put(sn, channel);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        DeviceConnectInfo deviceConnectInfo = new DeviceConnectInfo(uuid, sn);
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceConnectInfo);

        //设备版本号(固件版本号)
        String deviceVersion = commonMethod.getDeviceVersion(monitorPojo.getDataMap().get("02"));
        ProjectCrane projectCrane = new ProjectCrane();
        projectCrane.setDeviceNo(sn);
        projectCrane.setFirmwareVersion(deviceVersion);
        projectCrane.setModifyTime(new Date());
        Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no", sn);
        wrapper.eq("is_del", 0);
        projectCraneService.update(projectCrane, wrapper);
        projectCrane = projectCraneService.selectOne(sn);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 16进制字符串
        String id;
        if (projectCrane.getProjectId() < 1000) {
            id = String.format("%04d", projectCrane.getProjectId());
        } else {
            id = String.valueOf(projectCrane.getProjectId());
        }
        // id
        map.put("02", stringToHexString(id));
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);


    }

    /**
     * 心跳
     *
     * @param monitorPojo 数据
     */
    public void heartbeat(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //序列号
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        ProjectCraneHeartbeat heartbeat = new ProjectCraneHeartbeat();
        ProjectCrane crane = projectCraneService.selectOne(sn);

        heartbeat.setCraneId(crane.getId());
        heartbeat.setCreateTime(new Date());
        heartbeat.setDeviceNo(crane.getDeviceNo());
        if (!redisUtil.exists(xywgProerties.getRedisHead() + ":head:qzjht:" + sn)) {
            // 不存在 说明是开机
            // 上一条是关机
            projectCraneHeartbeatService.doOpenBusiness(heartbeat);
        } else {
            projectCraneHeartbeatService.updateEndTime(heartbeat);
        }
        redisUtil.setSec(xywgProerties.getRedisHead() + ":head:qzjht:" + sn, 1, 300L);
    }

    /**
     * 同步时间
     *
     * @param monitorPojo 数据
     */
    public void synchronizationTime(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //序列号 16进制的:
        String sn = monitorPojo.getDataMap().get("01");
        commonMethod.synchronizationTime(channel, sn, monitorPojo.getCommand(), monitorPojo.getVersion());
    }

    /**
     * 传输升级数据
     *
     * @param monitorPojo 数据
     */
    void upgradeTransferData(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        commonMethod.upgradeTransfer(channel, monitorPojo);
    }

    /**
     * 升级文件发送完毕回复
     *
     * @param monitorPojo 数据
     */
//    private void upgradeFileSentOver(CompleteDataPojo monitorPojo) {
//        //暂时不做处理
//
//
//    }

    /**
     * 上传设备异常情况数据
     *
     * @param monitorPojo 数据
     */
    public void exceptionalLog(Channel ctx, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        String exceptionMessageType = monitorPojo.getDataMap().get("02").replaceAll(" ", "");

        remoteSetup.deviceCraneExceptionalLog(sn, exceptionMessageType, DEVICE_TYPE_FLAG_CRANE);
    }

    /**
     * 远程重启
     *
     * @param monitorPojo
     */
    public void remoteReboot(CompleteDataPojo monitorPojo) {
        //暂时不做回复处理,只做命令下发
    }


    /**
     * 上传监控数据
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorData(Channel channel, CompleteDataPojo monitorPojo) {
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 监控数据", sn);
        // 查询设备阈值
        ProjectCrane crane = projectCraneService.selectOne(sn);
        log.info("塔吊--设备{}回复", sn);
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal dgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal height=craneHeight.subtract(dgHeight).setScale(2,RoundingMode.HALF_UP);
        System.out.println(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16));
        ProjectCraneDetail projectCraneDetail = new ProjectCraneDetail(
                null,
                sn,
                null,
                DateUtils.parseDate(monitorPojo.getDataMap().get("0D")),
                null,
                Long.parseLong(monitorPojo.getDataMap().get("05"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("0A"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("06"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("0B"), 16) / 100d,
                //height / 1d,
                Double.parseDouble(height.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("03"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"), 16) / 1d,
                Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"), 16) / 10d,
                0,
                DateUtils.parseDate(monitorPojo.getDataMap().get("0E")),
                Double.parseDouble(craneHeight.toString()),
                Double.parseDouble(dgHeight.toString())
                //Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16) / 10d
        );
        ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
        //数据明细表
        projectCraneDetailMapper.createDetail(projectCraneDetail, BaseFactory.getTableName(ProjectCraneDetail.class));
        CurrentCraneData currentCraneData = CurrentCraneData.factory(projectCraneDetail, crane);
        //检查是否是报警信息  0A表示报警id
        projectCraneDetail.setAlarmInfo(monitorPojo.getDataMap().get("0C"));
        String alarmData = projectCraneDetail.getAlarmInfo();

        //转发
        //判断是否需要转发
        if(redisUtil.exists(xywgProerties.getRedisTdDispatchPrefix()+ sn))
        {
            try {
                zbusProducerHolder.sendDispatchMessage(projectCraneDetail.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //00000000 表示正常数据
        String alarmTypeNormal = "00000000";
        if (StringUtils.isBlank(alarmData) || alarmTypeNormal.equals(alarmData)) {
            log.info("塔吊--设备{}正常数据", sn);
            return;
        }

        try {
            //将实时数据推送到WebSocket
            commonMethod.push(redisUtil, sn, "crane", projectInfo.getUuid(), currentCraneData, "" + crane.getProjectId());
            //sendAlarmData(projectCraneDetail, alarmData, projectInfo);
            int alarmStatus = sendAlarmData(projectCraneDetail, alarmData, projectInfo);
            log.info("塔吊--设备{}发送报警数据", sn);
            ProjectCrane updateCraneStatus = new ProjectCrane();
            updateCraneStatus.setAlarmStatus(alarmStatus);
            updateCraneStatus.setId(crane.getId());
            log.info("塔吊--设备{}修改设备报警状态", sn);
            projectCraneService.updateById(updateCraneStatus);
            projectCraneService.removeCache(sn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 上传离线监控数据
     *  1.新增监控数据
     *  2.新增报警数据
     *  3.修改设备报警状态
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadOfflineMonitorData(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 离线监控数据",sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal dgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal height=craneHeight.subtract(dgHeight).setScale(2,RoundingMode.HALF_UP);
        System.out.println(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16));
        ProjectCraneDetail projectCraneDetail = new ProjectCraneDetail(
                null,
                sn,
                null,
                DateUtils.parseDate(monitorPojo.getDataMap().get("0D")),
                null,
                Long.parseLong(monitorPojo.getDataMap().get("05"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("0A"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("06"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("0B"), 16) / 100d,
                //height / 1d,
                Double.parseDouble(height.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("03"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"), 16) / 1d,
                Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"), 16) / 10d,
                0,
                DateUtils.parseDate(monitorPojo.getDataMap().get("0E")),
                Double.parseDouble(craneHeight.toString()),
                Double.parseDouble(dgHeight.toString())
                //Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16) / 10d
        );ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
        //数据明细表
        projectCraneDetailService.insert(projectCraneDetail);
        CurrentCraneData currentCraneData = CurrentCraneData.factory(projectCraneDetail, crane);
        //检查是否是报警信息  0A表示报警id
        String alarmData = monitorPojo.getDataMap().get("0C");

        //将时时数据推送到WebSocket
        commonMethod.push(redisUtil, sn, "crane", projectInfo.getUuid(), currentCraneData, "" + crane.getProjectId());
        //00000000 表示正常数据
        String alarmTypeNormal = "00000000";
        if (StringUtils.isBlank(alarmData) || alarmTypeNormal.equals(alarmData)) {
            return;
        }

        try {
            sendOfflineAlarmData(projectCraneDetail, alarmData, projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("塔吊--设备{}执行完毕 离线监控数据",sn);
    }

    /**
     * 塔机基本信息设置上传服务器
     *
     * @param channel     通道
     * @param monitorPojo 数据
     */
    public void basicInformation(Channel channel, CompleteDataPojo monitorPojo) {
        String sn = decode(monitorPojo.getDataMap().get("01"));
        commonMethod.resMessageJoint(channel, sn, null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        ProjectCrane projectCrane = new ProjectCrane();
        //设备编号
        projectCrane.setDeviceNo(sn);
        //塔基编号
        String craneCode = monitorPojo.getDataMap().get("02");
        projectCrane.setCraneNo(Integer.parseInt(craneCode, 16) + "");
        //倍率
        String multiple = monitorPojo.getDataMap().get("03");
        projectCrane.setMultiple(Integer.parseInt(multiple, 16));
        //最大起重量
        projectCrane.setMaxWeight(new BigDecimal(Long.parseLong(monitorPojo.getDataMap().get("04"), 16) / 1000d));
        //额定力矩
        projectCrane.setFixMoment(new BigDecimal(Long.parseLong(monitorPojo.getDataMap().get("05"), 16)));
        //最大塔高
        //projectCrane.setStandardHeight(new BigDecimal(Long.parseLong(monitorPojo.getDataMap().get("06"), 16)));
        //最大幅度
        projectCrane.setMaxRange(new BigDecimal(Long.parseLong(monitorPojo.getDataMap().get("06"), 16)));
        //最大倾角
        projectCrane.setTiltAngle(new BigDecimal(Long.parseLong(monitorPojo.getDataMap().get("07"), 16) / 1000d));
        Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no", projectCrane.getDeviceNo());
        wrapper.eq("is_del", 0);
        projectCraneService.update(projectCrane, wrapper);
        // 清下缓存
        projectCraneService.removeCache(projectCrane.getDeviceNo());
        log.info("塔吊--设备{} 执行完毕 基本信息设置", sn);

    }

    /**
     * 塔机幅度校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel     通道
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void amplitudeCorrect(Channel channel, CompleteDataPojo monitorPojo, Integer projectId) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 幅度校准信息设置", deviceNo);
        //功能码
        String command = monitorPojo.getCommand();
        //幅度斜率，单位0.1     02
        Double amplitudeSlope = Integer.parseInt(monitorPojo.getDataMap().get("02"), 16) / 10d;
        //幅度偏移, 单位 0.1     03
        Double amplitudeDeviation = Long.parseLong(monitorPojo.getDataMap().get("03"), 16) / 10d;
        //幅度量程上限，单位0.1m    04
        Double amplitudeRangeMax = Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d;
        //幅度量程下限，单位0.1m    05
        Double amplitudeRangeMin = Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 10d;
        //幅度信号上限             06
        Long amplitudeSignalMax = Long.parseLong(monitorPojo.getDataMap().get("06"), 16);
        //幅度信号下限              07
        Long amplitudeSignalMin = Long.parseLong(monitorPojo.getDataMap().get("07"), 16);
        //幅度校准设置状态,        08      0 未设置，1 已设置
        int amplitudeCalibrationStatus = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("幅度斜率:").append(amplitudeSlope).append(",");
        sBuilder.append("幅度偏移:").append(amplitudeDeviation).append(",");
        sBuilder.append("幅度量程上限:").append(amplitudeRangeMax).append(",");
        sBuilder.append("幅度量程下限:").append(amplitudeRangeMin).append(",");
        sBuilder.append("幅度信号上限:").append(amplitudeSignalMax).append(",");
        sBuilder.append("幅度信号下限:").append(amplitudeSignalMin).append(",");
        sBuilder.append("幅度校准设置状态:").append(amplitudeCalibrationStatus == 0 ? "未设置" : "已设置").append(",");

        ProjectCraneCalibrationLog projectCraneCalibrationLog = new ProjectCraneCalibrationLog(
                deviceNo, projectId, command, sBuilder.toString(), 0, new Date()
        );
        projectCraneCalibrationLogMapper.insert(projectCraneCalibrationLog);
        log.info("塔吊--设备{} 执行完毕 幅度校准信息设置", deviceNo);
    }

    /**
     * 塔机高度校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel 通道
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void heightCorrect(Channel channel, CompleteDataPojo monitorPojo, Integer projectId) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 高度校准信息设置", deviceNo);
        //功能码
        String command = monitorPojo.getCommand();
        //高度斜率，单位0.1     02
        Double highSlope = Integer.parseInt(monitorPojo.getDataMap().get("02"), 16) / 10d;
        //高度偏移，单位0.1     03
        Double heightOffset = Long.parseLong(monitorPojo.getDataMap().get("03"), 16) / 10d;
        //高度量程上限，单位0.1     04
        Double heightRangeMax = Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d;
        //高度量程下限，单位0.1     05
        Double heightRangeMin = Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 10d;
        //高度信号上限             06
        Long heightSignalMax = Long.parseLong(monitorPojo.getDataMap().get("06"), 16);
        //高度信号下限              07
        Long heightSignalMin = Long.parseLong(monitorPojo.getDataMap().get("07"), 16);
        //高度校准设置状态,        08      0 未设置，1 已设置
        Integer heightCalibrationStatus = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("高度斜率:").append(highSlope).append(",");
        sBuilder.append("高度偏移:").append(heightOffset).append(",");
        sBuilder.append("高度量程上限:").append(heightRangeMax).append(",");
        sBuilder.append("高度量程下限:").append(heightRangeMin).append(",");
        sBuilder.append("高度信号上限:").append(heightSignalMax).append(",");
        sBuilder.append("高度信号下限:").append(heightSignalMin).append(",");
        sBuilder.append("高度校准设置状态:").append(heightCalibrationStatus == 0 ? "未设置" : "已设置").append(",");

        ProjectCraneCalibrationLog projectCraneCalibrationLog = new ProjectCraneCalibrationLog(
                deviceNo, projectId, command, sBuilder.toString(), 0, new Date()
        );
        projectCraneCalibrationLogMapper.insert(projectCraneCalibrationLog);
        log.info("塔吊--设备{} 执行完毕 高度校准信息设置", deviceNo);
    }

    /**
     * 塔机角度校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void angleCorrect(Channel channel, CompleteDataPojo monitorPojo, Integer projectId) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 角度校准信息设置", deviceNo);
        //功能码
        String command = monitorPojo.getCommand();
        //角度斜率，单位0.1     02
        Double angleSlope = Integer.parseInt(monitorPojo.getDataMap().get("02"), 16) / 10d;
        //角度偏移，单位0.1     03
        Double angleOffset = Long.parseLong(monitorPojo.getDataMap().get("03"), 16) / 10d;
        //角度量程上限，单位0.1     04
        Double angleRangeMax = Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d;
        //角度量程下限，单位0.1     05
        Double angleRangeMin = Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 10d;
        //角度信号上限             06
        Long angleSignalMax = Long.parseLong(monitorPojo.getDataMap().get("06"), 16);
        //角度信号下限              07
        Long angleSignalMin = Long.parseLong(monitorPojo.getDataMap().get("07"), 16);
        //角度校准设置状态,        08      0 未设置，1 已设置
        Integer angleCalibrationStatus = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("角度斜率:").append(angleSlope).append(",");
        sBuilder.append("角度偏移:").append(angleOffset).append(",");
        sBuilder.append("角度量程上限:").append(angleRangeMax).append(",");
        sBuilder.append("角度量程下限:").append(angleRangeMin).append(",");
        sBuilder.append("角度信号上限:").append(angleSignalMax).append(",");
        sBuilder.append("角度信号下限:").append(angleSignalMin).append(",");
        sBuilder.append("角度校准设置状态:").append(angleCalibrationStatus == 0 ? "未设置" : "已设置").append(",");

        ProjectCraneCalibrationLog projectCraneCalibrationLog = new ProjectCraneCalibrationLog(
                deviceNo, projectId, command, sBuilder.toString(), 0, new Date()
        );
        projectCraneCalibrationLogMapper.insert(projectCraneCalibrationLog);
        log.info("塔吊--设备{} 执行完毕 角度校准信息设置", deviceNo);

    }

    /**
     * 塔机起重量校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * <p>
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void elevatingCapacityCorrect(Channel channel, CompleteDataPojo monitorPojo, Integer projectId) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 起重量校准信息设置", deviceNo);
        //功能码
        String command = monitorPojo.getCommand();
        //重量斜率，单位0.1     02
        Double elevatingCapacitySlope = Integer.parseInt(monitorPojo.getDataMap().get("02"), 16) / 10d;
        //重量偏移，单位0.1     03
        Double elevatingCapacityOffset = Long.parseLong(monitorPojo.getDataMap().get("03"), 16) / 10d;
        //重量量程上限，单位kg     04
        Integer elevatingCapacityRangeMax = Integer.parseInt(monitorPojo.getDataMap().get("04"), 16);
        //重量量程下限，单位kg     05
        Integer elevatingCapacityRangeMin = Integer.parseInt(monitorPojo.getDataMap().get("05"), 16);
        //重量信号上限             06
        Long elevatingCapacitySignalMax = Long.parseLong(monitorPojo.getDataMap().get("06"), 16);
        //重量信号下限              07
        Long elevatingCapacitySignalMin = Long.parseLong(monitorPojo.getDataMap().get("07"), 16);
        //重量校准设置状态,        08      0 未设置，1 已设置
        int elevatingCapacityCalibrationStatus = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("重量斜率:").append(elevatingCapacitySlope).append(",");
        sBuilder.append("重量偏移:").append(elevatingCapacityOffset).append(",");
        sBuilder.append("重量量程上限:").append(elevatingCapacityRangeMax).append(",");
        sBuilder.append("重量量程下限:").append(elevatingCapacityRangeMin).append(",");
        sBuilder.append("重量信号上限:").append(elevatingCapacitySignalMax).append(",");
        sBuilder.append("重量信号下限:").append(elevatingCapacitySignalMin).append(",");
        sBuilder.append("重量校准设置状态:").append(elevatingCapacityCalibrationStatus == 0 ? "未设置" : "已设置").append(",");

        ProjectCraneCalibrationLog projectCraneCalibrationLog = new ProjectCraneCalibrationLog(
                deviceNo, projectId, command, sBuilder.toString(), 0, new Date()
        );
        projectCraneCalibrationLogMapper.insert(projectCraneCalibrationLog);
        log.info("塔吊--设备{} 执行完毕 起重量校准信息设置", deviceNo);
    }

    /**
     * 单机防碰撞区域设置上传服务器
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void antiCollisionCorrect(Channel channel, CompleteDataPojo monitorPojo, Integer projectId) {
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
        commonMethod.resMessageJoint(channel, deviceNo, null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        log.info("塔吊--设备{} 执行开始 单机防碰撞区域设置", deviceNo);
        //起始角度，单位0.1     02
        Double startAngle = Integer.parseInt(monitorPojo.getDataMap().get("02"), 16) / 10d;
        //终止角度，单位0.1     03
        Double endAngle = Long.parseLong(monitorPojo.getDataMap().get("03"), 16) / 10d;
        //起始高度，单位0.1m    04
        Double startHeight = Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d;
        //终止高度，单位0.1m    05
        Double endHeight = Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 10d;
        //起始幅度，单位0.1m           06
        Double startRange = Long.parseLong(monitorPojo.getDataMap().get("06"), 16) / 10d;
        //终止幅度，单位0.1m            07
        Double endRange = Long.parseLong(monitorPojo.getDataMap().get("07"), 16) / 10d;
        //设置状态,  0 未设置，1 已设置   08
        Integer status = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);
        //区域编号   09
        Integer pageRange = Integer.parseInt(monitorPojo.getDataMap().get("09"), 16);

        ProjectCraneSingleCollisionAvoidanceSet singleCollisionAvoidanceSet = new ProjectCraneSingleCollisionAvoidanceSet(
                deviceNo, projectId,
                new BigDecimal(startAngle),
                new BigDecimal(endAngle),
                new BigDecimal(startHeight),
                new BigDecimal(endHeight),
                new BigDecimal(startRange),
                new BigDecimal(endRange),
                status, pageRange, 0, new Date()
        );
        craneSingleAvoidanceSetMapper.insert(singleCollisionAvoidanceSet);
        log.info("塔吊--设备{} 执行完毕 单机防碰撞区域设置", deviceNo);
    }

    /**
     * 多机防碰撞
     *
     * @param channel     通道
     * @param monitorPojo 信息
     */

    void mutiCollisionCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 多机防碰撞设置", sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        ProjectCraneMutiCollisionAvoidanceSet craneMutiCollisionAvoidanceSet = new ProjectCraneMutiCollisionAvoidanceSet(
                sn,
                crane.getProjectId(),
                monitorPojo.getDataMap().get("02"),
                monitorPojo.getDataMap().get("03"),
                Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("06"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0A"), 16),
                0,
                new Date()

        );
        mutiCollisionAvoidanceSetService.insert(craneMutiCollisionAvoidanceSet);
        log.info("塔吊--设备{} 执行完毕 多机防碰撞设置", sn);
    }

    /**
     * 处理报警数据
     *
     * @param projectCraneDetail 解析后的明细数据
     * @param alarmData          报警数据中的报警信息代码
     */
    private int sendAlarmData(ProjectCraneDetail projectCraneDetail, String alarmData, ProjectInfo projectInfo) {
        String sn = projectCraneDetail.getDeviceNo();
        log.info("塔吊--设备{}处理报警数据开始", sn);
        // 认为只有一个程序，否则可能导致
        List<AlarmDTO> alarmInfo = new ArrayList<>();
        //32 表示位数    FFFFFFFF ---> ‭11111111111111111111111111111111‬
        //进来了说明至少是预警
        int status = GlobalStaticConstant.DEVICE_STATE_YUJING;
        // 实时更新统计缓存
        String cyclicWorkKey = GlobalStaticConstant.getStatisticsKey(xywgProerties.getRedisHead(), sn);
        ProjectCraneStatisticsDaily statisticsDaily;
        if (!redisUtil.exists(cyclicWorkKey)) {
            ProjectCrane crane = projectCraneService.selectOne(sn);
            statisticsDaily = new ProjectCraneStatisticsDaily(projectInfo.getId(), projectInfo.getName(), projectInfo.getBuilder(), crane.getOwner(), crane.getIdentifier(), crane.getCraneNo(), sn);
        } else {
            statisticsDaily = JSONUtil.toBean((String) redisUtil.get(cyclicWorkKey), ProjectCraneStatisticsDaily.class);
        }
        log.info("塔吊--设备{}获取每日统计缓存", sn);
        int length = 16;
        for (int i = 0; i < length; i++) {
            String key = GlobalStaticConstant.getAlarmSatusKey(xywgProerties.getRedisHead(), sn, i);
            String lastAlarmKey = GlobalStaticConstant.getLastAlarmKey(xywgProerties.getRedisHead(), sn, i);
            //如果是1表示警告,0表示不是警告
            // 1-》0 则添加一个结束时间
            // 0-》1 则添加一条报警数据
            if (!redisUtil.exists(key)) {
                redisUtil.set(key, "0");
            }
            if (((Long.parseLong(alarmData, 16)) & (0x00000001 << i)) >> i == 1) {
                log.info("塔吊--设备{} 进入报警代码块", sn);
                if (null!=redisUtil.get(key)&&"1".equals(redisUtil.get(key))&&redisUtil.get(lastAlarmKey)!=null&&!(redisUtil.get(lastAlarmKey)).equals("")) {
                    System.out.println(redisUtil.get(lastAlarmKey));
                    //已经报过警了
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    //更新缓存
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));
                    log.info("塔吊--设备{} 重复报警，修改报警结束时间", sn);
                } else {
                    //添加一条报警数据
                    // 设置当前类型的报警状态为1
                    log.info("塔吊--设备{} 新增报警", sn);
                    redisUtil.set(key, "1");
                    int alarmId = i + 1;
                    status = deviceStatus(alarmId, status);
                    // 缓存
                    statisticsDaily = ProjectCraneStatisticsDaily.addAlarm(alarmId, statisticsDaily);
                    log.info("塔吊--设备{} 统计值+1", sn);
                    ProjectCraneAlarm alarm = new ProjectCraneAlarm();
                    String warningMessage = alarmCodeToMessage(alarmId, projectCraneDetail, alarm);
                    alarm.setCraneId(projectCraneDetail.getCraneId());
                    alarm.setDetailId(projectCraneDetail.getId());
                    alarm.setAlarmId(alarmId);
                    alarm.setAlarmInfo(warningMessage);
                    alarm.setDeviceNo(projectCraneDetail.getDeviceNo());
                    alarm.setIsDel(0);
                    alarm.setValue(alarm.getValue());
                    alarm.setIsHandle(0);
                    alarm.setDeviceTime(projectCraneDetail.getDeviceTime());
                    alarm.setCreateTime(projectCraneDetail.getDeviceTime());
                    alarm.setAlarmTime(projectCraneDetail.getDeviceTime());
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    //添加报警表
                    projectCraneAlarmService.insert(alarm);
                    Wrapper<ProjectCraneAlarm> wrapper = new EntityWrapper<>();
                    wrapper.eq("create_time", projectCraneDetail.getCreateTime());
                    wrapper.eq("crane_id", projectCraneDetail.getCraneId());
                    wrapper.eq("alarm_id", alarmId);
                    alarm = projectCraneAlarmService.selectOne(wrapper);
                    //把最后一条保存起来
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));
                    alarmInfo.add(AlarmDTOFactory.factory(alarm, projectInfo, "塔吊设备"));
                    //推送到移动端
                    pushService.pushMob("注意", AlarmDTOFactory.factoryMessage(alarm, projectInfo, "塔吊设备"), projectInfo.getUuid());
                }

            } else {
                log.info("塔吊--设备{} 进入正常代码块", sn);
                if ("0".equals(redisUtil.get(key))) {
                    //不用操作
                    //已经报过警了
                    log.info("塔吊--设备{} 未报警", sn);
                } else {
                    //报警停止了
                    redisUtil.set(key, "0");
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    Wrapper<ProjectCraneAlarm> wrapper = new EntityWrapper<>();
                    wrapper.eq("create_time", alarm.getCreateTime());
                    wrapper.eq("id", alarm.getId());
                    projectCraneAlarmService.update(alarm, wrapper);
                    redisUtil.remove(lastAlarmKey);
                    log.info("塔吊--设备{} 报警停止", sn);
                }
            }


        }
        // 更新缓存信息
        redisUtil.set(cyclicWorkKey, JSONUtil.toJsonStr(statisticsDaily));

        if (alarmInfo.size() > 0) {
            ResultDTO redisData = ResultDTO.factory(alarmInfo, 21);
            //放入缓存 报警信息推送到web端

            commonMethod.push(redisUtil, projectCraneDetail.getDeviceNo(), "crane_alarm", projectInfo.getUuid(), redisData, "" + projectInfo.getId());
            log.info("塔吊--设备{}处理报警数据结束", sn);
            return status;
        } else {
            log.info("塔吊--设备{}处理报警数据结束", sn);
            return 0;
        }

    }

    /**
     * 处理离线报警数据
     *
     * @param projectCraneDetail 解析后的明细数据
     * @param alarmData          报警数据中的报警信息代码
     */
    public int sendOfflineAlarmData(ProjectCraneDetail projectCraneDetail, String alarmData, ProjectInfo projectInfo) {
        String sn = projectCraneDetail.getDeviceNo();
        log.info("塔吊--设备{}处理离线报警数据开始", sn);
        // 实时更新统计缓存
        String cyclicWorkKey = GlobalStaticConstant.getStatisticsKey(xywgProerties.getRedisHead(), sn);
        ProjectCraneStatisticsDaily statisticsDaily;

        if (DateUtil.beginOfDay(new Date()).getTime() == DateUtil.beginOfDay(projectCraneDetail.getCreateTime()).getTime()) {
            //报警数据属于当天的
            log.info("塔吊--设备{}处理 当天 离线报警数据", sn);
            if (!redisUtil.exists(cyclicWorkKey)) {
                ProjectCrane crane = projectCraneService.selectOne(sn);
                statisticsDaily = new ProjectCraneStatisticsDaily(projectInfo.getId(), projectInfo.getName(), projectInfo.getBuilder(), crane.getOwner(), crane.getIdentifier(), crane.getCraneNo(), sn);
            } else {
                statisticsDaily = JSONUtil.toBean((String) redisUtil.get(cyclicWorkKey), ProjectCraneStatisticsDaily.class);
            }

        } else {
            log.info("塔吊--设备{}处理 非当天 离线报警数据", sn);
            statisticsDaily = null;
        }

        //32 表示位数    FFFFFFFF ---> ‭11111111111111111111111111111111‬
        //进来了说明至少是预警
        int status = GlobalStaticConstant.DEVICE_STATE_YUJING;
        int length = 16;
        for (int i = 0; i < length; i++) {
            String key = GlobalStaticConstant.getOfflineAlarmSatusKey(xywgProerties.getRedisHead(), sn, i);
            String lastAlarmKey = GlobalStaticConstant.getOfflineLastAlarmKey(xywgProerties.getRedisHead(), sn, i);
            //如果是1表示警告,0表示不是警告
            // 1-》0 则添加一个结束时间
            // 0-》1 则添加一条报警数据
            if (((Long.parseLong(alarmData, 16)) & (0x00000001 << i)) >> i == 1 && "1".equals(redisUtil.get(key))) {
                log.info("塔吊--设备{}处理 非当天 离线报警数据", sn);
                if ("1".equals(redisUtil.get(key))) {
                    //已经报过警了
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    //更新缓存
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));
                } else {
                    //添加一条报警数据
                    // 设置当前类型的报警状态为1
                    log.info("塔吊--设备{} 新增报警", sn);
                    redisUtil.set(key, "1");
                    int alarmId = i + 1;
                    status = deviceStatus(alarmId, status);

                    // 缓存
                    if (statisticsDaily != null) {
                        statisticsDaily = ProjectCraneStatisticsDaily.addAlarm(alarmId, statisticsDaily);
                        log.info("塔吊--设备{} 统计值+1", sn);
                    }


                    ProjectCraneAlarm alarm = new ProjectCraneAlarm();
                    String warningMessage = alarmCodeToMessage(alarmId, projectCraneDetail, alarm);
                    alarm.setCraneId(projectCraneDetail.getCraneId());
                    alarm.setDetailId(projectCraneDetail.getId());
                    alarm.setAlarmId(alarmId);
                    alarm.setAlarmInfo(warningMessage);
                    alarm.setDeviceNo(projectCraneDetail.getDeviceNo());
                    alarm.setIsDel(0);
                    alarm.setValue(alarm.getValue());
                    alarm.setDeviceTime(projectCraneDetail.getDeviceTime());
                    alarm.setCreateTime(projectCraneDetail.getDeviceTime());
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    //添加报警表
                    projectCraneAlarmService.insert(alarm);
                    //把最后一条保存起来
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));

                }

            } else {
                log.info("塔吊--设备{} 进入正常代码块", sn);
                if ("0".equals(redisUtil.get(key))) {
                    //不用操作
                    //已经报过警了
                    log.info("塔吊--设备{} 未报警", sn);
                } else {
                    //报警停止了
                    redisUtil.set(key, "0");
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    Wrapper<ProjectCraneAlarm> wrapper = new EntityWrapper<>();
                    wrapper.eq("create_time", alarm.getCreateTime());
                    wrapper.eq("id", alarm.getId());
                    projectCraneAlarmService.update(alarm, wrapper);
                    redisUtil.remove(lastAlarmKey);
                    log.info("塔吊--设备{} 报警停止", sn);
                }
            }


        }
        // 更新缓存信息
        if (statisticsDaily != null) {
            redisUtil.set(cyclicWorkKey, JSONUtil.toJsonStr(statisticsDaily));
        }
        log.info("塔吊--设备{}处理离线报警数据结束", sn);
        return 0;
    }

    private int deviceStatus(int alarmId, int currentStatus) {
        if (currentStatus == GlobalStaticConstant.DEVICE_STATE_CHAOBIAO) {
            return currentStatus;
        }
        if (alarmId == 1) {
            //超标
            return GlobalStaticConstant.DEVICE_STATE_CHAOBIAO;
        }
        if (alarmId == 16 || alarmId % 2 == 1) {
            //是报警
            return GlobalStaticConstant.DEVICE_STATE_BAOJING;
        } else {
            //是预警
            if (currentStatus > GlobalStaticConstant.DEVICE_STATE_YUJING) {
                // 之前已经是预警以上了
                return currentStatus;
            }
            return GlobalStaticConstant.DEVICE_STATE_YUJING;
        }
    }

    /**
     *  工作循环
     *   查询一次
     *   新增一次
     *   修改一次
     * @param channel 通道
     * @param monitorPojo 信息
     */
    public void operatingCycle(Channel channel, CompleteDataPojo monitorPojo){
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 工作循环",sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);

        Date start = DateUtils.parseDate(monitorPojo.getDataMap().get("0D"));
        Date end = DateUtils.parseDate(monitorPojo.getDataMap().get("0E"));

        // 查下这段时间内的报警数据
        Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
        alarmWrapper.between("create_time",start,end);
        //查询报警
        log.info("塔吊--设备{}查询报警数据",sn);
        List<ProjectCraneAlarm> alarms =  projectCraneAlarmService.selectList(alarmWrapper);
        String alarmInfo;
        if(alarms!=null&&alarms.size()>0){
            log.info("塔吊--设备{}组装报警信息",sn);
            alarmInfo =   getAlarmInfo(alarms);
        }else{
            log.info("塔吊--设备{}暂无报警信息",sn);
            alarmInfo  = "无" ;
        }
        String cyclicWorkKey = GlobalStaticConstant.getStatisticsKey(xywgProerties.getRedisHead(),sn);
        ProjectCraneStatisticsDaily statisticsDaily;
        log.info("塔吊--设备{}更新吊重次数",sn);
        if(!redisUtil.exists(cyclicWorkKey)){
            ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
            statisticsDaily = new ProjectCraneStatisticsDaily(projectInfo.getId(),projectInfo.getName(),projectInfo.getBuilder(),crane.getOwner(),crane.getIdentifier(),crane.getCraneNo(),sn);

        }else{
            statisticsDaily = JSONUtil.toBean((String)redisUtil.get(cyclicWorkKey),ProjectCraneStatisticsDaily.class);
        }


        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0F"), 16)/10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal begindgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal enddgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("03"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal beginheight=craneHeight.subtract(begindgHeight).setScale(2,RoundingMode.HALF_UP);
        BigDecimal endheight=craneHeight.subtract(enddgHeight).setScale(2,RoundingMode.HALF_UP);

        ProjectCraneCyclicWorkDuration workDuration = new ProjectCraneCyclicWorkDuration(
                crane.getId(),
                sn,
                Double.parseDouble(beginheight.toString()),
                Double.parseDouble(endheight.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("04"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("05"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("06"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"),16)/10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0A"),16)/10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0B"),16)/10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0C"),16)/10d,
                crane.getMultiple().doubleValue(),
                alarmInfo,
                start,
                end,
                start,
                Double.parseDouble(begindgHeight.toString()),
                Double.parseDouble(enddgHeight.toString())
                );
        log.info("塔吊--设备{} 组装工作循环",sn);
        statisticsDaily.setLiftFrequency(statisticsDaily.getLiftFrequency()+1);
        statisticsDaily =  ProjectCraneStatisticsDaily.addPercentage(workDuration.getMomentPercentage(),statisticsDaily);
        redisUtil.set(cyclicWorkKey,JSONUtil.toJsonStr(statisticsDaily));
        log.info("塔吊--设备{} 更新统计缓存(吊重次数+1，力矩百分比分类)",sn);
        workDurationService.insert(workDuration);
        Long timesum = crane.getTimeSum();
        if(timesum==null){
            crane.setTimeSum(end.getTime()-start.getTime());
        }else{
            crane.setTimeSum(timesum+end.getTime()-start.getTime());
        }

        projectCraneService.updateById(crane);
        projectCraneService.removeCache(sn);
        log.info("塔吊--设备{} 更新累计工作时间",sn);
        log.info("塔吊--设备{} 执行结束 工作循环",sn);
    }
    /**
     *  离线工作循环
     * @param channel 通道
     * @param monitorPojo 信息对象
     */
    public void offlineOperatingCycle(Channel channel, CompleteDataPojo monitorPojo){
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 离线工作循环",sn);



        ProjectCrane crane = projectCraneService.selectOne(sn);

        Date start = DateUtils.parseDate(monitorPojo.getDataMap().get("0D"));
        Date end = DateUtils.parseDate(monitorPojo.getDataMap().get("0E"));
        // 实时更新统计缓存
        String cyclicWorkKey = GlobalStaticConstant.getStatisticsKey(xywgProerties.getRedisHead(),sn);
        ProjectCraneStatisticsDaily statisticsDaily;

        if(DateUtil.beginOfDay(new Date()).getTime()==DateUtil.beginOfDay(start).getTime()){
            //报警数据属于当天的
            log.info("塔吊--设备{}处理 当天 离线报警数据",sn);
            if(!redisUtil.exists(cyclicWorkKey)){
                ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
                statisticsDaily = new ProjectCraneStatisticsDaily(projectInfo.getId(),projectInfo.getName(),projectInfo.getBuilder(),crane.getOwner(),crane.getIdentifier(),crane.getCraneNo(),sn);

            }else{
                statisticsDaily = JSONUtil.toBean((String)redisUtil.get(cyclicWorkKey),ProjectCraneStatisticsDaily.class);
            }

        }else{
            log.info("塔吊--设备{}处理 非当天 离线报警数据",sn);
            statisticsDaily=null;
        }
        // 查下这段时间内的报警数据
        Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
        alarmWrapper.between("create_time",start,end);
        //查询报警
        log.info("塔吊--设备{}查询报警数据",sn);
        List<ProjectCraneAlarm> alarms =  projectCraneAlarmService.selectList(alarmWrapper);
        String alarmInfo;
        if(alarms!=null&&alarms.size()>0){
            log.info("塔吊--设备{}组装报警信息",sn);
            alarmInfo =   getAlarmInfo(alarms);
        }else{
            log.info("塔吊--设备{}暂无报警信息",sn);
            alarmInfo  = "无" ;
        }
        log.info("塔吊--设备{}更新吊重次数",sn);
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0F"), 16)/10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal begindgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal enddgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("03"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal beginheight=craneHeight.subtract(begindgHeight).setScale(2,RoundingMode.HALF_UP);
        BigDecimal endheight=craneHeight.subtract(enddgHeight).setScale(2,RoundingMode.HALF_UP);
        ProjectCraneCyclicWorkDuration workDuration = new ProjectCraneCyclicWorkDuration(
                crane.getId(),
                sn,
                Double.parseDouble(beginheight.toString()),
                Double.parseDouble(endheight.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("04"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("05"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("06"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"),16)/1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"),16)/10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0A"),16)/10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0B"),16)/10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0C"),16)/10d,
                crane.getMultiple().doubleValue(),
                alarmInfo,
                start,
                end,
                start,
                Double.parseDouble(begindgHeight.toString()),
                Double.parseDouble(enddgHeight.toString())
                );
        log.info("塔吊--设备{} 组装工作循环",sn);
        if(statisticsDaily!=null){
            statisticsDaily.setLiftFrequency(statisticsDaily.getLiftFrequency()+1);
            statisticsDaily =  ProjectCraneStatisticsDaily.addPercentage(workDuration.getMomentPercentage(),statisticsDaily);
            redisUtil.set(cyclicWorkKey,JSONUtil.toJsonStr(statisticsDaily));
            log.info("塔吊--设备{} 更新统计缓存(吊重次数+1，力矩百分比分类)",sn);
        }else{
            log.info("塔吊--设备{} 非当天数据",sn);
        }

        workDurationService.insert(workDuration);
        Long timesum = crane.getTimeSum();
        if(timesum==null){
            crane.setTimeSum(end.getTime()-start.getTime());
        }else{
            crane.setTimeSum(timesum+end.getTime()-start.getTime());
        }

        projectCraneService.updateById(crane);
        projectCraneService.removeCache(sn);
        log.info("塔吊--设备{} 更新累计工作时间",sn);
        log.info("塔吊--设备{} 执行结束 离线工作循环",sn);
    }
    private String getAlarmInfo(List<ProjectCraneAlarm> alarms){
        StringBuffer stringBuffer =new StringBuffer();
        int status [] = new int[16];
        for(ProjectCraneAlarm alarm:alarms){
            status[alarm.getAlarmId()-1] = 1;
        }
        for(int i = 0; i<16;i++){
            if(status[i]==1){
                switch ((i+1)){
                    case 1:
                        stringBuffer.append("起重量报警;") ;
                        break;
                    case 2:
                        stringBuffer.append("起重量预警;");
                        break;
                    case 3:
                        stringBuffer.append("幅度向内报警;");
                        break;
                    case 4:
                        stringBuffer.append("幅度向内预警;");
                        break;
                    case 5:
                        stringBuffer.append("幅度向外报警;");
                        break;
                    case 6:
                        stringBuffer.append("幅度向外预警;");
                        break;
                    case 7:
                        stringBuffer.append("高度向上报警;");
                        break;
                    case 8:
                        stringBuffer.append("高度向上预警;");
                        break;
                    case 9:
                        stringBuffer.append("力矩报警;");
                        break;
                    case 10:
                        stringBuffer.append("力矩预警;");
                        break;
                    case 11:
                        stringBuffer.append("单机防碰撞报警;");
                        break;
                    case 12:
                        stringBuffer.append("单机防碰撞预警;");
                        break;
                    case 13:
                        stringBuffer.append("多机防碰撞报警;");
                        break;
                    case 14:
                        stringBuffer.append("多机防碰撞预警;");
                        break;
                    case 15:
                        stringBuffer.append("风速报警;");
                        break;
                    case 16:
                        stringBuffer.append("倾角报警;");
                        break;}

            }
        }
        return stringBuffer.toString();
    }
    /**
     * 离线原因
     * @param channel
     * @param monitorPojo
     */
    public void offlineReason(Channel channel, CompleteDataPojo monitorPojo){
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 累计工作时间",sn);
        String reason =  monitorPojo.getDataMap().get("03");
        if( Integer.parseInt(reason,16)==0){
            offline(channel,sn,GlobalStaticConstant.OFFLINE_REASON_NORMAL);
        }else{
            offline(channel,sn,GlobalStaticConstant.OFFLINE_REASON_NOPOWER);
        }
        // 清除登录信息
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(null);
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        log.info("塔吊--设备{} 执行完毕 累计工作时间",sn);
    }
    /**
     *  下线
     *  1. 修改设备状态为离线
     *  2. 删除设备缓存
     *  3. 添加更新受控数据
     *  4. 添加未受控数据
     *  5. 将报警状态重置，保证报警数据中含有离线时间
     *  6. 将离线报警状态重置，保证报警数据中含有离线时间
     * @param channel
     * @param sn
     * @param reason
     */
    public void offline(Channel channel,String sn,String reason){
        log.info("塔吊--设备{} 离线",sn);

        ProjectCrane projectCrane =  projectCraneService.selectOne(sn);;
        projectCrane.setDeviceNo(sn);
        projectCrane.setIsOnline(DEVICE_STATE_OFF_LINE);
        projectCrane.setModifyTime(new Date());
        projectCraneService.updateById(projectCrane);
        log.info("塔吊--设备{} 更新设备状态",sn);
        // 删除缓存
        projectCraneService.removeCache(sn);
        // 更新缓存
        projectCrane = projectCraneService.selectOne(sn);
        // 添加一条受控数据
        // 更新 受控数据的截止时间
        ProjectCraneHeartbeat projectEnvironmentHeartbeat = new ProjectCraneHeartbeat();
        projectEnvironmentHeartbeat.setDeviceNo(sn);
        projectEnvironmentHeartbeat.setCreateTime(new Date());
        projectEnvironmentHeartbeat.setReason(reason);
        projectEnvironmentHeartbeat.setCraneId(projectCrane.getId());
        projectCraneHeartbeatService.updateEndTime(projectEnvironmentHeartbeat);

        // 结束掉 报警
        modifyAlarmStatus(sn);
        // 结束掉 离线报警
        modifyOfflineAlarmStatus(sn);
        ProjectInfo projectInfo = projectInfoService.selectCacheOne(projectCrane.getProjectId());
        log.info("塔吊--设备{} 下线提醒",sn);
        //下线提醒
        commonMethod.pushOffline(projectInfo.getUuid(),projectInfo.getId()+"",sn);
    }
    //变更 报警状态
    private  void modifyOfflineAlarmStatus(String sn){
        for(int i=0;i<16;i++){
            String alarmSatusKey =  GlobalStaticConstant.getOfflineAlarmSatusKey(xywgProerties.getRedisHead(),sn,i) ;
            String lastAlarmKey =  GlobalStaticConstant.getOfflineLastAlarmKey(xywgProerties.getRedisHead(),sn,i) ;
            if(redisUtil.exists(alarmSatusKey)){
                if("1".equals(redisUtil.get(alarmSatusKey))){
                    redisUtil.set(alarmSatusKey,"0");
                    if(redisUtil.exists(lastAlarmKey)){
                        ProjectCraneAlarm alarm = JSONUtil.toBean((String)redisUtil.get(lastAlarmKey),ProjectCraneAlarm.class);
                        Wrapper wrapper = new EntityWrapper();
                        wrapper.eq("create_time",alarm.getCreateTime());
                        wrapper.eq("id",alarm.getId());
                        projectCraneAlarmService.update(alarm,wrapper);
                        redisUtil.remove(lastAlarmKey);
                    }

                }
            }else{
                redisUtil.set(alarmSatusKey,"0");
            }

        }
    }

    //结束报警状态
    private  void modifyAlarmStatus(String sn){
        for(int i=0;i<16;i++){
            String alarmSatusKey =  GlobalStaticConstant.getAlarmSatusKey(xywgProerties.getRedisHead(),sn,i) ;
            String lastAlarmKey =  GlobalStaticConstant.getLastAlarmKey(xywgProerties.getRedisHead(),sn,i) ;
            if(redisUtil.exists(alarmSatusKey)){
                if("1".equals(redisUtil.get(alarmSatusKey))){
                    redisUtil.set(alarmSatusKey,"0");
                    if(redisUtil.exists(lastAlarmKey)){
                        ProjectCraneAlarm alarm = JSONUtil.toBean((String)redisUtil.get(lastAlarmKey),ProjectCraneAlarm.class);
                        Wrapper wrapper = new EntityWrapper();
                        wrapper.eq("create_time",alarm.getCreateTime());
                        wrapper.eq("id",alarm.getId());
                        projectCraneAlarmService.update(alarm,wrapper);
                        redisUtil.remove(lastAlarmKey);
                    }

                }
            }else{
                redisUtil.set(alarmSatusKey,"0");
            }

        }
    }
    /**
     * 将报警id转换为报警信息
     *
     * @return str
     */
    private String alarmCodeToMessage(Integer alarmCode, ProjectCraneDetail detail, ProjectCraneAlarm alarm) {
        String message;
        switch (alarmCode) {
            case 1:
                message = "起重量报警";
                alarm.setValue(detail.getWeight().toString());
                break;
            case 2:
                message = "起重量预警";
                alarm.setValue(detail.getWeight().toString());
                break;
            case 3:
                message = "幅度向内报警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 4:
                message = "幅度向内预警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 5:
                message = "幅度向外报警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 6:
                message = "幅度向外预警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 7:
                message = "高度向上报警";
                alarm.setValue(detail.getHeight().toString());
                break;
            case 8:
                message = "高度向上预警";
                alarm.setValue(detail.getHeight().toString());
                break;
            case 9:
                message = "力矩报警";
                alarm.setValue(detail.getMoment().toString());
                break;
            case 10:
                message = "力矩预警";
                alarm.setValue(detail.getMoment().toString());
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
                alarm.setValue(detail.getWindSpeed().toString());
                break;
            case 16:
                message = "倾角报警";
                alarm.setValue(detail.getTiltAngle().toString());
                break;
            default:
                message = "告警信息类型未定义";
                break;
        }
        return message;
    }


}