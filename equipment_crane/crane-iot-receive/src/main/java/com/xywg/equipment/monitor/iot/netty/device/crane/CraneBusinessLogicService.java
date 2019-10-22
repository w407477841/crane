package com.xywg.equipment.monitor.iot.netty.device.crane;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.config.rabbitmq.RabbitConfig;
import com.xywg.equipment.monitor.iot.core.util.DateUtils;
import com.xingyun.crane.cache.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneCalibrationLogMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneDetailMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneSingleCollisionAvoidanceSetMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dto.CurrentCraneData;
import com.xywg.equipment.monitor.iot.modular.crane.model.*;
import com.xywg.equipment.monitor.iot.modular.crane.service.*;
import com.xywg.equipment.monitor.iot.modular.romote.handle.RemoteSetupService;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.dto.RequestDTO;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.NettyChannelManage;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.beetl.ext.simulate.JsonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private RabbitTemplate rabbitTemplate;

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
    /**
     * 登录 只包含(sn,firmware)
     * 修改在线状态
     * 修改版本号
     * 设备切换项目前，先禁用原项目下设备，新项目安装完毕后，在新项目下添加设备并启用
     *
     * @param monitorPojo 数据
     */
    public void login(Channel channel, CompleteDataPojo monitorPojo) {

        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 登录", sn);
        if (NettyChannelManage.CHANNEL_MAP.containsKey(sn)) {
            log.error("塔吊--之前的链接未失效({}，提前断开)", sn);
            NettyChannelManage.CHANNEL_MAP.get(sn).close();
            throw  new RuntimeException("未失效,主动断开");
        }
        NettyChannelManage.CHANNEL_MAP.put(sn, channel);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        DeviceConnectInfo deviceConnectInfo = new DeviceConnectInfo(uuid, sn);
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceConnectInfo);
        log.info("塔吊--设备{}绑定通道{}", sn, uuid);
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), monitorPojo.getDataMap().get("02"));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void offline(Channel channel, String sn, String reason) {
        log.info("塔吊--设备{} 离线", sn);
        RequestDTO requestDTO = new RequestDTO(sn, "0014",reason);
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
    }

    /**
     * 下线执行逻辑
     *
     * @param sn
     * @param reason
     */
    public void offline(String sn, String reason) {
        this.offline(null, sn, reason);
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
    public void exceptionalLog(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        String exceptionMessageType = monitorPojo.getDataMap().get("02").replaceAll(" ", "");
        String exception = new StringBuffer(StrUtil.fillBefore(Integer.toBinaryString(Integer.parseInt(exceptionMessageType, 16)), '0', 8)).reverse().toString();
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), exception);
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorData(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 监控数据", sn);
        log.info("塔吊--设备{}回复", sn);
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        log.info("塔吊--设备{}组装数据", sn);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal dgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal height=craneHeight.subtract(dgHeight).setScale(2,RoundingMode.HALF_UP);
        System.out.println(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16));
        ProjectCraneDetail projectCraneDetail = new ProjectCraneDetail(
                null,
                sn,
                null,
                DateUtils.parseDate(monitorPojo.getDataMap().get("0E")),
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
                craneHeight,
                Double.parseDouble(dgHeight.toString())
                //Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16) / 10d
        );
        projectCraneDetail.setAlarmInfo(monitorPojo.getDataMap().get("0C"));
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), JSONUtil.toJsonStr(projectCraneDetail));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
    }

    /**
     * 上传离线监控数据
     * 1.新增监控数据
     * 2.新增报警数据
     * 3.修改设备报警状态
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadOfflineMonitorData(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 离线监控数据", sn);
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal dgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal height=craneHeight.subtract(dgHeight).setScale(2,RoundingMode.HALF_UP);
        ProjectCraneDetail projectCraneDetail = new ProjectCraneDetail(
                null,
                sn,
                null,
                DateUtils.parseDate(monitorPojo.getDataMap().get("0E")),
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
                craneHeight,
                Double.parseDouble(dgHeight.toString())
                //Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16) / 10d
        );
        projectCraneDetail.setAlarmInfo(monitorPojo.getDataMap().get("0C"));
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), JSONUtil.toJsonStr(projectCraneDetail));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void operatingCycle(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 工作循环", sn);
        Date start = DateUtils.parseDate(monitorPojo.getDataMap().get("0D"));
        Date end = DateUtils.parseDate(monitorPojo.getDataMap().get("0E"));

        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0F"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal begindgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal enddgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("03"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);

        BigDecimal beginheight=craneHeight.subtract(begindgHeight).setScale(2,RoundingMode.HALF_UP);
        BigDecimal endheight=craneHeight.subtract(enddgHeight).setScale(2,RoundingMode.HALF_UP);
        ProjectCraneCyclicWorkDuration workDuration = new ProjectCraneCyclicWorkDuration(
                null,
                sn,
                Double.parseDouble(beginheight.toString()),
                Double.parseDouble(endheight.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("06"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0A"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0B"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0C"), 16) / 10d,
                null,
                null,
                start,
                end,
                start,
                Double.parseDouble(begindgHeight.toString()),
                Double.parseDouble(enddgHeight.toString())
        );
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), JSONUtil.toJsonStr(workDuration));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
        log.info("塔吊--设备{}执行完毕 工作循环", sn);
    }

    /**
     * 离线工作循环
     *
     * @param channel
     * @param monitorPojo
     */
    public void offlineOperatingCycle(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 离线工作循环", sn);
        Date start = DateUtils.parseDate(monitorPojo.getDataMap().get("0D"));
        Date end = DateUtils.parseDate(monitorPojo.getDataMap().get("0E"));
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0F"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal begindgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal enddgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("03"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);

        BigDecimal beginheight=craneHeight.subtract(begindgHeight).setScale(2,RoundingMode.HALF_UP);
        BigDecimal endheight=craneHeight.subtract(enddgHeight).setScale(2,RoundingMode.HALF_UP);
        ProjectCraneCyclicWorkDuration workDuration = new ProjectCraneCyclicWorkDuration(
                null,
                sn,
                Double.parseDouble(beginheight.toString()),
                Double.parseDouble(endheight.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("05"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("06"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0A"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0B"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("0C"), 16) / 10d,
                null,
                null,
                start,
                end,
                start,
                Double.parseDouble(begindgHeight.toString()),
                Double.parseDouble(enddgHeight.toString())
        );
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), JSONUtil.toJsonStr(workDuration));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void offlineReason(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        String sn = decode(monitorPojo.getDataMap().get("01"));
        String reason = monitorPojo.getDataMap().get("03");
        if( Integer.parseInt(reason,16)==0){
            offline(channel,sn,GlobalStaticConstant.OFFLINE_REASON_NORMAL);
        }else{
            offline(channel,sn,GlobalStaticConstant.OFFLINE_REASON_NOPOWER);
        }
         // 清除登录信息
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(null);
        NettyChannelManage.CHANNEL_MAP.remove(deviceConnectInfo.getSn());
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
    }

    /**
     * 塔机基本信息设置上传服务器
     *
     * @param channel
     * @param monitorPojo 数据
     */
    public void basicInformation(Channel channel, CompleteDataPojo monitorPojo) {
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 基本信息设置", sn);
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        ProjectCrane projectCrane = new ProjectCrane();
        //设备编号
        projectCrane.setDeviceNo(sn);
        //塔基编号
        String craneCode = monitorPojo.getDataMap().get("02");
        projectCrane.setCraneNo(Integer.parseInt(craneCode, 16) + "");
        //倍率
        String multiple = monitorPojo.getDataMap().get("03");
        projectCrane.setMultipleRate(Integer.parseInt(multiple, 16));
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
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), JSONUtil.toJsonStr(projectCrane));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void amplitudeCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 幅度校准信息设置", sn);
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
        Integer amplitudeCalibrationStatus = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("幅度斜率:").append(amplitudeSlope).append(",");
        sBuilder.append("幅度偏移:").append(amplitudeDeviation).append(",");
        sBuilder.append("幅度量程上限:").append(amplitudeRangeMax).append(",");
        sBuilder.append("幅度量程下限:").append(amplitudeRangeMin).append(",");
        sBuilder.append("幅度信号上限:").append(amplitudeSignalMax).append(",");
        sBuilder.append("幅度信号下限:").append(amplitudeSignalMin).append(",");
        sBuilder.append("幅度校准设置状态:").append(amplitudeCalibrationStatus == 0 ? "未设置" : "已设置").append(",");
        ProjectCraneCalibrationLog projectCraneCalibrationLog = new ProjectCraneCalibrationLog(
                sn, null, command, sBuilder.toString(), 0, new Date()
        );
        RequestDTO requestDTO = new RequestDTO(sn, command, JSONUtil.toJsonStr(projectCraneCalibrationLog));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void heightCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
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
                deviceNo, null, command, sBuilder.toString(), 0, new Date()
        );
        RequestDTO requestDTO = new RequestDTO(deviceNo, command, JSONUtil.toJsonStr(projectCraneCalibrationLog));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void angleCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
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
                deviceNo, null, command, sBuilder.toString(), 0, new Date()
        );
        RequestDTO requestDTO = new RequestDTO(deviceNo, command, JSONUtil.toJsonStr(projectCraneCalibrationLog));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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
    public void elevatingCapacityCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{} 执行开始 起重量校准信息设置", deviceNo);
        Integer projectId = projectCraneService.selectOne(deviceNo).getProjectId();
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
        Integer elevatingCapacityCalibrationStatus = Integer.parseInt(monitorPojo.getDataMap().get("08"), 16);
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("重量斜率:").append(elevatingCapacitySlope).append(",");
        sBuilder.append("重量偏移:").append(elevatingCapacityOffset).append(",");
        sBuilder.append("重量量程上限:").append(elevatingCapacityRangeMax).append(",");
        sBuilder.append("重量量程下限:").append(elevatingCapacityRangeMin).append(",");
        sBuilder.append("重量信号上限:").append(elevatingCapacitySignalMax).append(",");
        sBuilder.append("重量信号下限:").append(elevatingCapacitySignalMin).append(",");
        sBuilder.append("重量校准设置状态:").append(elevatingCapacityCalibrationStatus == 0 ? "未设置" : "已设置").append(",");

        ProjectCraneCalibrationLog projectCraneCalibrationLog = new ProjectCraneCalibrationLog(
                deviceNo, null, command, sBuilder.toString(), 0, new Date()
        );
        RequestDTO requestDTO = new RequestDTO(deviceNo, command, JSONUtil.toJsonStr(projectCraneCalibrationLog));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
    }

    /**
     * 单机防碰撞区域设置上传服务器
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     * @param projectId   项目id
     */
    public void antiCollisionCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        //序列号   01
        String deviceNo = decode(monitorPojo.getDataMap().get("01"));
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
                deviceNo, null,
                new BigDecimal(startAngle),
                new BigDecimal(endAngle),
                new BigDecimal(startHeight),
                new BigDecimal(endHeight),
                new BigDecimal(startRange),
                new BigDecimal(endRange),
                status, pageRange, 0, new Date()
        );
        RequestDTO requestDTO = new RequestDTO(deviceNo, monitorPojo.getCommand(), JSONUtil.toJsonStr(singleCollisionAvoidanceSet));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
    }

    /**
     * 多机防碰撞
     *
     * @param channel
     * @param monitorPojo
     */

    public void mutiCollisionCorrect(Channel channel, CompleteDataPojo monitorPojo) {
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
        RequestDTO requestDTO = new RequestDTO(sn, monitorPojo.getCommand(), JSONUtil.toJsonStr(mutiCollisionAvoidanceSetService));
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
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