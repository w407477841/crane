package com.xywg.equipment.monitor.iot.netty.device.crane;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xingyun.crane.cache.RedisUtil;
import com.xywg.equipment.monitor.iot.config.rabbitmq.RabbitConfig;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneCalibrationLogMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneSingleCollisionAvoidanceSetMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dto.CurrentCraneData;
import com.xywg.equipment.monitor.iot.modular.crane.model.*;
import com.xywg.equipment.monitor.iot.modular.crane.service.*;
import com.xywg.equipment.monitor.iot.modular.romote.handle.RemoteSetupService;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.dto.RequestDTO;
import com.xywg.equipment.monitor.iot.netty.device.dto.ResponseDTO;
import com.xywg.equipment.monitor.iot.netty.device.dto.WSAlarmDTO;
import com.xywg.equipment.monitor.iot.netty.device.dto.WSDetailDTO;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.NettyChannelManage;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static com.xywg.equipment.global.GlobalStaticConstant.*;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.decode;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.stringToHexString;

/**
 * @author hjy
 * @date 2018/9/20
 * 环境监控 业务逻辑处理服务类(塔吊)
 */
@Component
@SuppressWarnings("all")
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
    private IProjectCraneDetailService projectCraneDetailService;

    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private IProjectCraneAlarmService projectCraneAlarmService;
    @Autowired
    protected PushService pushService;
    @Autowired
    private RemoteSetupService remoteSetup;
    @Autowired
    private ProjectCraneCalibrationLogMapper projectCraneCalibrationLogMapper;
    @Autowired
    private ProjectCraneSingleCollisionAvoidanceSetMapper craneSingleAvoidanceSetMapper;
    @Autowired
    private IProjectCraneCyclicWorkDurationService workDurationService;
    @Autowired
    private IProjectCraneMutiCollisionAvoidanceSetService mutiCollisionAvoidanceSetService;
    @Autowired
    CraneBusinessLogicService craneBusinessLogicService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 登录 只包含(sn,firmware)
     * 修改在线状态
     * 修改版本号
     * 设备切换项目前，先禁用原项目下设备，新项目安装完毕后，在新项目下添加设备并启用
     *
     * @param monitorPojo 数据
     */
    public void login(RequestDTO requestDTO) {

        //序列号:
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{}开始执行 登录", sn);
        ProjectCrane projectCrane = projectCraneService.selectOne(sn);
        if (projectCrane == null) {
            log.error("塔吊--非法数据({} 不存在/未启用)", sn);
            ResponseDTO responseDTO = new ResponseDTO(sn, requestDTO.getCmd(), 801, null);
            commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));
        }

        ProjectInfo projectInfo = projectInfoService.selectCacheOne(projectCrane.getProjectId());
        if (projectInfo == null) {
            log.error("塔吊--错误数据({} 不在项目任何下)", sn);
            ResponseDTO responseDTO = new ResponseDTO(sn, requestDTO.getCmd(), 802, null);
            commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));
        }

        //设备版本号(固件版本号) , 设备在线
        String deviceVersion = requestDTO.getData();
        projectCrane = new ProjectCrane();
        projectCrane.setDeviceNo(sn);
        projectCrane.setIsOnline(DEVICE_STATE_ON_LINE);
        projectCrane.setFirmwareVersion(getDeviceVersion(deviceVersion));
        projectCrane.setModifyTime(new Date());
        Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no", sn);
        wrapper.eq("is_del", 0);
        projectCraneService.update(projectCrane, wrapper);
        // 删除缓存
        projectCraneService.removeCache(sn);
        log.info("塔吊--设备{}更新设备信息", sn);
        // 更新 设备报警状态
        modifyAlarmStatus(sn);
        log.info("塔吊--设备{}更新16项报警状态{}", sn);
        // 更新 设备离线报警状态
        modifyOfflineAlarmStatus(sn);
        log.info("塔吊--设备{}更新16项离线报警状态{}", sn);
        // 更新设备缓存
        projectCrane = projectCraneService.selectOne(sn);
        // 添加一条受控数据

        ProjectCraneHeartbeat heartbeat = new ProjectCraneHeartbeat();
        heartbeat.setDeviceNo(sn);
        heartbeat.setCraneId(projectCrane.getId());
        heartbeat.setCreateTime(new Date());
        projectCraneHeartbeatService.doOpenBusiness(heartbeat);
        log.info("塔吊--设备{}修正 t_project_crane_heartbeat 数据", sn);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 16进制字符串, 至少四位
        String id;
        if (projectCrane.getProjectId() < 1000) {
            id = String.format("%04d", projectCrane.getProjectId());
        } else {
            id = String.valueOf(projectCrane.getProjectId());
        }
        // id
        map.put("02", stringToHexString(id));
        //收到消息时回复给客户端

       // map.put("01", CommonStaticMethod.stringToHexString(sn));
        //上线提醒
        commonMethod.pushOnline(projectInfo.getUuid(), projectInfo.getId().toString(), sn);
        String hexMessage = commonMethod.messageJoint(CommonStaticMethod.stringToHexString(sn), map, requestDTO.getCmd(), "01", GlobalStaticConstant.DATA_PACKAGE_RESPONSE);
        ResponseDTO responseDTO = new ResponseDTO(sn, requestDTO.getCmd(), 200, hexMessage);
        commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));

    }
    /**
     * 转换版本号
     *
     * @return
     */
    private String getDeviceVersion(String version) {
        StringBuilder sb = new StringBuilder();
        sb.append("v");
        for (int i = 0; i < version.length(); i = i + 2) {
            sb.append(Integer.parseInt(version.substring(i, i + 2),16)).append(".");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    //变更 报警状态
    private void modifyAlarmStatus(String sn) {
        for (int i = 0; i < 16; i++) {
            String alarmSatusKey = GlobalStaticConstant.getAlarmSatusKey(xywgProerties.getRedisHead(), sn, i);
            String lastAlarmKey = GlobalStaticConstant.getLastAlarmKey(xywgProerties.getRedisHead(), sn, i);
            if (redisUtil.exists(alarmSatusKey)) {
                if ("1".equals(redisUtil.get(alarmSatusKey))) {
                    redisUtil.set(alarmSatusKey, "0");
                    if (redisUtil.exists(lastAlarmKey)) {
                        ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                        Wrapper wrapper = new EntityWrapper();
                        wrapper.eq("create_time", alarm.getCreateTime());
                        wrapper.eq("id", alarm.getId());
                        projectCraneAlarmService.update(alarm, wrapper);
                        redisUtil.remove(lastAlarmKey);
                    }

                }
            } else {
                redisUtil.set(alarmSatusKey, "0");
            }

        }
    }

    //变更 报警状态
    private void modifyOfflineAlarmStatus(String sn) {
        for (int i = 0; i < 16; i++) {
            String alarmSatusKey = GlobalStaticConstant.getOfflineAlarmSatusKey(xywgProerties.getRedisHead(), sn, i);
            String lastAlarmKey = GlobalStaticConstant.getOfflineLastAlarmKey(xywgProerties.getRedisHead(), sn, i);
            if (redisUtil.exists(alarmSatusKey)) {
                if ("1".equals(redisUtil.get(alarmSatusKey))) {
                    redisUtil.set(alarmSatusKey, "0");
                    if (redisUtil.exists(lastAlarmKey)) {
                        ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                        Wrapper wrapper = new EntityWrapper();
                        wrapper.eq("create_time", alarm.getCreateTime());
                        wrapper.eq("id", alarm.getId());
                        projectCraneAlarmService.update(alarm, wrapper);
                        redisUtil.remove(lastAlarmKey);
                    }

                }
            } else {
                redisUtil.set(alarmSatusKey, "0");
            }

        }
    }


    /**
     * 下线
     * 1. 修改设备状态为离线
     * 2. 删除设备缓存
     * 3. 添加更新受控数据
     * 4. 添加未受控数据
     * 5. 将报警状态重置，保证报警数据中含有离线时间
     * 6. 将离线报警状态重置，保证报警数据中含有离线时间
     *
     * @param channel
     * @param sn
     * @param reason
     */
    public void offline(RequestDTO requestDTO) {
        String sn=requestDTO.getSn();
        String reason=requestDTO.getData();
        log.info("塔吊--设备{} 离线",sn );
        ProjectCrane projectCrane = projectCraneService.selectOne(sn);
        projectCrane.setIsOnline(DEVICE_STATE_OFF_LINE);
        projectCrane.setModifyTime(new Date());
        projectCraneService.updateById(projectCrane);
        log.info("塔吊--设备{} 更新设备状态", sn);
        // 删除缓存
        projectCraneService.removeCache(sn);
        // 更新缓存
        modifyAlarmStatus(sn);
        // 结束掉 离线报警
        modifyOfflineAlarmStatus(sn);
        // 添加一条受控数据
        // 更新 受控数据的截止时间
        ProjectCraneHeartbeat projectEnvironmentHeartbeat = new ProjectCraneHeartbeat();
        projectEnvironmentHeartbeat.setDeviceNo(sn);
        projectEnvironmentHeartbeat.setCreateTime(new Date());
        projectEnvironmentHeartbeat.setReason(reason);
        projectEnvironmentHeartbeat.setCraneId(projectCrane.getId());
        projectCraneHeartbeatService.updateEndTime(projectEnvironmentHeartbeat);
        ProjectInfo projectInfo = projectInfoService.selectCacheOne(projectCrane.getProjectId());
        Map<String,Object> info=new HashMap<>();
        info.put("uuid",projectInfo.getUuid());
        info.put("id",projectInfo.getId());
        ResponseDTO responseDTO = new ResponseDTO(sn, "0018", 200, JSONUtil.toJsonStr(info));
        commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));
    }

    /**
     * 下线执行逻辑
     *
     * @param sn
     * @param reason
     */
