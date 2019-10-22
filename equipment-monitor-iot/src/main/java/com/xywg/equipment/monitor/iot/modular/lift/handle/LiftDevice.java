package com.xywg.equipment.monitor.iot.modular.lift.handle;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.core.util.OriDataUtil;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.handler.BaseDevice;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.lift.dao.ProjectLiftAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.lift.dao.ProjectLiftDetailMapper;
import com.xywg.equipment.monitor.iot.modular.lift.dto.CurrentLiftData;
import com.xywg.equipment.monitor.iot.modular.lift.factory.LiftAlarmFactory;
import com.xywg.equipment.monitor.iot.modular.lift.model.*;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftHeartbeatService;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftOriginalDataService;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftService;
import com.xywg.equipment.monitor.iot.modular.lift.service.impl.ProjectLiftServiceImpl;
import com.xywg.equipment.monitor.iot.netty.aop.ZbusProducerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author wangyifei
 */
@Component
public class LiftDevice extends BaseDevice {
    @Autowired
    ProjectLiftDetailMapper detailMapper;
    @Autowired
    IProjectLiftService projectLiftService;
    @Autowired
    ProjectLiftAlarmMapper projectLiftAlarmMapper;
    @Autowired
    IProjectLiftOriginalDataService projectLiftOriginalDataService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IProjectLiftHeartbeatService projectLiftHeartbeatService;
    @Autowired
    IProjectInfoService projectInfoService;


    @Override
    public boolean isHeartbeat(String data) {

        if (data.startsWith("sjjht:")) {
            //原理 开机前的第一条数据肯定是关机
            //判断缓存中是否 记录
            //没记录说明是开机
            //有记录重新设置缓存
            ProjectLiftHeartbeat heartbeat = new ProjectLiftHeartbeat();
            heartbeat.setDeviceNo(data.split(":")[1]);
            /*
            Wrapper<ProjectLift> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no", data.split(":")[1]);*/
            ProjectLift lift = projectLiftService.selectOne(heartbeat.getDeviceNo());
            if (lift == null) {
                throw new RuntimeException("数据库中没有" + heartbeat.getDeviceNo() + "该设备");
            }
            if(0==lift.getStatus()){
                throw new RuntimeException("" + heartbeat.getDeviceNo() + "该设备未启用");
            }
            heartbeat.setLiftId(lift.getId());
            heartbeat.setCreateTime(new Date());
            if (!redisUtil.exists(this.xywgProerties.getRedisHead() + ":" + "head:" + data)) {
                // 缓存中不存在 说明是开机
                // 添加一条不受控数据
                //添加一条受控数据
                projectLiftHeartbeatService.doOpenBusiness(heartbeat);
                //1.修改数据库 online 字段
                ProjectLift updlift = new ProjectLift();
                updlift.setId(lift.getId());
                updlift.setIsOnline(1);
                projectLiftService.updateById(updlift);
                //删除 设备信息缓存
                String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+heartbeat.getDeviceNo();
                redisUtil.remove(deviceKey);


            } else {
                projectLiftHeartbeatService.updateEndTime(heartbeat);
            }
            redisUtil.setSec(this.xywgProerties.getRedisHead() + ":" + "head:" + data, 1, 46L);

            return true;
        }

        return false;
    }

    @Override
    public void insertOrigin(String data) {
        OriDataUtil.SJJ_LOGGER.info(data);
        /*
        ProjectLiftOriginalData originalData = new ProjectLiftOriginalData();
        originalData.setOriginalData(data);
        originalData.setDeviceTime(new Date());
        projectLiftOriginalDataService.insert(originalData);*/
    }

