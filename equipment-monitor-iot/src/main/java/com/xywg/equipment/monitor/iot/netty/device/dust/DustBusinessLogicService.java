package com.xywg.equipment.monitor.iot.netty.device.dust;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.BaiDuUtil;
import com.xywg.equipment.monitor.iot.core.util.DateUtils;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.envmon.dao.ProjectEnvironmentMonitorAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.envmon.dao.ProjectEnvironmentMonitorDetailMapper;
import com.xywg.equipment.monitor.iot.modular.envmon.dao.ProjectEnvironmentMonitorMapper;
import com.xywg.equipment.monitor.iot.modular.envmon.factory.MonitorAlarmFactory;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentHeartbeat;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitorAlarm;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitorDetail;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentHeartbeatService;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentMonitorService;
import com.xywg.equipment.monitor.iot.modular.romote.handle.RemoteSetupService;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSpray;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayBindService;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayService;
import com.xywg.equipment.monitor.iot.netty.aop.ZbusProducerHolder;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.DuctDataPojoVo;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.NettyChannelManage;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import com.xywg.equipment.monitor.iot.netty.device.spray.SprayBusinessLogicService;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.xywg.equipment.global.GlobalStaticConstant.DATA_PACKAGE_RESPONSE;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.decode;


/**
 * @author hjy
 * @date 2018/9/20
 * 环境监控 业务逻辑处理服务类(扬尘)
 */
@Service
public class DustBusinessLogicService {
    @Autowired
    private IProjectEnvironmentMonitorService projectEnvironmentMonitorService;
    @Autowired
    private DustHandlerService monitorHandlerService;
    @Autowired
    private ProjectEnvironmentMonitorDetailMapper projectEnvironmentMonitorDetailMapper;
    @Autowired
    private ProjectEnvironmentMonitorAlarmMapper alarmMapper;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private PushService pushService;
    @Autowired
    protected XywgProerties xywgProerties;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired
    private IProjectEnvironmentHeartbeatService projectEnvironmentHeartbeatService;
    @Autowired
    private RemoteSetupService remoteSetup;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private ProjectEnvironmentMonitorMapper projectEnvironmentMonitorMapper;
    @Autowired
    private ProjectSprayService projectSprayService;
    @Autowired
    private SprayBusinessLogicService sprayBusinessLogicService;
    @Autowired
    private ProjectSprayBindService projectSprayBindService;

    @Autowired
    private ZbusProducerHolder zbusProducerHolder;

    private Logger logger = LoggerFactory.getLogger(DustBusinessLogicService.class);

