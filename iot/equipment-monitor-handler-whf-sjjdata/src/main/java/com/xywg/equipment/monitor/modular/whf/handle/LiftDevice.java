package com.xywg.equipment.monitor.modular.whf.handle;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.ProducerUtil;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.*;
import com.xywg.equipment.monitor.modular.whf.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.modular.whf.factory.BaseFactory;
import com.xywg.equipment.monitor.modular.whf.factory.LiftAlarmFactory;
import com.xywg.equipment.monitor.modular.whf.model.*;
import com.xywg.equipment.monitor.modular.whf.service.*;
import com.xywg.equipment.monitor.modular.whf.service.impl.ProjectLiftServiceImpl;
import com.xywg.equipment.monitor.modular.whf.service.impl.PushService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author wangyifei
 */
@Component
public class LiftDevice{
    @Autowired
    private IProjectLiftDetailService projectLiftDetailService;
    @Autowired
    private IProjectLiftService projectLiftService;
    @Autowired
    private IProjectLiftAlarmService projectLiftAlarmService;
    @Autowired
    private IProjectLiftOriginalDataService projectLiftOriginalDataService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProjectLiftHeartbeatService projectLiftHeartbeatService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ZbusProducerHolder zbusProducerHolder;
    @Autowired
    private PushService pushService;
    @Autowired
    private XywgProerties xywgProerties;
    @Autowired
    private  ProducerUtil producerUtil;

    @Autowired
    private  IProjectLiftPictureDetailService projectLiftPictureDetailService;