    @Override
    public void offline(String data) {
        String deviceNo =  data.split(":")[1];

        ProjectLift lift =  projectLiftService.selectOne(deviceNo);
        if(lift == null){
            throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
        }
        if(0==lift.getStatus()){
            throw new RuntimeException("" + deviceNo + "该设备未启用");
        }

        //1.修改数据库 online 字段
        ProjectLift updLift = new ProjectLift();
        updLift.setId(lift.getId());
        updLift.setIsOnline(0);
        projectLiftService.updateById(updLift);
        //删除 设备信息缓存
        String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        redisUtil.remove(deviceKey);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void doBusiness(String data, ZbusProducerHolder producerHolder, MasterProtocolConfigDTO config) {
        // 模拟配置

        ProjectLiftDataModel dataModel = new ProjectLiftDataModel();
        dataModel.setDeviceNo("dltysjj");
        dataModel.setWeight("a");
        dataModel.setHeight("b");
        dataModel.setSpeed("c");
        dataModel.setFrontDoorStatus("d");
        dataModel.setBackDoorStatus("e");
        dataModel.setStatus("f");
        dataModel.setKey1("g");
        dataModel.setKey2("h");
        dataModel.setKey3("i");
        dataModel.setKey4("j");
        dataModel.setKey5("k");
        dataModel.setKey6("l");
        dataModel.setKey7("m");
        //  去除  :, 和 :结尾的数据
        data = this.removeBlank(data);

        data = this.replaceHead(data, dataModel.getDeviceNo(), "deviceNo");
        data = this.replaceKey(data, dataModel.getWeight(), "weight");
        data = this.replaceKey(data, dataModel.getHeight(), "height");
        data = this.replaceKey(data, dataModel.getSpeed(), "speed");
        data = this.replaceKey(data, dataModel.getFrontDoorStatus(), "frontDoorStatus");
        data = this.replaceKey(data, dataModel.getBackDoorStatus(), "backDoorStatus");
        data = this.replaceKey(data, dataModel.getStatus(), "status");
        data = this.replaceKey(data, dataModel.getKey1(), "key1");
        data = this.replaceKey(data, dataModel.getKey2(), "key2");
        data = this.replaceKey(data, dataModel.getKey3(), "key3");
        data = this.replaceKey(data, dataModel.getKey4(), "key4");
        data = this.replaceKey(data, dataModel.getKey5(), "key5");
        data = this.replaceKey(data, dataModel.getKey6(), "key6");
        data = this.replaceKey(data, dataModel.getKey7(), "key7");

        String jsonStr = "{" + data + "}";
        if (!JSONUtil.isJson(jsonStr)) {
            throw new RuntimeException("数据格式异常");
        }
        System.out.println("升降机数据 :  " + jsonStr);
        ProjectLiftDetail detail = JSONUtil.toBean(jsonStr, ProjectLiftDetail.class);
        /*
        Wrapper<ProjectLift> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no", detail.getDeviceNo());*/
        ProjectLift lift = projectLiftService.selectOne(detail.getDeviceNo());

        if (lift == null) {
            // 压根没有这个设备
            throw new RuntimeException("数据库中没有" + detail.getDeviceNo() + "该设备");
        }
        if(0==lift.getStatus()){
            throw new RuntimeException("" + detail.getDeviceNo() + "该设备未启用");
        }
        ProjectInfo projectInfo = projectInfoService.selectById(lift.getProjectId());
        if (projectInfo == null) {
            throw new RuntimeException("设备" + detail.getDeviceNo() + "不在任何项目下");
        }

        detail.setLiftId(lift.getId());
        detailMapper.createDetail(detail, BaseFactory.getTableName(ProjectLiftDetail.class));

        CurrentLiftData  currentLiftData =  CurrentLiftData.factory(detail,lift);

        //放入缓存
        this.push(redisUtil, lift.getDeviceNo(), "lift", projectInfo.getUuid(), currentLiftData,""+lift.getProjectId());

        //比对 阈值

        List<ProjectLiftAlarm> alarms = LiftAlarmFactory.factory(
                lift, detail
        );
        // 项目信息


        List<AlarmDTO> alarmInfo = new ArrayList<>();


        for (ProjectLiftAlarm alarm : alarms) {
            //添加 库
            projectLiftAlarmMapper.createAlarm(alarm, BaseFactory.getTableName(ProjectLiftAlarm.class));
            //加入list
            alarmInfo.add(AlarmDTOFactory.factory(alarm, projectInfo, "升降机设备"));
            pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"升降机设备"),projectInfo.getUuid());

        }
        if(alarmInfo.size()>0) {
            ResultDTO redisData = ResultDTO.factory(alarmInfo, 22);
            //放入缓存
            this.push(redisUtil, lift.getDeviceNo(), "lift_alarm", projectInfo.getUuid(), redisData,""+lift.getProjectId());
        }
    }

}
