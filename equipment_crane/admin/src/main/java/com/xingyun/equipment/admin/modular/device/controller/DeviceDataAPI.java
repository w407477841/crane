package com.xingyun.equipment.admin.modular.device.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dto.CraneDetailDTO;
import com.xingyun.equipment.admin.modular.device.dto.LiftDetailDTO;
import com.xingyun.equipment.admin.modular.device.dto.MonitorDetailDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitorDetail;
import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.device.service.IProjectLiftService;
import com.xingyun.equipment.admin.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.admin.modular.device.service.ProjectEnvironmentMonitorService;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:11 2019/1/21
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/device")
public class DeviceDataAPI {
    @Autowired
    ZbusProducerHolder  zbusProducerHolder ;
    @Autowired
    IProjectInfoService   projectInfoService;
    @Autowired
    ProjectEnvironmentMonitorService   projectEnvironmentMonitorService;
    @Autowired
    ProjectCraneService projectCraneService;
    @Autowired
    IProjectLiftService  projectLiftService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataAPI.class);
    @PostMapping("monitor/insertData")
        public ResultDTO insertMonitorData(@RequestParam("uuid") String uuid, @RequestBody MonitorDetailDTO monitor){

        try {
            Date date = monitor.getTime();
            Wrapper wrapper = new EntityWrapper();
            wrapper.eq("is_del",0);
            wrapper.eq("uuid",uuid);
            ProjectInfo projectInfo = projectInfoService.selectOne(wrapper);
            if(projectInfo ==null){
                return new ResultDTO(false,null,"UUID 不存在");
            }

            Wrapper monitorWrapper =  new EntityWrapper();
            monitorWrapper.eq("is_del",0);
            monitorWrapper.eq("project_id",projectInfo.getId());
            monitorWrapper.eq("device_no",monitor.getDeviceNo());
            ProjectEnvironmentMonitor device  = projectEnvironmentMonitorService.selectOne(monitorWrapper);
            if(device == null){
                return new ResultDTO(false,null,"设备不存在");
            }
            if(System.currentTimeMillis()-date.getTime()>180*1000){
                //离线
                if(1==device.getIsOnline()){
                    device.setIsOnline(0);
                    projectEnvironmentMonitorService.updateById(device);
                    String jsondata  = "lx:"+monitor.getDeviceNo();

                    zbusProducerHolder.sendYcLxMessage(jsondata);
                }
            }else{
                //在线
                if(0==device.getIsOnline()){
                    device.setIsOnline(1);
                    projectEnvironmentMonitorService.updateById(device);
                    String jsondata  = "{\"deviceNo\":\""+monitor.getDeviceNo()+"\",\"deviceTime\":"+System.currentTimeMillis()+"}";
                    zbusProducerHolder.sendYcHtMessage(jsondata);
                }
            }
            monitor.setCreateTime(date);
            monitor.setDeviceTime(date);
            String fengxiang = monitor.getWindDirection();
             if(fengxiang!=null){
                 if(fengxiang.equals("东北")){
                     monitor.setWindDirection("45");
                 }else if(fengxiang.equals("东南")){
                     monitor.setWindDirection("120");
                 }else if(fengxiang.equals("西南")){
                     monitor.setWindDirection("250");
                 }else if(fengxiang.equals("西北")){
                     monitor.setWindDirection("300");
                 }else if(fengxiang.equals("东")){
                     monitor.setWindDirection("90");
                 }else if(fengxiang.equals("南")){
                     monitor.setWindDirection("180");
                 }else if(fengxiang.equals("西")){
                     monitor.setWindDirection("270");
                 }else if(fengxiang.equals("北")){
                     monitor.setWindDirection("0");
                 }else{
                     monitor.setWindDirection(null);
                 }
             }
            zbusProducerHolder.sendYcDataMessage(JSONUtil.toJsonStr(monitor));
        } catch (Exception e) {

        }
        return new ResultDTO(true);
        }


    @PostMapping("crane/insertData")
    public ResultDTO insertCraneData(@RequestParam("uuid") String uuid, @RequestBody CraneDetailDTO crane){

        try {
            Date date = crane.getTime();
            Wrapper wrapper = new EntityWrapper();
            wrapper.eq("is_del",0);
            wrapper.eq("uuid",uuid);
            ProjectInfo projectInfo = projectInfoService.selectOne(wrapper);
            if(projectInfo ==null){
                return new ResultDTO(false,null,"UUID 不存在");
            }

            Wrapper monitorWrapper =  new EntityWrapper();
            monitorWrapper.eq("is_del",0);
            monitorWrapper.eq("project_id",projectInfo.getId());
            monitorWrapper.eq("device_no",crane.getDeviceNo());
            ProjectCrane device  = projectCraneService.selectOne(monitorWrapper);
            if(device == null){
                return new ResultDTO(false,null,"设备不存在");
            }
            if(System.currentTimeMillis()-date.getTime()>180*1000){
                //离线
                if(1==device.getIsOnline()){
                    device.setIsOnline(0);
                    projectCraneService.updateById(device);
                    String jsondata  = "lx:"+crane.getDeviceNo();
                    zbusProducerHolder.sendTjLxMessage(jsondata);
                }
            }else{
                //在线
                if(0==device.getIsOnline()){
                    device.setIsOnline(1);
                    projectCraneService.updateById(device);
                    String jsondata  = "{\"deviceNo\":\""+crane.getDeviceNo()+"\",\"deviceTime\":"+System.currentTimeMillis()+"}";
                    zbusProducerHolder.sendTjHtMessage(jsondata);
                }
            }

            crane.setCreateTime(date);
            crane.setDeviceTime(date);
            zbusProducerHolder.sendTjDataMessage(JSONUtil.toJsonStr(crane));
        } catch (Exception e) {

        }
        return new ResultDTO(true);
    }

    @PostMapping("lift/insertData")
    public ResultDTO insertLiftData(@RequestParam("uuid") String uuid, @RequestBody LiftDetailDTO lift){

        try {
            Date date = lift.getTime();
            Wrapper wrapper = new EntityWrapper();
            wrapper.eq("is_del",0);
            wrapper.eq("uuid",uuid);
            ProjectInfo projectInfo = projectInfoService.selectOne(wrapper);
            if(projectInfo ==null){
                return new ResultDTO(false,null,"UUID 不存在");
            }

            Wrapper monitorWrapper =  new EntityWrapper();
            monitorWrapper.eq("is_del",0);
            monitorWrapper.eq("project_id",projectInfo.getId());
            monitorWrapper.eq("device_no",lift.getDeviceNo());
            ProjectLift device  = projectLiftService.selectOne(monitorWrapper);
            if(device == null){
                return new ResultDTO(false,null,"设备不存在");
            }
            if(System.currentTimeMillis()-date.getTime()>180*1000){
                //离线
                if(1==device.getIsOnline()){
                    device.setIsOnline(0);
                    projectLiftService.updateById(device);
                    String jsondata  = "deviceNo:"+lift.getDeviceNo();
                    zbusProducerHolder.sendSjjLxMessage(jsondata);
                }
            }else{
                //在线
                if(0==device.getIsOnline()){
                    device.setIsOnline(1);
                    projectLiftService.updateById(device);
                    String jsondata  = "{\"deviceNo\":\""+lift.getDeviceNo()+"\",\"deviceTime\":"+System.currentTimeMillis()+"}";
                    zbusProducerHolder.sendSjjHtMessage(jsondata);
                }
            }

            lift.setCreateTime(date);
            lift.setDeviceTime(date);
            zbusProducerHolder.sendSjjDataMessage(JSONUtil.toJsonStr(lift));
        } catch (Exception e) {

        }
        return new ResultDTO(true);
    }

}
