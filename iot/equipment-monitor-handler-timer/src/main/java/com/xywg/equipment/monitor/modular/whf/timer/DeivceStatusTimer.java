package com.xywg.equipment.monitor.modular.whf.timer;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.DeviceStatus;
import com.xywg.equipment.monitor.modular.whf.dto.DeviceStatusVO;
import com.xywg.equipment.monitor.modular.whf.dto.RemoteDTO;
import com.xywg.equipment.monitor.modular.whf.service.IProjectEnvironmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
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
@Component
@ConditionalOnProperty(prefix = "timer",name = "status",havingValue = "true")
public class DeivceStatusTimer{
    private final RedisUtil redisUtil;
    @Autowired
    private IProjectEnvironmentMonitorService monitorService;
    @Autowired
    private ZbusProducerHolder  zbusProducerHolder;


    @Autowired
    private XywgProerties xywgProerties;

    @Autowired
    public DeivceStatusTimer(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
    @Scheduled(cron = "0 * * * * ?")
    public void execute(){
     //   System.out.println("==========================设备状态开始=============================");
        // 设备信息缓存
        List<DeviceStatusVO>  list  =  monitorService.deviceStatus();
        for ( DeviceStatusVO statusVO:list ){
            String uuid = statusVO.getUuid();
            if( null!=uuid && !"".equals(uuid)){
                String topic = " /topic/devicestatus/"+uuid;
                String redisKey = xywgProerties.getRedisHead()+":devicestatus:"+uuid;
                DeviceStatus lift = new DeviceStatus(statusVO.getLiftAmount(),statusVO.getLiftOn(),statusVO.getLiftOff());
                DeviceStatus crane = new DeviceStatus(statusVO.getCraneAmount(),statusVO.getCraneOn(),statusVO.getCraneOff());
                DeviceStatus monitor = new DeviceStatus(statusVO.getMonitorAmount(),statusVO.getMonitorOn(),statusVO.getMonitorOff());
                Map<String,Object> devicestatus = new HashMap<>();
                devicestatus.put("craneInfo",crane);
                devicestatus.put("liftInfo",lift);
                devicestatus.put("environmentInfo",monitor);
                RemoteDTO remoteDTO =  RemoteDTO.factory(topic,JSONUtil.toJsonStr(devicestatus));
                zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
                redisUtil.set(redisKey,JSONUtil.toJsonStr(devicestatus));
            }
        }
    }



}