    /**
     * 登录 只包含(sn,firmware)
     *
     * @param monitorPojo
     */
    public void login(Channel ctx, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));

        //更新map有效的链接
        NettyChannelManage.CHANNEL_MAP.put(sn, ctx);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        DeviceConnectInfo deviceConnectInfo = new DeviceConnectInfo(uuid, sn);
        ctx.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceConnectInfo);

        //设备版本号(固件版本号)
        String deviceVersion = commonMethod.getDeviceVersion(monitorPojo.getDataMap().get("02"));


        ProjectEnvironmentMonitor projectEnvironmentMonitor = new ProjectEnvironmentMonitor();
        projectEnvironmentMonitor.setDeviceNo(sn);
        projectEnvironmentMonitor.setModifyTime(new Date());
        projectEnvironmentMonitor.setFirmwareVersion(deviceVersion);
        Wrapper<ProjectEnvironmentMonitor> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no", sn);
        wrapper.eq("is_del", 0);
        projectEnvironmentMonitorMapper.update(projectEnvironmentMonitor, wrapper);

    }

    /**
     * 心跳
     *
     * @param monitorPojo
     */
    public void heartbeat(Channel ctx, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        //获取监控数据设置的阈值
        ProjectEnvironmentMonitor monitor = projectEnvironmentMonitorService.selectOne(sn);
        //业务处理

        ProjectEnvironmentHeartbeat heartbeat = new ProjectEnvironmentHeartbeat();
        heartbeat.setDeviceNo(sn);
        heartbeat.setMonitorId(monitor.getId());
        heartbeat.setCreateTime(new Date());
        if (!redisUtil.exists(this.xywgProerties.getRedisHead() + ":head:ycht:" + sn)) {
            //不存在 说明是开机
            //上一条数据是关机
            projectEnvironmentHeartbeatService.doOpenBusiness(heartbeat);
        } else {
            projectEnvironmentHeartbeatService.updateEndTime(heartbeat);

        }
        redisUtil.setSec(this.xywgProerties.getRedisHead() + ":head:ycht:" + sn, 1, 300L);
    }

    /**
     * 同步时间
     *
     * @param monitorPojo
     */
    public void synchronizationTime(Channel ctx, CompleteDataPojo monitorPojo) {
        String todayString = "";
        String tomorrowString = "";
        //序列号 16进制的:
        String sn = monitorPojo.getDataMap().get("01");
        //查询当前设备的经纬度地址
        ProjectEnvironmentMonitor projectEnvironmentMonitor = projectEnvironmentMonitorService.selectOne(decode(sn));
        String placePoint = projectEnvironmentMonitor.getPlacePoint();
        if (StringUtils.isNotBlank(placePoint)) {
            //使用经纬度 调用百度API 获取天气信息
            String resMessage = BaiDuUtil.getWeather(placePoint, "json");
            Map map = JSON.parseObject(resMessage, Map.class);
            //只有返回正确数据的的时候才处理
            String flag = "success";
            if (flag.equals(map.get("status"))) {
                List resultsList = (List) map.get("results");
                Map result = (Map) resultsList.get(0);
                List weatherList = (List) result.get("weather_data");
                //取今明两天的天气
                Map today = (Map) weatherList.get(0);
                todayString = getWeatherStr(today);
                Map tomorrow = (Map) weatherList.get(1);
                tomorrowString = getWeatherStr(tomorrow);
            }
        }
        //commonMethod.synchronizationTime(ctx, sn, monitorPojo.getCommand(), monitorPojo.getVersion());
        //获取服务器当前时间
        String dateTimeStr = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        //02 代表功能数据的id  即回复的时间
        linkedHashMap.put("02", dateTimeStr);
        if (StringUtils.isNotBlank(todayString)) {
            linkedHashMap.put("03", todayString);
        }
        if (StringUtils.isNotBlank(tomorrowString)) {
            linkedHashMap.put("04", tomorrowString);
        }
        commonMethod.resMessageJoint(ctx, sn, linkedHashMap, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }


    /**
     * 传输升级数据
     *
     * @param monitorPojo
     */
    public void upgradeTransferData(Channel ctx, CompleteDataPojo monitorPojo) {
        commonMethod.upgradeTransfer(ctx, monitorPojo);
    }

    /**
     * 升级文件发送完毕回复
     *
     * @param monitorPojo
     */
    public void upgradeFileSentOver(CompleteDataPojo monitorPojo) {
        //暂时不做处理


    }

    /**
     * 上传设备异常日志
     *
     * @param monitorPojo
     */
    public void exceptionalLog(Channel ctx, CompleteDataPojo monitorPojo) {
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        String exceptionMessageType = monitorPojo.getDataMap().get("02").replaceAll(" ", "");

        remoteSetup.deviceDustExceptionalLog(sn, exceptionMessageType, GlobalStaticConstant.DEVICE_TYPE_FLAG_DUST);
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
     * 上传监控数据 (sn,windSpeed,windForce,windDirection,PM2.5,PM10,temperature,humidity,noise)
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorData(Channel ctx, CompleteDataPojo monitorPojo) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        DuctDataPojoVo ductDataPojoVo = new DuctDataPojoVo();
        BeanUtils.copyProperties(monitorPojo, ductDataPojoVo);
        /*0x01	序列号	STRING 0x02	风速	STRING 0x03	风力	STRING  0x04	风向	STRING  0x05	PM2.5	STRING
        0x06	PM10	STRING 0x07	温度	STRING 0x08	湿度	STRING  0x09	噪声	STRING*/
        //将key  转为属性
        for (String key : monitorPojo.getDataMap().keySet()) {
            ductDataPojoVo = monitorHandlerService.getMonitorPojoData(ductDataPojoVo, key, monitorPojo.getDataMap().get(key));
        }
        //获取监控数据设置的阈值
        ProjectEnvironmentMonitor projectEnvironmentMonitor = projectEnvironmentMonitorService.selectOne(ductDataPojoVo.getSn());
        if (projectEnvironmentMonitor == null) {
            return;
        }
        ProjectInfo projectInfo = projectInfoService.selectById(projectEnvironmentMonitor.getProjectId());
        if (projectInfo == null) {
            throw new RuntimeException("设备" + projectEnvironmentMonitor.getDeviceNo() + "不在任何项目下");
        }

        ProjectEnvironmentMonitorDetail projectEnvironmentMonitorDetail = new ProjectEnvironmentMonitorDetail(
                projectEnvironmentMonitor.getId(),
                ductDataPojoVo.getSn(),
                Double.parseDouble(ductDataPojoVo.getPm10()),
                Double.parseDouble(ductDataPojoVo.getPm25()),
                Double.parseDouble(ductDataPojoVo.getNoise()),
                Double.parseDouble(ductDataPojoVo.getWindSpeed()),
                ductDataPojoVo.getWindDirection(),
                new Date(),
                Double.parseDouble(ductDataPojoVo.getTemperature()),
                Double.parseDouble(ductDataPojoVo.getHumidity()),
                Double.parseDouble(ductDataPojoVo.getWindForce()),
                null,
                null,
                0,
                new Date(),
                null
        );


        //判断是否需要转发
        if(redisUtil.exists(xywgProerties.getRedisYcDispatchPrefix() + ductDataPojoVo.getSn()))
        {
            try {
                zbusProducerHolder.sendDispatchMessage(dataToStr(projectEnvironmentMonitorDetail));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String tableName = "t_project_environment_monitor_detail_" + DateUtils.getDate().replaceAll("-", "").substring(0, 6);
        //保存解析后的数据
        projectEnvironmentMonitorDetailMapper.createDetail(projectEnvironmentMonitorDetail, tableName);
        //处理喷淋系统的控制
        handleSprayControl(projectEnvironmentMonitor, projectEnvironmentMonitorDetail);
        try {
            //添加到redis和
            //推送时时数据
            commonMethod.push(redisUtil, projectEnvironmentMonitor.getDeviceNo(), "monitor", projectInfo.getUuid(), projectEnvironmentMonitorDetail, projectEnvironmentMonitor.getProjectId() + "");

            //比较阈值看是否满足报警条件 如果满足那么存到报警信息表去
            // 比对 阈值
            List<ProjectEnvironmentMonitorAlarm> alarms = MonitorAlarmFactory.factory(projectEnvironmentMonitor, projectEnvironmentMonitorDetail);
            List<AlarmDTO> alarmInfo = new ArrayList<>();
            for (ProjectEnvironmentMonitorAlarm alarm : alarms) {
                alarmMapper.createAlarm(alarm, BaseFactory.getTableName(ProjectEnvironmentMonitorAlarm.class));
                alarmInfo.add(AlarmDTOFactory.factory(alarm, projectInfo, "扬尘设备"));
                //推送到移动端
                pushService.pushMob("注意", AlarmDTOFactory.factoryMessage(alarm, projectInfo, "扬尘设备"), projectInfo.getUuid());

            }
            if (alarmInfo.size() > 0) {
                ResultDTO redisData = ResultDTO.factory(alarmInfo, 20);
                //放入缓存
                //推送报警数据到web页面
                commonMethod.push(redisUtil, projectEnvironmentMonitor.getDeviceNo(), "monitor_alarm", projectInfo.getUuid(), redisData, projectEnvironmentMonitor.getProjectId() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //TODO 转成我们自己的解析后格式 deviceNo...什么的
    private String dataToStr(ProjectEnvironmentMonitorDetail detail) {
        StringBuffer sb = new StringBuffer();
        sb.append("sdsyr:").append(detail.getDeviceNo())
                .append(",a:").append(String.format("%03d",detail.getPm25().longValue()))
                .append(",b:").append(String.format("%03d",detail.getPm10().longValue()))
                .append(",c:").append(detail.getTemperature())
                .append(",d:").append(detail.getHumidity())
                .append(",e:").append(String.format("%03d",detail.getNoise().longValue()))
                .append(",f:").append("0")
                .append(",g:").append("0")
                .append(",h:").append("0")
                .append(",i:").append("0")
                .append(",j:").append(detail.getWindSpeed())
                .append(",k:").append(getWindDirectionAngle(detail.getWindDirection()))
                .append(",l:").append("0")
                .append(",m:").append("0");
        return sb.toString();
    }

    private String getWindDirectionAngle(String name){
        for(WindDirection direction : WindDirection.values()){
            if(direction.name.equals(name)){
                return direction.angle;
            }
        }
        return "000";
    }


    private static String getGBK16(String gbkString) {
        StringBuilder sb = new StringBuilder();
        try {

            byte[] ssChar = gbkString.getBytes("GBK");
            for (byte b : ssChar) {
                sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
            }
            //System.out.println(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 拼接每天的天气信息
     *
     * @param map
     * @return
     */
    private String getWeatherStr(Map map) {
        String weather = map.get("weather").toString();
        String wind = map.get("wind").toString();
        String temperature = map.get("temperature").toString();
        //获得第一段的内码
        String oneString = getGBK16(weather);
        //获得第二段的内码
        String twoString = getGBK16(wind);
        //获得第三段的内码
        String threeString = getGBK16(temperature);
        // System.out.println(getSectionCode(oneString)+"---"+getSectionCode(twoString)+"---"+getSectionCode(threeString) );
        return getSectionCode(oneString) + getSectionCode(twoString) + getSectionCode(threeString);
    }

    /**
     * 组装每一段编码  每一段的字符长度为16字节  不足时 前后补空格 字符在中间
     * 字符空格对应的 16对应 为0x20
     */
    private String getSectionCode(String info) {

        Integer maxLength = 16;
        //一个字节转化字符型后占2位
        Integer k = 2;
        //一共需要拼接的空格数
        Integer number = (maxLength * k - info.length()) / 2;
        //因为文字需要居中,所以前后各拼接一半的字符串   例如一共需要拼接5个空格,这个时间就前2后3
        StringBuilder sb = new StringBuilder();
        if (number % 2 == 0) {
            for (int i = 0; i < number / 2; i++) {
                sb.append("20");
            }
            sb.append(info);
            for (int i = 0; i < number / 2; i++) {
                sb.append("20");
            }
        } else {
            for (int i = 0; i < (number - 1) / 2; i++) {
                sb.append("20");
            }
            sb.append(info);
            for (int i = 0; i < (number + 1) / 2; i++) {
                sb.append("20");
            }
        }

        return sb.toString();
    }


    /**
     * 根据PM值判断是否超标来控制处理喷淋系统
     *
     * @param thresholdValue 数据库中设置的值
     * @param detail         收到的数据明细
     */
    private void handleSprayControl(ProjectEnvironmentMonitor thresholdValue, ProjectEnvironmentMonitorDetail detail) {
        //阈值threshold
        Double thresholdPm25 ;
        Double thresholdPm10 ;

        Double detailPm25 = detail.getPm25();
        Double detailPm10 = detail.getPm10();

        String detailStr="pm2.5="+detailPm25+",pm10="+detailPm10;
        //查询绑定的喷淋设备

        List<ProjectSpray>    bindVoList= projectSprayBindService.getListProjectSprayBind(thresholdValue.getId());
        List<ProjectSpray> shouldOpenList = new LinkedList();
        List<ProjectSpray> shouldCloseList = new LinkedList();
        if (bindVoList.size() == 0) {
            return;
        }
        for(ProjectSpray spray:  bindVoList){
            thresholdPm25 = spray.getPm25();
            thresholdPm10 = spray.getPm10();
            if (detailPm25 > thresholdPm25 || detailPm10 > thresholdPm10) {
                //开启喷淋
                shouldOpenList.add(spray);

            } else {
                //关闭喷淋
                shouldCloseList.add(spray);

            }
        }
        if(CollectionUtils.isEmpty(shouldOpenList)){
            sprayBusinessLogicService.issueSwitchInstructions(shouldOpenList, "01",detailStr,0);
        }
        if (CollectionUtils.isEmpty(shouldCloseList)){
            sprayBusinessLogicService.issueSwitchInstructions(bindVoList, "00",detailStr,0);
        }




    }


}