//    public void offline(String sn, String reason) {
//        //this.offline(null, sn, reason);
//    }

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
    public void upgradeTransferData(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.upgradeTransfer(channel, monitorPojo);
    }

    /**
     * 升级文件发送完毕回复
     *
     * @param monitorPojo 数据
     */
    public void upgradeFileSentOver(CompleteDataPojo monitorPojo) {
        //暂时不做处理


    }

    /**
     * 上传设备异常情况数据
     *
     * @param monitorPojo 数据
     */
    public void exceptionalLog(RequestDTO requestDTO) {
        //序列号:
        String sn = requestDTO.getSn();
        String exception = requestDTO.getData();
        remoteSetup.deviceCraneExceptionalLog(sn, exception, DEVICE_TYPE_FLAG_CRANE);
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
     * 1.新增监控数据
     * 2.新增报警数据
     * 3.修改设备报警状态
     * @param monitorPojo 完整的原始数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void uploadMonitorData(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{}开始执行 监控数据", sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        log.info("塔吊--设备{}回复", sn);
        ProjectCraneDetail projectCraneDetail = JSONUtil.toBean(requestDTO.getData(), ProjectCraneDetail.class);
        projectCraneDetail.setCraneId(crane.getId());
//        //当前高度 = 塔吊高度 - 实时数据高度
//        projectCraneDetail.setHeight(Double.parseDouble(crane.getCurrentHeight())-projectCraneDetail.getHeight());
        log.info("塔吊--设备{}组装数据", sn);
        ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
        //数据明细表
        projectCraneDetailService.insert(projectCraneDetail);
        CurrentCraneData currentCraneData = CurrentCraneData.factory(projectCraneDetail, crane);
        //检查是否是报警信息  0A表示报警id
        String alarmData = projectCraneDetail.getAlarmInfo();
        //将时时数据推送到WebSocket
        WSDetailDTO detailDTO =new WSDetailDTO();
        detailDTO.setCurrentCraneData(currentCraneData);;
        detailDTO.setSn(sn);
        detailDTO.setProjectId(crane.getProjectId()+"");
        detailDTO.setType("crane");
        detailDTO.setUuid(projectInfo.getUuid());
        ResponseDTO responseDTO =new ResponseDTO(sn,"0020",200,JSONUtil.toJsonStr(detailDTO));
        commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));
        log.info("塔吊--设备{}推送数据", sn);
        //00000000 表示正常数据
        String alarmTypeNormal = "00000000";
        int alarmStatus  ;
        if (StringUtils.isBlank(alarmData) || alarmTypeNormal.equals(alarmData)) {
            log.info("塔吊--设备{}正常数据", sn);
            alarmStatus = 0 ;
            if(crane.getStatus() == 0) {
                log.info("塔吊--设备{}未报警，且之前未报警", sn);
                return ;
            }
        }else{
            try {
                alarmStatus = sendAlarmData(projectCraneDetail, alarmData, projectInfo);
                log.info("塔吊--设备{}发送报警数据", sn);

            } catch (Exception e) {
                e.printStackTrace();
                return ;
            }
        }
        ProjectCrane updateCraneStatus = new ProjectCrane();
        updateCraneStatus.setAlarmStatus(alarmStatus);
        updateCraneStatus.setId(crane.getId());
        log.info("塔吊--设备{}修改设备报警状态", sn);
        projectCraneService.updateById(updateCraneStatus);
        projectCraneService.removeCache(sn);
        log.info("塔吊--设备{}执行结束 监控数据", sn);
    }

    /**
     * 上传离线监控数据
     * 1.新增监控数据
     * 2.新增报警数据
     * 3.修改设备报警状态
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadOfflineMonitorData(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{}开始执行 离线监控数据", sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
        //数据明细表
        ProjectCraneDetail projectCraneDetail = JSONUtil.toBean(requestDTO.getData(), ProjectCraneDetail.class);
        projectCraneDetail.setCraneId(crane.getId());
        //当前高度 = 塔吊高度 - 实时数据高度
        //projectCraneDetail.setHeight(Double.parseDouble(crane.getCurrentHeight())-projectCraneDetail.getHeight());
        projectCraneDetailService.insert(projectCraneDetail);
        CurrentCraneData currentCraneData = CurrentCraneData.factory(projectCraneDetail, crane);
        //检查是否是报警信息  0A表示报警id
        String alarmData = projectCraneDetail.getAlarmInfo();
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
        log.info("塔吊--设备{}执行完毕 离线监控数据", sn);
    }

    /**
     * 工作循环
     * 查询一次
     * 新增一次
     * 修改一次
     *
     * @param channel
     * @param monitorPojo
     */
    public void operatingCycle(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{}开始执行 工作循环", sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        ProjectCraneCyclicWorkDuration duration = JSONUtil.toBean(requestDTO.getData(), ProjectCraneCyclicWorkDuration.class);
        Date start = duration.getBeginTime();
        Date end = duration.getEndTime();
        // 查下这段时间内的报警数据
        Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
        alarmWrapper.between("create_time", start, end);
        //查询报警
        log.info("塔吊--设备{}查询报警数据", sn);
        List<ProjectCraneAlarm> alarms = projectCraneAlarmService.selectList(alarmWrapper);
        String alarmInfo;
        if (alarms != null && alarms.size() > 0) {
            log.info("塔吊--设备{}组装报警信息", sn);
            alarmInfo = getAlarmInfo(alarms);
        } else {
            log.info("塔吊--设备{}暂无报警信息", sn);
            alarmInfo = "无";
        }
        String cyclicWorkKey = GlobalStaticConstant.getStatisticsKey(xywgProerties.getRedisHead(), sn);
        ProjectCraneStatisticsDaily statisticsDaily;
        log.info("塔吊--设备{}更新吊重次数", sn);
        if (!redisUtil.exists(cyclicWorkKey)) {
            ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
            statisticsDaily = new ProjectCraneStatisticsDaily(projectInfo.getId(), projectInfo.getName(), projectInfo.getBuilder(), crane.getOwner(), crane.getIdentifier(), crane.getCraneNo(), sn);

        } else {
            statisticsDaily = JSONUtil.toBean((String) redisUtil.get(cyclicWorkKey), ProjectCraneStatisticsDaily.class);
        }

        log.info("塔吊--设备{} 组装工作循环", sn);
        statisticsDaily.setLiftFrequency(statisticsDaily.getLiftFrequency() + 1);
        statisticsDaily = ProjectCraneStatisticsDaily.addPercentage(duration.getMomentPercentage(), statisticsDaily);
        redisUtil.set(cyclicWorkKey, JSONUtil.toJsonStr(statisticsDaily));
        log.info("塔吊--设备{} 更新统计缓存(吊重次数+1，力矩百分比分类)", sn);
        duration.setCraneId(crane.getId());
        duration.setMultipleRate(crane.getMultipleRate().doubleValue());
        duration.setAlarmInfo(alarmInfo);
//        duration.setBeginHeight(duration.getBeginHeight());
//        duration.setEndHeight(duration.getEndHeight());
        workDurationService.insert(duration);
        Long timesum = crane.getTimeSum();
        if (timesum == null) {
            crane.setTimeSum(end.getTime() - start.getTime());
        } else {
            crane.setTimeSum(timesum + end.getTime() - start.getTime());
        }
        projectCraneService.updateById(crane);
        projectCraneService.removeCache(sn);
        log.info("塔吊--设备{} 更新累计工作时间", sn);
        log.info("塔吊--设备{} 执行结束 工作循环", sn);
    }

    /**
     * 离线工作循环
     *
     * @param channel
     * @param monitorPojo
     */
    public void offlineOperatingCycle(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{}开始执行 离线工作循环", sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        ProjectCraneCyclicWorkDuration duration = JSONUtil.toBean(requestDTO.getData(), ProjectCraneCyclicWorkDuration.class);
        Date start = duration.getBeginTime();
        Date end = duration.getEndTime();
        // 实时更新统计缓存
        String cyclicWorkKey = GlobalStaticConstant.getStatisticsKey(xywgProerties.getRedisHead(), sn);
        ProjectCraneStatisticsDaily statisticsDaily;
        if (DateUtil.beginOfDay(new Date()).getTime() == DateUtil.beginOfDay(start).getTime()) {
            //报警数据属于当天的
            log.info("塔吊--设备{}处理 当天 离线报警数据", sn);
            if (!redisUtil.exists(cyclicWorkKey)) {
                ProjectInfo projectInfo = projectInfoService.selectCacheOne(crane.getProjectId());
                statisticsDaily = new ProjectCraneStatisticsDaily(projectInfo.getId(), projectInfo.getName(), projectInfo.getBuilder(), crane.getOwner(), crane.getIdentifier(), crane.getCraneNo(), sn);

            } else {
                statisticsDaily = JSONUtil.toBean((String) redisUtil.get(cyclicWorkKey), ProjectCraneStatisticsDaily.class);
            }

        } else {
            log.info("塔吊--设备{}处理 非当天 离线报警数据", sn);
            statisticsDaily = null;
        }
        // 查下这段时间内的报警数据
        Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
        alarmWrapper.between("create_time", start, end);
        //查询报警
        log.info("塔吊--设备{}查询报警数据", sn);
        List<ProjectCraneAlarm> alarms = projectCraneAlarmService.selectList(alarmWrapper);
        String alarmInfo;
        if (alarms != null && alarms.size() > 0) {
            log.info("塔吊--设备{}组装报警信息", sn);
            alarmInfo = getAlarmInfo(alarms);
        } else {
            log.info("塔吊--设备{}暂无报警信息", sn);
            alarmInfo = "无";
        }
        log.info("塔吊--设备{}更新吊重次数", sn);
        log.info("塔吊--设备{} 组装工作循环", sn);
        if (statisticsDaily != null) {
            statisticsDaily.setLiftFrequency(statisticsDaily.getLiftFrequency() + 1);
            statisticsDaily = ProjectCraneStatisticsDaily.addPercentage(duration.getMomentPercentage(), statisticsDaily);
            redisUtil.set(cyclicWorkKey, JSONUtil.toJsonStr(statisticsDaily));
            log.info("塔吊--设备{} 更新统计缓存(吊重次数+1，力矩百分比分类)", sn);
        } else {
            log.info("塔吊--设备{} 非当天数据", sn);
        }
        duration.setCraneId(crane.getId());
        duration.setMultipleRate(crane.getMultipleRate().doubleValue());
        duration.setAlarmInfo(alarmInfo);
//        duration.setBeginHeight(Double.parseDouble(crane.getCurrentHeight())-duration.getBeginHeight());
//        duration.setEndHeight(Double.parseDouble(crane.getCurrentHeight())-duration.getEndHeight());
        workDurationService.insert(duration);
        Long timesum = crane.getTimeSum();
        if (timesum == null) {
            crane.setTimeSum(end.getTime() - start.getTime());
        } else {
            crane.setTimeSum(timesum + end.getTime() - start.getTime());
        }
        projectCraneService.updateById(crane);
        projectCraneService.removeCache(sn);
        log.info("塔吊--设备{} 更新累计工作时间", sn);
        log.info("塔吊--设备{} 执行结束 离线工作循环", sn);
    }


    private String getAlarmInfo(List<ProjectCraneAlarm> alarms) {
        StringBuffer stringBuffer = new StringBuffer();
        int status[] = new int[16];
        for (ProjectCraneAlarm alarm : alarms) {
            status[alarm.getAlarmId() - 1] = 1;
        }
        for (int i = 0; i < 16; i++) {
            if (status[i] == 1) {
                switch ((i + 1)) {
                    case 1:
                        stringBuffer.append("起重量报警;");
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
                        break;
                }

            }
        }
        return stringBuffer.toString();
    }

    /**
     * 离线原因
     *
     * @param channel
     * @param monitorPojo
     */
    public void offlineReason(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{} 执行开始 累计工作时间", sn);
        offline(requestDTO);
        // 清除登录信息
        log.info("塔吊--设备{} 执行完毕 累计工作时间", sn);
        ResponseDTO responseDTO = new ResponseDTO(sn, requestDTO.getCmd(), 200, "");
        commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));

    }

    /**
     * 塔机基本信息设置上传服务器
     *
     * @param channel
     * @param monitorPojo 数据
     */
    public void basicInformation(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        ProjectCrane projectCrane = JSONUtil.toBean(requestDTO.getData(), ProjectCrane.class);
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
     * @param channel
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void amplitudeCorrect(RequestDTO requestDTO) {
        settingCommon(requestDTO);
        log.info("塔吊--设备{} 执行完毕 幅度校准信息设置", requestDTO.getSn());

    }

    private void settingCommon(RequestDTO requestDTO) {
        //序列号   01
        String sn = requestDTO.getSn();
        Integer projectId = projectCraneService.selectOne(sn).getProjectId();
        ProjectCraneCalibrationLog projectCraneCalibrationLog = JSONUtil.toBean(requestDTO.getData(), ProjectCraneCalibrationLog.class);
        projectCraneCalibrationLog.setProjectId(projectId);
        projectCraneCalibrationLogMapper.insert(projectCraneCalibrationLog);

    }

    /**
     * 塔机高度校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void heightCorrect(RequestDTO requestDTO) {
        settingCommon(requestDTO);
        log.info("塔吊--设备{} 执行完毕 高度校准信息设置", requestDTO.getSn());
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
    public void angleCorrect(RequestDTO requestDTO) {
        settingCommon(requestDTO);
        log.info("塔吊--设备{} 执行完毕 角度校准信息设置", requestDTO.getSn());
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
    public void elevatingCapacityCorrect(RequestDTO requestDTO) {
        settingCommon(requestDTO);
        log.info("塔吊--设备{} 执行完毕 起重量校准信息设置", requestDTO.getSn());
    }

    /**
     * 单机防碰撞区域设置上传服务器
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void antiCollisionCorrect(RequestDTO requestDTO) {
        //序列号   01
        String deviceNo = requestDTO.getSn();
        log.info("塔吊--设备{} 执行开始 单机防碰撞区域设置", deviceNo);
        Integer projectId = projectCraneService.selectOne(deviceNo).getProjectId();
        ProjectCraneSingleCollisionAvoidanceSet singleCollisionAvoidanceSet = JSONUtil.toBean(requestDTO.getData(), ProjectCraneSingleCollisionAvoidanceSet.class);
        singleCollisionAvoidanceSet.setProjectId(projectId);
        craneSingleAvoidanceSetMapper.insert(singleCollisionAvoidanceSet);
        log.info("塔吊--设备{} 执行完毕 单机防碰撞区域设置", deviceNo);
    }

    /**
     * 多机防碰撞
     *
     * @param channel
     * @param monitorPojo
     */

    public void mutiCollisionCorrect(RequestDTO requestDTO) {
        String sn = requestDTO.getSn();
        log.info("塔吊--设备{} 执行开始 多机防碰撞设置", sn);
        ProjectCrane crane = projectCraneService.selectOne(sn);
        ProjectCraneMutiCollisionAvoidanceSet craneMutiCollisionAvoidanceSet = JSONUtil.toBean(requestDTO.getData(), ProjectCraneMutiCollisionAvoidanceSet.class);
        mutiCollisionAvoidanceSetService.insert(craneMutiCollisionAvoidanceSet);
        log.info("塔吊--设备{} 执行完毕 多机防碰撞设置", sn);
    }

    /**
     * 处理报警数据
     *
     * @param projectCraneDetail 解析后的明细数据
     * @param alarmData          报警数据中的报警信息代码
     */
    public int sendAlarmData(ProjectCraneDetail projectCraneDetail, String alarmData, ProjectInfo projectInfo) {
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
                if ("1".equals(redisUtil.get(key))) {
                    //已经报过警了
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    //更新缓存
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));
                    log.info("塔吊--设备{} 重复报警，修改报警结束时间", sn);
                    continue;
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
                    Wrapper wrapper = new EntityWrapper();
                    wrapper.eq("create_time", projectCraneDetail.getCreateTime());
                    wrapper.eq("crane_id", projectCraneDetail.getCraneId());
                    wrapper.eq("alarm_id", alarmId);
                    alarm = projectCraneAlarmService.selectOne(wrapper);
                    //把最后一条保存起来
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));
                    alarmInfo.add(AlarmDTOFactory.factory(alarm, projectInfo, "塔吊设备"));
                    //推送到移动端
                    pushService.pushMob("注意", AlarmDTOFactory.factoryMessage(alarm, projectInfo, "塔吊设备"), projectInfo.getUuid());
                    continue;
                }

            } else {
                log.info("塔吊--设备{} 进入正常代码块", sn);
                if ("0".equals(redisUtil.get(key))) {
                    //不用操作
                    //已经报过警了
                    log.info("塔吊--设备{} 未报警", sn);
                    continue;
                } else {
                    //报警停止了
                    redisUtil.set(key, "0");
                    int alarmId = i + 1;
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    Wrapper wrapper = new EntityWrapper();
                    wrapper.eq("create_time", alarm.getCreateTime());
                    wrapper.eq("id", alarm.getId());
                    projectCraneAlarmService.update(alarm, wrapper);
                    redisUtil.remove(lastAlarmKey);
                    log.info("塔吊--设备{} 报警停止", sn);
                    continue;
                }
            }


        }
        // 更新缓存信息
        redisUtil.set(cyclicWorkKey, JSONUtil.toJsonStr(statisticsDaily));

        if (alarmInfo.size() > 0) {
            ResultDTO redisData = ResultDTO.factory(alarmInfo, 21);
            //放入缓存 报警信息推送到web端

            WSAlarmDTO detailDTO =new WSAlarmDTO();
            detailDTO.setResultDTO(redisData);;
            detailDTO.setSn(sn);
            detailDTO.setProjectId(projectInfo.getId()+"");
            detailDTO.setType("crane_alarm");
            detailDTO.setUuid(projectInfo.getUuid());
            ResponseDTO responseDTO =new ResponseDTO(sn,"0021",200,JSONUtil.toJsonStr(detailDTO));
            commonMethod.send2Mq(JSONUtil.toJsonStr(responseDTO));

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
            if (((Long.parseLong(alarmData, 16)) & (0x00000001 << i)) >> i == 1 && redisUtil.get(key).equals("1")) {
                log.info("塔吊--设备{}处理 非当天 离线报警数据", sn);
                if (redisUtil.get(key).equals("1")) {
                    //已经报过警了
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    //更新缓存
                    redisUtil.set(lastAlarmKey, JSONUtil.toJsonStr(alarm));
                    continue;
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

                    continue;
                }

            } else {
                log.info("塔吊--设备{} 进入正常代码块", sn);
                if (redisUtil.get(key).equals("0")) {
                    //不用操作
                    //已经报过警了
                    log.info("塔吊--设备{} 未报警", sn);
                    continue;
                } else {
                    //报警停止了
                    redisUtil.set(key, "0");
                    int alarmId = i + 1;
                    ProjectCraneAlarm alarm = JSONUtil.toBean((String) redisUtil.get(lastAlarmKey), ProjectCraneAlarm.class);
                    alarm.setEndTime(projectCraneDetail.getDeviceTime());
                    Wrapper wrapper = new EntityWrapper();
                    wrapper.eq("create_time", alarm.getCreateTime());
                    wrapper.eq("id", alarm.getId());
                    projectCraneAlarmService.update(alarm, wrapper);
                    redisUtil.remove(lastAlarmKey);

                    log.info("塔吊--设备{} 报警停止", sn);
                    continue;
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

    public void beginExcecution()
    {
        Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
        wrapper.eq("is_online",1);
        wrapper.eq("is_del",0);
        List<ProjectCrane> onlineCraneList =  projectCraneService.selectList(wrapper);
        onlineCraneList.forEach(item->{
            RequestDTO req=new RequestDTO();
            req.setSn(item.getDeviceNo());
            req.setData(GlobalStaticConstant.OFFLINE_REASON_SERVER_REBOOT);
            craneBusinessLogicService.offline(req);
        });
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