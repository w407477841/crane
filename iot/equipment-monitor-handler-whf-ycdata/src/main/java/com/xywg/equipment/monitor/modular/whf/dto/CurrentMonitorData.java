package com.xywg.equipment.monitor.modular.whf.dto;

import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 19:48 2018/8/28
 * Modified By : wangyifei
 */
@Data
public class CurrentMonitorData implements Serializable {

    String deviceNo;
    Double pm25;
    Double pm10;
    Double tsp ;
    Double temperature;
    Double humidity;
    Double windSpeed;
    Double windForce;
    String windDirection;
    Double noise;
    String status;
    Date deviceTime;


    public static CurrentMonitorData factory(ProjectEnvironmentMonitorDetail detail, ProjectEnvironmentMonitor monitor){
        CurrentMonitorData  current = new CurrentMonitorData();
        current.setDeviceNo(detail.getDeviceNo());
        current.setDeviceTime(new Date());
        current.setPm25(detail.getPm25());
        current.setPm10(detail.getPm10());
        current.setTemperature(detail.getTemperature());
        current.setHumidity(detail.getHumidity());
        current.setWindSpeed(detail.getWindSpeed());
        current.setWindForce(detail.getWindForce());
        current.setWindDirection(detail.getWindDirection());
        current.setNoise(detail.getNoise());
        current.setTsp(detail.getTsp());
        Integer status =  monitor.getIsOnline();
        current.setStatus("离线");
        if(status!=null){
            if(status.equals(new Integer(1))){
                current.setStatus("在线");
            }
        }


        return current;
    }


}
