package com.xywg.equipment.monitor.modular.sb.handler;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.ProducerUtil;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.sb.dao.ProjectWaterMeterAlarmMapper;
import com.xywg.equipment.monitor.modular.sb.dao.ProjectWaterMeterDetailMapper;
import com.xywg.equipment.monitor.modular.sb.dto.AlarmDTO;
import com.xywg.equipment.monitor.modular.sb.dto.ControllerDTO;
import com.xywg.equipment.monitor.modular.sb.dto.RemoteDTO;
import com.xywg.equipment.monitor.modular.sb.dto.WebsocketDTO;
import com.xywg.equipment.monitor.modular.sb.factory.BaseFactory;
import com.xywg.equipment.monitor.modular.sb.model.*;
import com.xywg.equipment.monitor.modular.sb.service.*;
import com.xywg.equipment.monitor.modular.sb.service.impl.ProjectWaterMeterServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:54 2018/9/11
 * Modified By : wangyifei
 */
@Component
public class AmmeterHandler  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmmeterHandler.class);
    /** 0x33 数据加减量*/
    private final int CHANGE_NUM = 51;
    /**预警系数*/
    private final float RATIO = 0.9f;
    /**告警 即实时电表度数大于阈值*/
    private final int CODE_ALERM_OVERFLOW = 1;
    /**预警 即实时电表度数大于阈值90%*/
    private final int CODE_ALERM_EARLY = 2;
    /**告警的中文*/
    private final String TRANS_ALERM_OVERFLOW = "用水报警";
    /**预警的中文*/
    private final String TRANS_ALERM_EARLY = "用电预警";
    /**设备类型*/
    private final String DEVICE_TYPE = "ammeter";
    /**设备名称*/
    private final String DEVICE_NAME = "电表设备";


  //  @Autowired
 //   IProjectElectricPowerDetailService ipepdService;

    @Autowired
    IProjectWaterMeterService  ipepService;


 //   @Autowired
//    IProjectElectricPowerAlarmService ipepaService;

    @Autowired
    IProjectInfoService ipiService;

    @Autowired
    ProjectWaterMeterDetailMapper pepdMapper;


    @Autowired
    ProjectWaterMeterAlarmMapper pepaMapper;

    //@Autowired
   // IProjectElectricPowerHeartbeatService ipephService;
