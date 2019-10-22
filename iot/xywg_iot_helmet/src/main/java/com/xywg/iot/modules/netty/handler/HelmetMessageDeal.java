package com.xywg.iot.modules.netty.handler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.DataAnalyUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.core.annotion.SocketCommand;
import com.xywg.iot.modules.helmet.model.*;
import com.xywg.iot.modules.helmet.service.*;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.xywg.iot.common.global.GlobalStaticConstant.XYWG_IOT_HELMET;

/**
 * @author hjy
 * @date 2018/11/20
 * 具体的消息处理逻辑
 */
@Service
public class HelmetMessageDeal {
    private Logger logger = LoggerFactory.getLogger(HelmetMessageDeal.class);
    @Autowired
    private ProjectHelmetService projectHelmetService;
    @Autowired
    private ProjectHelmetHealthDetailService projectHelmetHealthDetailService;
    @Autowired
    private ProjectHelmetPositionDetailService projectHelmetPositionDetailService;
    @Autowired
    private ProjectHelmetAlarmService projectHelmetAlarmService;
    @Autowired
    private ProjectHelmetHeartbeatService projectHelmetHeartbeatService;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 设备下线
     *
     * @param deviceWorker
     */
    @Transactional(rollbackFor = Exception.class)
    @SocketCommand(command = GlobalStaticConstant.TERMINAL_DOWN)
    public void terminalDown(DeviceWorker deviceWorker) {
        logger.info("Terminal Down Complete: " + deviceWorker.getImei());
        ProjectHelmet helmet = new ProjectHelmet();
        helmet.setId(deviceWorker.getHelmetId());
        helmet.setIsOnline(GlobalStaticConstant.DEVICE_STATE_OFF_LINE);
        helmet.setCreateTime(new Date());
        projectHelmetService.updateById(helmet);
        redisUtil.remove(XYWG_IOT_HELMET + deviceWorker.getImei());

        //更新在线的结束时间
        updateEndHelmetHeartbeatDate(1, deviceWorker);
        //添加离线的开始时间
        insertStartHelmetHeartbeatDate(0, deviceWorker);

    }

    /**
     * 登录
     *
     * @param ctx
     * @param dataDomain
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @SocketCommand(command = GlobalStaticConstant.LOGIN)
    public void login(DataDomain dataDomain, ChannelHandlerContext ctx) {
        Wrapper<ProjectHelmet> wrapper = new EntityWrapper<>();
        wrapper.eq("imei", dataDomain.getImei());
        wrapper.eq("status", 1);
        wrapper.eq("current_flag", 0);
        wrapper.eq("is_del", 0);
        //查询安全帽是否存在
        ProjectHelmet projectHelmet = projectHelmetService.selectOne(wrapper);
        if (projectHelmet == null) {
            logger.warn("Error Data --- Device: " + dataDomain.getImei() + " No exist Or Not Enabled!");
            //回复登录失败
            ctx.writeAndFlush(Unpooled.wrappedBuffer(DataAnalyUtil.responseLogin(false)));
            ctx.close();
            return;
        }
        DeviceWorker deviceWorker = new DeviceWorker();
        deviceWorker.setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        deviceWorker.setImei(dataDomain.getImei());
        deviceWorker.setHelmetId(projectHelmet.getId());
        //连接到全局连接管理
        NettyChannelManage.CHANNEL_MAP.put(dataDomain.getImei(), ctx.channel());
        ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceWorker);

        ProjectHelmet helmet = new ProjectHelmet();
        helmet.setId(projectHelmet.getId());
        //helmet.setImei(projectHelmet.getImei());
        helmet.setIsOnline(GlobalStaticConstant.DEVICE_STATE_ON_LINE);
        helmet.setModifyTime(new Date());
        projectHelmetService.updateById(helmet);

        //登录成功后 在心跳表中添加心跳开始時間,只有在设备下线的时候才往心跳表中修改本次结束时间
        handleHeartbeat(deviceWorker);

        //登录成功后将设备基本信息放入到redis
        projectHelmet.setIsOnline(1);
        redisUtil.set(XYWG_IOT_HELMET + projectHelmet.getImei(), projectHelmet);

        //回复登录成功
        ctx.writeAndFlush(Unpooled.wrappedBuffer(DataAnalyUtil.responseLogin(true)));
    }



    /**
     * 室内定位数据
     *
     * @param dataDomain
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @SocketCommand(command = GlobalStaticConstant.INDOOR_POSITION)
    public void indoorPosition(DataDomain dataDomain, ChannelHandlerContext ctx) {

    }




    /**
     * 定位健康数据
     *
     * @param dataDomain
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @SocketCommand(command = GlobalStaticConstant.POSITION_HEALTH)
    public void positionHealth(DataDomain dataDomain, ChannelHandlerContext ctx) {
        DeviceWorker deviceWorker = NettyChannelManage.getDeviceWorkerByChannel(ctx.channel());
        //功能码30  中包含了 07(定位)   和0f(健康)
        if (dataDomain.getPositionHealthCommand() == GlobalStaticConstant.POSITION) {
            //定位
            dealPosition(dataDomain, deviceWorker);
        } else if (dataDomain.getPositionHealthCommand() == GlobalStaticConstant.HEALTH) {
            //健康
            dealHealth(dataDomain, deviceWorker);
            //新增的室内定位
        } else if (dataDomain.getPositionHealthCommand() == GlobalStaticConstant.INDOOR_POSITION){
            dealPosition(dataDomain, deviceWorker);
        }
    }

    /**
     * 定位数据
     *
     * @param dataDomain
     */