    @Transactional(rollbackFor = Exception.class)
    public void insertData(ProjectLiftDetail detail,String serverTopic) throws Exception {
        //获取设备信息
        ProjectLift lift =  projectLiftService.selectOne(detail.getDeviceNo());
        if(lift == null){
            // 压根没有这个设备
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","lift")));
            throw new RuntimeException("数据库中没有"+detail.getDeviceNo()+"该设备");
        }
        if(0==lift.getStatus()){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","lift")));
            throw new RuntimeException("" + detail.getDeviceNo() + "该设备未启用");
        }
        ProjectInfo projectInfo =  projectInfoService.selectById(lift.getProjectId());
        if(projectInfo==null){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","lift")));
            throw new RuntimeException("设备"+detail.getDeviceNo()+"不在任何项目下");
        }
        detail.setLiftId(lift.getId());
        CurrentLiftData currentMonitorData =CurrentLiftData.factory(detail,lift);
        projectLiftDetailService.createDetail(detail, BaseFactory.getTableName(ProjectLiftDetail.class));
        // 比对 阈值
        List<ProjectLiftAlarm> alarms = LiftAlarmFactory.factory(lift,detail);
        List<AlarmDTO>   alarmInfo   = new ArrayList<>();
        for(ProjectLiftAlarm alarm:alarms){
            projectLiftAlarmService.createAlarm(alarm,BaseFactory.getTableName(ProjectLiftAlarm.class));
            alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"升降机设备",detail,lift.getName()));
            pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"升降机设备",detail,lift.getName()),projectInfo.getUuid());
        }
        //推送实时数据
        this.push(redisUtil,lift.getDeviceNo(),"lift",projectInfo.getUuid(),currentMonitorData,""+lift.getProjectId());
        //推送报警数据
        if(alarmInfo.size()>0){
            ResultDTO redisData =  ResultDTO.factory(alarmInfo,20);
            //放入缓存
            this.push(redisUtil,lift.getDeviceNo(),"lift_alarm",projectInfo.getUuid(),redisData,""+lift.getProjectId());
        }
    }

    public ProjectLiftDetail insertOriginData(String data) {
        ProjectLiftDetail  detail =  JSONUtil.toBean(data,ProjectLiftDetail.class) ;
        ProjectLiftOriginalData originalData  =  new ProjectLiftOriginalData();
        originalData.setOriginalData(data);
        originalData.setDeviceTime(detail.getDeviceTime());
        return detail;
    }



    public  void push(RedisUtil redisUtil,String deviceNo,String deviceType,String uuid,Object data,String projectId){
        String key  = xywgProerties.getRedisHead()+":current:"+uuid+":"+deviceType+":"+deviceNo;
        String topic = "";
        if(deviceType.endsWith("alarm")){
            topic="/topic/current/"+deviceType+"/"+uuid;
        }else{
            topic="/topic/current/"+deviceType+"/"+uuid+"/"+deviceNo;
            redisUtil.set(key, JSONUtil.toJsonStr(data));
            if(StringUtils.isNotEmpty(projectId)){
                String tempKey  = xywgProerties.getRedisHead()+":current:"+projectId+":"+deviceType+":"+deviceNo;
                String tempTopic="/topic/current/"+deviceType+"/"+projectId+"/"+deviceNo;
                redisUtil.set(tempKey, JSONUtil.toJsonStr(data));
                RemoteDTO remoteDTO = RemoteDTO.factory(tempTopic,JSONUtil.toJsonStr(data));
                zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
            }
        }
        System.out.println("订阅路径："+topic);
        // 推送至 websocket
        RemoteDTO remoteDTO = RemoteDTO.factory(topic,JSONUtil.toJsonStr(data));
        zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
    }

    /**
     * 上线
     * @param ht
     */
    public void online(Map<String,Object> ht) {


        ProjectLiftHeartbeat heartbeat = new ProjectLiftHeartbeat();
        String deviceNo = (String)ht.get("deviceNo");
        Long time = (Long) ht.get("deviceTime");
        String serverTopic = (String)ht.get("serverTopic");
        heartbeat.setDeviceNo(deviceNo);
        // 获取 设备信息

        ProjectLift lift =  projectLiftService.selectOne(deviceNo);
        if(lift == null){
            // 压根没有这个设备
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","lift")));
            throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
        }
        if(0==lift.getStatus()){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","lift")));
            throw new RuntimeException("" +deviceNo + "该设备未启用");
        }
        ProjectInfo projectInfo =  projectInfoService.selectById(lift.getProjectId());
        if(projectInfo==null){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","lift")));
            throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
        }
        heartbeat.setLiftId(lift.getId());
        heartbeat.setCreateTime(new Date(time));

        projectLiftHeartbeatService.doOpenBusiness(heartbeat);
        // 此处设备为在线
        //1.修改数据库 online 字段
        ProjectLift updlift = new ProjectLift();
        updlift.setId(lift.getId());
        updlift.setIsOnline(1);
        projectLiftService.updateById(updlift);
        //删除 设备信息缓存
        String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        redisUtil.remove(deviceKey);


    }

    public Map<String,Object>  insertOrigin(String data) {
        Map<String,Object> ht =  JSONUtil.toBean(data,Map.class) ;
        ProjectLiftOriginalData originalData  =  new ProjectLiftOriginalData();
        originalData.setOriginalData(data);
        originalData.setDeviceTime(new Date((Long)ht.get("deviceTime")));
        return ht;
    }

    /**
     * 插入图像
     * @param data
     */
   public void insertImg(String data){

       JSONObject json=JSONUtil.parseObj(data);
       String deviceNo = json.get("deviceNo").toString();
       ProjectLift lift =  projectLiftService.selectOne(deviceNo);
       if(lift == null){
           // 压根没有这个设备

           throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
       }else{
          String url = json.get("url").toString();
          String deviceTime =json.get("deviceTime").toString();
          ProjectLiftPictureDetail detail = new ProjectLiftPictureDetail();
          detail.setLiftId(lift.getId());
          detail.setDeviceNo(deviceNo);
          detail.setCreateTime(new Date());
          detail.setIsDel(0);
          detail.setUrl(url);
           SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm;ss");
          String deviceTimeNew = sdf.format(new Date(Long.parseLong(deviceTime)));
           detail.setDeviceTime(deviceTimeNew);
           projectLiftPictureDetailService.insertImg(detail);
       }

   }


    /**
     * 下线
     */
    public void offline(String data,String serverTopic){
        String deviceNo =  data.split(":")[1];

        ProjectLift lift =  projectLiftService.selectOne(deviceNo);
        if(lift == null){
            // 压根没有这个设备
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","lift")));
            throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
        }
        if(0==lift.getStatus()){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","lift")));
            throw new RuntimeException("" +deviceNo + "该设备未启用");
        }
        ProjectInfo projectInfo =  projectInfoService.selectById(lift.getProjectId());
        if(projectInfo==null){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","lift")));
            throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
        }
        // 更新 受控数据的截止时间
        ProjectLiftHeartbeat  projectLiftHeartbeat = new ProjectLiftHeartbeat();
        projectLiftHeartbeat.setDeviceNo(deviceNo);
        projectLiftHeartbeat.setCreateTime(new Date());
        projectLiftHeartbeatService.updateEndTime(projectLiftHeartbeat);

        //1.修改数据库 online 字段
        ProjectLift updLift = new ProjectLift();
        updLift.setId(lift.getId());
        updLift.setIsOnline(0);
        projectLiftService.updateById(updLift);
        //删除 设备信息缓存
        String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        redisUtil.remove(deviceKey);

    }
}