@Autowired
     IProjectWaterMeterHeartbeatService  ipephService;

   // @Autowired
 //   IProjectElectricPowerOriginalService ipepoService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private XywgProerties  xywgProerties;
    @Autowired
    private ZbusProducerHolder zbusProducerHolder;
    @Autowired
    private ProducerUtil producerUtil;


    public void insertOri(String json){
        RemoteDTO remoteDTO =  JSONUtil.toBean(json,RemoteDTO.class);
        ProjectWaterMeterOriginal ori = new ProjectWaterMeterOriginal();
        ori.setDeviceTime(remoteDTO.getDeviceTime());
        ori.setCreateTime(remoteDTO.getCreateTime());
        ori.setSendData(remoteDTO.getSend());
        ori.setReceiveData(remoteDTO.getOri());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertdata(String json, String serverTopic){

        RemoteDTO remoteDTO =  JSONUtil.toBean(json,RemoteDTO.class);

        //获取 设备信息
        ProjectWaterMeter pep = ipepService.getBaseInfo(remoteDTO.getDeviceNo());

        if(pep==null){
            // 压根没有这个设备
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(remoteDTO.getDeviceNo(),"01","water")));
            throw new RuntimeException("数据库中没有"+remoteDTO.getDeviceNo()+"该设备");
        }
        if(0==pep.getStatus()){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(remoteDTO.getDeviceNo(),"01","water")));
            throw new RuntimeException("" + remoteDTO.getDeviceNo() + "该设备未启用");
        }
        ProjectInfo pi=  ipiService.selectById(pep.getProjectId());
        if(null==pi){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(remoteDTO.getDeviceNo(),"01","water")));
            throw new RuntimeException("设备"+remoteDTO.getDeviceNo()+"不在任何项目下");
        }
        /**
         * 互感系数
         */
         BigDecimal ratio =  pep.getRatio();
         if(ratio==null){
             ratio = new BigDecimal(1);
         }
        ProjectWaterMeterDetail  detail  = new ProjectWaterMeterDetail();
        detail.setElectricId(pep.getId());
        detail.setCurrent(new BigDecimal(remoteDTO.getData()));
        detail.setActualDegree(ratio.multiply(detail.getCurrent()));
        detail.setCreateTime(remoteDTO.getCreateTime());
        detail.setDeviceTime(remoteDTO.getDeviceTime());
        detail.setIsDel(0);
        pepdMapper.create(detail, BaseFactory.getTableName(ProjectWaterMeterDetail.class));

        //设置detail的redis key
        String redisDetailKey = "device_platform:current:"+pi.getId()+":water:"+remoteDTO.getDeviceNo();
        if(redisUtil.exists(redisDetailKey)){
            String jsonOld = (String) redisUtil.get(redisDetailKey);
            ProjectWaterMeterDetail oldDetail  = JSONUtil.toBean(jsonOld,ProjectWaterMeterDetail.class);
            BigDecimal curr =   detail.getCurrent();
            BigDecimal old = oldDetail.getCurrent();
            long dateLen = detail.getCreateTime().getTime()-oldDetail.getCreateTime().getTime();
            BigDecimal perCurrent =curr.subtract(old).divide(new BigDecimal(dateLen<1000?0:dateLen/1000),2,ROUND_HALF_UP);
            ProjectWaterMeterAlarm pepa  = null;
            if(perCurrent.compareTo(pep.getDissipation())==1){
              //报警
                //当前每秒读度数大于阈值
                //报警了
                pepa = new ProjectWaterMeterAlarm();
                pepa.setAlarmId(CODE_ALERM_OVERFLOW);
                pepa.setAlarmInfo(TRANS_ALERM_OVERFLOW);
                pepa.setCreateTime(new Date());
                pepa.setElectricId(pep.getId());
                pepa.setDetailId(detail.getId());
                pepa.setIsDel(0);
                pepa.setStatus(0);
                pepaMapper.create(pepa, BaseFactory.getTableName(ProjectWaterMeterAlarm.class));
            }
            if(pepa!= null ){
                this.push(redisUtil,pep.getDeviceNo(),"ammeter_alarm",pi.getUuid(),pepa,pi.getId()+"");
                List<AlarmDTO> alarmInfo   = new ArrayList<>();
                AlarmDTO ad = new AlarmDTO();
                ad.setUuid(pi.getUuid());
                ad.setDeviceType("水表设备");
                ad.setDeviceNo(pep.getDeviceNo());
                ad.setDeviceTime(new Date());
                ad.setInfo("用水报警");
                alarmInfo.add(ad);
                String  topicAlarm="/topic/current/water_alarm/"+pi.getUuid();
                WebsocketDTO websocketDTO = WebsocketDTO.factory(topicAlarm,JSONUtil.toJsonStr(alarmInfo) );
                zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(websocketDTO));
            }
        }
        //推送 实时数据
        this.push(redisUtil,pep.getDeviceNo(),"water",pi.getUuid(),detail,pi.getId()+"");
        String topic =   "/topic/current/water/"+pi.getUuid();
        WebsocketDTO  websocketDTO = WebsocketDTO.factory(topic,JSONUtil.toJsonStr(detail) );
        zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(websocketDTO));



    }



    /**
     * 下线
     * @param data
     */
    @Transactional(rollbackFor = Exception.class)
    public void offline(String data,String serverTopic){
        String deviceNo =  data.split(":")[1];
        ProjectWaterMeter pep=  ipepService.getBaseInfo(deviceNo);

        if(pep==null){
            // 压根没有这个设备
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","water")));
            throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
        }
        if(0==pep.getStatus()){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","water")));
            throw new RuntimeException("" + deviceNo + "该设备未启用");
        }
        ProjectInfo pi=  ipiService.selectById(pep.getProjectId());
        if(null==pi){
            producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","water")));
            throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
        }
        //修改设备状态
        ProjectWaterMeter now = new ProjectWaterMeter();
         now.setId(pep.getId());
         now.setIsOnline(0);
         ipepService.updateById(now);
         String key = xywgProerties.getRedisHead() + ":" + ProjectWaterMeterServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
         redisUtil.remove(key);
    }
@Transactional(rollbackFor = Exception.class)
    public void online(String deviceNo,String serverTopic){
    ProjectWaterMeter pep=  ipepService.getBaseInfo(deviceNo);
    if(pep==null){
        // 压根没有这个设备
        producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","water")));
        throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
    }
    if(0==pep.getStatus()){
        producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","water")));
        throw new RuntimeException("" + deviceNo + "该设备未启用");
    }
    ProjectInfo pi=  ipiService.selectById(pep.getProjectId());
    if(null==pi){
        producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","water")));
        throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
    }
    //修改设备状态
    ProjectWaterMeter now = new ProjectWaterMeter();
    now.setId(pep.getId());
    now.setIsOnline(1);
    ipepService.updateById(now);
    String key = xywgProerties.getRedisHead() + ":" + ProjectWaterMeterServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
    redisUtil.remove(key);

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
                WebsocketDTO remoteDTO = WebsocketDTO.factory(tempTopic,JSONUtil.toJsonStr(data));
                zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));

            }
        }
        System.out.println("订阅路径："+topic);
        // 推送至 websocket
        WebsocketDTO remoteDTO = WebsocketDTO.factory(topic,JSONUtil.toJsonStr(data));
        zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));

    }



}