    private void dealPosition(DataDomain dataDomain, DeviceWorker deviceWorker) {
        ProjectHelmetPositionDetail projectHelmetPositionDetail = new ProjectHelmetPositionDetail();
        BeanUtils.copyProperties(dataDomain, projectHelmetPositionDetail);
        projectHelmetPositionDetail.setHelmetId(deviceWorker.getHelmetId());
        projectHelmetPositionDetailService.insertByMonth("t_project_helmet_position_detail_" + getYearMonthStr(), projectHelmetPositionDetail);
    }

    /**
     * 健康数据
     *
     * @param dataDomain
     */
    private void dealHealth(DataDomain dataDomain, DeviceWorker deviceWorker) {

        ProjectHelmetHealthDetail projectHelmetHealthDetail = new ProjectHelmetHealthDetail();
        BeanUtils.copyProperties(dataDomain, projectHelmetHealthDetail);
        projectHelmetHealthDetail.setHelmetId(deviceWorker.getHelmetId());
        String tableName = "t_project_helmet_health_detail_" + getYearMonthStr();
        projectHelmetHealthDetail.setTableName(tableName);
        projectHelmetHealthDetailService.insertByMonth(projectHelmetHealthDetail);

        //判断健康状况 健康报警
        List<ProjectHelmetAlarm> alarmList = healthStatus(projectHelmetHealthDetail);
        if (alarmList.size() > 0) {
            projectHelmetAlarmService.insertBatchByMonth("t_project_helmet_alarm_" + getYearMonthStr(), alarmList);
        }
    }


    /**
     * 计算健康状态
     *
     * @param healthDetail
     * @return
     */
    private List<ProjectHelmetAlarm> healthStatus(ProjectHelmetHealthDetail healthDetail) {
        List<ProjectHelmetAlarm> list = new ArrayList<>();
        ProjectHelmetAlarm projectHelmetAlarm = new ProjectHelmetAlarm();
        projectHelmetAlarm.setHandleStatus(0);
        projectHelmetAlarm.setDetailId(healthDetail.getId());
        projectHelmetAlarm.setHelmetId(healthDetail.getHelmetId());
        //判断是否是人物跌倒数据
        if (GlobalStaticConstant.DATA_HEALTH_SIXAXIS_TUMBLE.equals(healthDetail.getSixAxis())) {
            ProjectHelmetAlarm helmetAlarm = new ProjectHelmetAlarm();
            BeanUtils.copyProperties(projectHelmetAlarm, helmetAlarm);
            helmetAlarm.setAlarmType(1);
            helmetAlarm.setAlarmInfo("跌倒报警");
            list.add(helmetAlarm);
        }
        //判断心率
        /*if (healthDetail.getHeartRate() > GlobalStaticConstant.HEART_RATE_MAX_VALUE) {
            projectHelmetAlarm.setAlarmType(2);
            projectHelmetAlarm.setAlarmInfo("心率过高报警,当前心率:"+healthDetail.getHeartRate());
            list.add(projectHelmetAlarm);
        }
        if(healthDetail.getHeartRate()<GlobalStaticConstant.HEART_RATE_MIN_VALUE){
            projectHelmetAlarm.setAlarmType(2);
            projectHelmetAlarm.setAlarmInfo("心率过低报警,当前心率:"+healthDetail.getHeartRate());
            list.add(projectHelmetAlarm);
        }*/

        if (healthDetail.getBloodOxygen() < GlobalStaticConstant.HEART_RATE_BLOOD_OXYGEN) {
            ProjectHelmetAlarm helmetAlarm = new ProjectHelmetAlarm();
            BeanUtils.copyProperties(projectHelmetAlarm, helmetAlarm);
            helmetAlarm.setAlarmType(3);
            helmetAlarm.setAlarmInfo("动脉血氧饱和度较低,当前含量:" + healthDetail.getBloodOxygen() + "%");
            list.add(helmetAlarm);
        }
        if (healthDetail.getTemperature() < GlobalStaticConstant.HEART_RATE_TEMPERATURE_MIN) {
            ProjectHelmetAlarm helmetAlarm = new ProjectHelmetAlarm();
            BeanUtils.copyProperties(projectHelmetAlarm, helmetAlarm);
            helmetAlarm.setAlarmType(4);
            helmetAlarm.setAlarmInfo("体温较低,当前体温:" + healthDetail.getTemperature());
            list.add(helmetAlarm);
        }
        if (healthDetail.getTemperature() > GlobalStaticConstant.HEART_RATE_TEMPERATURE_MAX) {
            ProjectHelmetAlarm helmetAlarm = new ProjectHelmetAlarm();
            BeanUtils.copyProperties(projectHelmetAlarm, helmetAlarm);
            helmetAlarm.setAlarmType(4);
            helmetAlarm.setAlarmInfo("体温较高,当前体温:" + healthDetail.getTemperature());
            list.add(helmetAlarm);
        }
        return list;
    }

