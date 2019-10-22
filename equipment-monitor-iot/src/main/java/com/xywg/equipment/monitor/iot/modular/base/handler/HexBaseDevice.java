package com.xywg.equipment.monitor.iot.modular.base.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:41 2018/9/11
 * Modified By : wangyifei
 */

public abstract class HexBaseDevice {

    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    protected XywgProerties xywgProerties;
    @Autowired
    protected PushService pushService;
    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 流程控制
     */
    private boolean isTrue = false;

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
    public boolean isTrue() {
        return isTrue;
    }

    /**
     * 定制流程
     */
    public  void doBusiness(String message, ChannelHandlerContext ctx){
        //解析数据
        Object data = resolver(message,ctx);


        if(isTrue()){
            //获取配置
            Object config = config(data);
            //获取项目
            ProjectInfo project = project(config);

            //心跳操作
            pushAndInsertHeartbeat(config,project);

            //插入获取到的数据
            Object detail = pushAndInsertData(config,data,project);

            //进行告警
            pushAndinsertAlarm(config,data,project,detail);
        }
    }



    /**
     * 解析消息
     * @param message 消息
     * @return
     */
    protected abstract Object resolver(String message,ChannelHandlerContext ctx);

    /**
     * 获取项目信息
     * @return
     */
    protected abstract ProjectInfo project(Object data );

    /**
     * 获取 设备配置
     * @param data
     */
    protected  abstract  Object config(Object data);

    /**
     * 心跳数据插入
     * @param config
     * @param project
     */
    private void pushAndInsertHeartbeat(Object config,ProjectInfo project){
        insertHeartbeat(config);
        //pushRedis();
    }

    /**心跳
     * @param config
     * */
    protected abstract void insertHeartbeat(Object config);
    /**实时数据
     * @param config
     * @param data
     * */
    protected  abstract Object insertData(Object config,Object data);

    /**
     * 数据域数据插入
     * @param config
     * @param data
     * @param project
     * @return
     */
    private Object pushAndInsertData(Object config,Object data,ProjectInfo project){
        Object obj = null;
        if(null != config && null != project){
            obj = insertData(config,data);
            pushDataWebSocket(config,data,project);
        }
        //pushRedis();
        return obj;
    }

    /**
     * 报警
     * @param config
     * @param data
     */
    public  abstract  Object insertAlarm(Object config,Object data,Object project,Object detail);

    /**
     *
     * @param config
     * @param data
     */
    private void pushAndinsertAlarm(Object config,Object data,Object project,Object detail){
        Object pepa = insertAlarm(config,data,project,detail);
        if(null != pepa && null != config && null != project){
            pushMobile(project,config,detail,pepa);
            pushAlarmWebSocket(config,pepa,project);
        }
    }


    /**
     * 手机信息推送
     */
    private void pushMobile(Object project,Object config,Object detail,Object alarmObj){
        if(null != detail && null != alarmObj){
            ProjectInfo pi = (ProjectInfo) project;
            Method mConfig = null;
            Method mAlarm = null;
            try{
                mConfig = config.getClass().getDeclaredMethod("getDeviceNo",null);

                //ProjectElectricPower pep = (ProjectElectricPower) config;

                //ProjectElectricPowerAlarm alarm = (ProjectElectricPowerAlarm) alarmObj;

                mAlarm = alarmObj.getClass().getDeclaredMethod("getAlarmInfo",null);

                StringBuilder sb = new StringBuilder();
                sb.append("项目名称:")
                        .append(pi.getName())
                        .append("\n")
                        .append("设备类型:")
                        .append(getDeviceName())
                        .append("\n")
                        .append("设备号为:")
                        .append(mConfig.invoke(config))
                        .append("\n")
                        .append("错误信息为:")
                        .append(mAlarm.invoke(alarmObj))
                        .append("\n")
                        .append("时间: ")
                        .append(DateUtil.format(new Date(),"HH:mm:ss"));

                pushService.pushMob("注意",sb.toString(),pi.getUuid());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * websocket操作
     */
    private void pushDataWebSocket(Object config,Object data,Object project){
        ProjectInfo pi = (ProjectInfo)project;

        String  topic="/topic/current/"+getModular()+"/"+pi.getUuid();

        System.out.println("订阅路径："+topic);
        // 推送至 websocket
        simpMessageSendingOperations.convertAndSend(topic,data);
    }

    /**
     * 设置模块名
     * @return
     */
     protected abstract String getModular();

    /**
     * 设置设备名
     * @return
     */
    protected abstract String getDeviceName();

    /**
     * websocket操作
     */
    private void pushAlarmWebSocket(Object config,Object alarmObj,Object project){
        if(null != config && null != project){
            ProjectInfo pi = (ProjectInfo)project;

            Method mConfig = null;
            Method mAlarm = null;
            try{
                //ProjectElectricPower pep = (ProjectElectricPower)config;
                mConfig = config.getClass().getDeclaredMethod("getDeviceNo",null);
                //ProjectElectricPowerAlarm pepa = (ProjectElectricPowerAlarm) alarmObj;
                mAlarm = alarmObj.getClass().getDeclaredMethod("getAlarmInfo",null);
                List<AlarmDTO> alarmInfo   = new ArrayList<>();
                AlarmDTO ad = new AlarmDTO();
                ad.setUuid(pi.getUuid());
                ad.setDeviceType(getDeviceName());
                ad.setDeviceNo((String)mConfig.invoke(config));
                ad.setDeviceTime(new Date());
                ad.setInfo((String)mAlarm.invoke(alarmObj));
                alarmInfo.add(ad);
                ResultDTO redisData = ResultDTO.factory(alarmInfo, 21);
                String  topic="/topic/current/"+getModular()+"_alarm/"+pi.getUuid();
                System.out.println("订阅路径："+topic);
                simpMessageSendingOperations.convertAndSend(topic,redisData);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入redis
     * @param redisUtil
     * @param deviceNo
     * @param deviceType
     * @param uuid
     * @param data
     */
    protected void pushRedis(RedisUtil redisUtil, String deviceNo, String deviceType, String uuid, Object data,String projectId){
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
                simpMessageSendingOperations.convertAndSend(tempTopic,data);
            }
        }

        System.out.println("订阅路径："+topic);
        // 推送至 websocket
        simpMessageSendingOperations.convertAndSend(topic,data);


    }




}
