package com.xywg.equipment.monitor.iot.modular.timer;

import cn.hutool.json.JSONUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPower;
import com.xywg.equipment.monitor.iot.modular.ammeter.service.IProjectElectricPowerService;
import com.xywg.equipment.monitor.iot.modular.ammeter.service.impl.ProjectElectricPowerServiceImpl;
import com.xywg.equipment.monitor.iot.modular.base.dto.DeviceStatus;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import com.xywg.equipment.monitor.iot.modular.crane.service.impl.ProjectCraneServiceImpl;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentMonitorService;
import com.xywg.equipment.monitor.iot.modular.envmon.service.impl.ProjectEnvironmentMonitorServiceImpl;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLift;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftService;
import com.xywg.equipment.monitor.iot.modular.lift.service.impl.ProjectLiftServiceImpl;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeter;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.IProjectWaterMeterService;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.impl.ProjectWaterMeterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 21:21 2018/8/29
 * Modified By : wangyifei
 */
//@Component
//@JobHandler(value = "device-status")
public class DeivceStatusTimer extends IJobHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProjectLiftService liftService;
    @Autowired
    private IProjectCraneService craneService;
    @Autowired
    private IProjectEnvironmentMonitorService monitorService;

    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private IProjectElectricPowerService ipepService;

    @Autowired
    private IProjectWaterMeterService ipwmService;

    @Autowired
    private XywgProerties xywgProerties;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
     //   System.out.println("==========================设备状态开始=============================");
        // 设备信息缓存
        List<Map<String,String>> devices  =  craneService.selectAllDevice();

        for(Map<String,String> map  :devices){
            System.out.println(map.get("deviceStr"));
        }

        for(Map<String,String> map  :devices){
            doByUuid(map);
        }

    //    System.out.println("==========================设备状态结束=============================");

        return null;
    }

    private void doByUuid(Map<String,String> map){
        String deviceStr = map.get("deviceStr");
        String uuid =  map.get("uuid");
        String [] devices = deviceStr.split(",");

        DeviceStatus  craneStatus = new DeviceStatus();
        DeviceStatus  liftStatus = new DeviceStatus();
        DeviceStatus  monitorStatus = new DeviceStatus();

        for(String device : devices){
            String key = xywgProerties.getRedisHead()+":head:"+device;
            if(redisUtil.exists(key)){
                //在线
                modifyStatusCache(device,1,craneStatus,liftStatus,monitorStatus);

            }else{
                //离线
                modifyStatusCache(device,0,craneStatus,liftStatus,monitorStatus);
            }

        }
        Map<String,Object> devicestatus = new HashMap<>();
        devicestatus.put("craneInfo",craneStatus);
        devicestatus.put("liftInfo",liftStatus);
        devicestatus.put("environmentInfo",monitorStatus);
        simpMessageSendingOperations.convertAndSend("/topic/devicestatus/"+uuid,devicestatus);
        redisUtil.set(xywgProerties.getRedisHead()+":devicestatus:"+uuid,JSONUtil.toJsonStr(devicestatus));
    }


    /**
     *  修改 设备在线状态
     * @param device  设备头信息
     * @param status  在线状态
     * @param craneStatus 塔吊设备状态
     * @param liftStatus 扬尘设备状态
     * @param monitorStatus 扬尘设备状态
     */
    private void modifyStatusCache(String device, int status, DeviceStatus craneStatus, DeviceStatus  liftStatus, DeviceStatus  monitorStatus ){
        String headerCrane="qzjht";
        String headerLift="sjjht";
        String headerMonitor="ycht";
        String headerAmeter = "dbht";
        String headerWater = "sbht";
        String split = ":";
        if(headerCrane.equals(device.split(split)[0])){
            //塔吊
            String deviceNo = device.split(split)[1];
            ProjectCrane crane = craneService.selectOne(deviceNo);
            //System.out.println("crane"+deviceNo);

            if(crane != null){

                if(null==crane.getIsOnline()||crane.getIsOnline()!=status){
                    doStatusChange("crane",deviceNo,status);
                    //修改状态
                    crane.setIsOnline(status);
                    String cacheKey  =  xywgProerties.getRedisHead()+":"+ ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
                     redisUtil.set(cacheKey,crane,5L);
                }


                craneStatus.setAmount(craneStatus.getAmount()+1);
                if(status == 1){
                    craneStatus.setIsOn(craneStatus.getIsOn()+1);
                }else{
                    craneStatus.setIsOff(craneStatus.getIsOff()+1);
                }

            }else{
                    craneStatus.setAmount(craneStatus.getAmount()+1);
                        craneStatus.setIsOff(craneStatus.getIsOff()+1);
                }

        }
        else if(headerLift.equals(device.split(split)[0])){
            //升降机
            String deviceNo = device.split(split)[1];
            ProjectLift lift = liftService.selectOne(deviceNo);

            if(lift!=null){

                if(null==lift.getIsOnline()||lift.getIsOnline()!=status){
                    doStatusChange("lift",deviceNo,status);
                    //修改状态
                    lift.setIsOnline(status);
                    String cacheKey  =  xywgProerties.getRedisHead()+":"+  ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
                     redisUtil.set(cacheKey,lift,5L);
                }


                liftStatus.setAmount(liftStatus.getAmount()+1);
                if(status == 1){
                    liftStatus.setIsOn(liftStatus.getIsOn()+1);
                }else{
                    liftStatus.setIsOff(liftStatus.getIsOff()+1);
                }
            }else{
                liftStatus.setAmount(liftStatus.getAmount()+1);
                    liftStatus.setIsOff(liftStatus.getIsOff()+1);
            }



        }else if(headerMonitor.equals(device.split(split)[0])){
            //扬尘
            String deviceNo = device.split(split)[1];
            ProjectEnvironmentMonitor monitor = monitorService.selectOne(deviceNo);
            if(monitor!=null){
                if(null==monitor.getIsOnline()||monitor.getIsOnline()!=status){
                    doStatusChange("environment_monitor",deviceNo,status);
                    //修改状态
                    monitor.setIsOnline(status);
                    String cacheKey  =  xywgProerties.getRedisHead()+":"+  ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
                     redisUtil.set(cacheKey,monitor,5L);
                }

                monitorStatus.setAmount(monitorStatus.getAmount()+1);
                if(status == 1){
                    monitorStatus.setIsOn(monitorStatus.getIsOn()+1);
                }else{
                    monitorStatus.setIsOff(monitorStatus.getIsOff()+1);
                }
            }else{
                monitorStatus.setAmount(monitorStatus.getAmount()+1);
                    monitorStatus.setIsOff(monitorStatus.getIsOff()+1);
            }

        }else if(headerAmeter.equals(device.split(split)[0])){
            //电表
            String deviceNo = device.split(split)[1];
            //System.out.println("################电表开始:"+deviceNo+"##################");
            ProjectElectricPower pep = ipepService.getBaseInfo(deviceNo);
            //System.out.println("######"+status+"#####:"+pep);
            if(pep!=null){
                if(null==pep.getIsOnline()||pep.getIsOnline()!=status){
                    doStatusChange("electric_power",deviceNo,status);
                    //修改状态
                    pep.setIsOnline(status);
                    String cacheKey  =  xywgProerties.getRedisHead()+":"+  ProjectElectricPowerServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
                      redisUtil.set(cacheKey,pep,5L);
                }

            }
        }else if(headerWater.equals(device.split(split)[0])){
            //水表
            String deviceNo = device.split(split)[1];
            ProjectWaterMeter pwm = ipwmService.getBaseInfo(deviceNo);
            if(pwm!=null){
                if(null==pwm.getIsOnline()||pwm.getIsOnline()!=status){
                    doStatusChange("water_meter",deviceNo,status);
                    //修改状态
                    pwm.setIsOnline(status);
                    String cacheKey  =  xywgProerties.getRedisHead()+":"+  ProjectWaterMeterServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
                    redisUtil.set(cacheKey,pwm,5L);
                }

            }
        }
      //  System.out.println("#################结束###################");
    }

    private void doStatusChange(String deviceType,String deviceNo,int status){
        String tableName = "t_project_"+deviceType;
        craneService.doStatusChange(tableName,"'"+deviceNo+"'",status);
    }

}