    private void handleHeartbeat(DeviceWorker deviceWorker) {
        Wrapper<ProjectHelmetHeartbeat> wrapper = new EntityWrapper<>();
        wrapper.eq("imei", deviceWorker.getImei());
        wrapper.eq("helmet_id", deviceWorker.getHelmetId());

        List<ProjectHelmetHeartbeat> projectHelmetHeartbeat = projectHelmetHeartbeatService.selectList(wrapper);
        //
        if (projectHelmetHeartbeat.size() > 0) {
            //更新离线的结束时间,
            updateEndHelmetHeartbeatDate(0, deviceWorker);
            //添加在线开始数据
            insertStartHelmetHeartbeatDate(1, deviceWorker);
        } else {
            ProjectHelmetHeartbeat toDbHelmetHeartbeat = new ProjectHelmetHeartbeat();
            toDbHelmetHeartbeat.setImei(deviceWorker.getImei());
            toDbHelmetHeartbeat.setHelmetId(deviceWorker.getHelmetId());
            toDbHelmetHeartbeat.setCreateTime(new Date());
            toDbHelmetHeartbeat.setEndTime(new Date());
            toDbHelmetHeartbeat.setStatus(1);
            projectHelmetHeartbeatService.insert(toDbHelmetHeartbeat);
        }

    }


    public String getYearMonthStr() {
        return DateFormatUtils.format(new Date(), "yyyyMM");
    }


    /**
     * 心跳表添加开始数据
     *
     * @param status
     * @param deviceWorker
     */
    public void insertStartHelmetHeartbeatDate(Integer status, DeviceWorker deviceWorker) {
        ProjectHelmetHeartbeat toDbHelmetHeartbeat = new ProjectHelmetHeartbeat();
        toDbHelmetHeartbeat.setImei(deviceWorker.getImei());
        toDbHelmetHeartbeat.setHelmetId(deviceWorker.getHelmetId());
        toDbHelmetHeartbeat.setCreateTime(new Date());
        toDbHelmetHeartbeat.setStatus(status);
        projectHelmetHeartbeatService.insert(toDbHelmetHeartbeat);
    }


    /**
     * 心跳表更新结束时间
     *
     * @param status
     * @param deviceWorker
     */
    public void updateEndHelmetHeartbeatDate(Integer status, DeviceWorker deviceWorker) {
        ProjectHelmetHeartbeat helmetHeartbeat = new ProjectHelmetHeartbeat();
        helmetHeartbeat.setEndTime(new Date());
        Wrapper<ProjectHelmetHeartbeat> wrapperUpdate = new EntityWrapper<>();
        wrapperUpdate.eq("imei", deviceWorker.getImei());
        wrapperUpdate.eq("helmet_id", deviceWorker.getHelmetId());
        wrapperUpdate.eq("status", status);
        wrapperUpdate.isNull("end_time");
        projectHelmetHeartbeatService.update(helmetHeartbeat, wrapperUpdate);
    }


}
