package com.xywg.equipment.monitor.iot.modular.envmon.factory;

import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneAlarm;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitorAlarm;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitorDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:57 2018/8/24
 * Modified By : wangyifei
 */
public class MonitorAlarmFactory {
/**
* @author: wangyifei
* Description:
* Date: 11:10 2018/8/24
*/
    public static List<ProjectEnvironmentMonitorAlarm> factory(ProjectEnvironmentMonitor monitor, ProjectEnvironmentMonitorDetail detail){
        List<ProjectEnvironmentMonitorAlarm> alarms = new ArrayList<>();

        // pm2.5 阈值
       Double pm25 =  monitor.getPm25();
       if(pm25 != null) {
           if (pm25 < detail.getPm25()) {
               alarms.add(factory(detail, "pm2.5报警",1,detail.getPm25().toString()));

           }
           if (pm25.equals(detail.getPm25())) {
               alarms.add(factory(detail, "pm2.5预警",2,detail.getPm25().toString()));
           }
       }
       // pm10  阈值
        Double pm10 = monitor.getPm10();
       if(pm10 != null){
           if(pm10 <detail.getPm10()){
               alarms.add(factory(detail,"pm10报警",3,detail.getPm10().toString()));
           }
           if(pm10 .equals(detail.getPm10())){
               alarms.add(factory(detail,"pm10预警",4,detail.getPm10().toString()));
           }
       }

        // 温度 阈值
        Double temperatureMax=    monitor.getTemperatureMax();
       if(temperatureMax != null){
           if(temperatureMax<detail.getTemperature()){
               alarms.add(factory(detail,"高温报警",5,detail.getTemperature().toString()));
           }
           if(temperatureMax.equals((detail.getTemperature()))){
               alarms.add(factory(detail,"高温预警",6,detail.getTemperature().toString()));
           }
       }

        Double temperatureMin=    monitor.getTemperatureMin();
        if(temperatureMin != null){
            if(temperatureMin>detail.getTemperature()){
                alarms.add(factory(detail,"低温报警",7,detail.getTemperature().toString()));
            }
            if(temperatureMin.equals((detail.getTemperature()))){
                alarms.add(factory(detail,"低温预警",8,detail.getTemperature().toString()));
            }
        }

      //湿度
        Double humidityMax = monitor.getHumidityMax();
       if(humidityMax!=null){
           if(humidityMax<detail.getHumidity()){
               alarms.add(factory(detail,"湿度超标报警",9,detail.getHumidity().toString()));
           }
           if(humidityMax.equals(detail.getHumidity())){
               alarms.add(factory(detail,"湿度超标预警",10,detail.getHumidity().toString()));
           }
       }
        Double humidityMin = monitor.getHumidityMin();
        if(humidityMax!=null){
            if(humidityMin>detail.getHumidity()){
                alarms.add(factory(detail,"湿度过低报警",11,detail.getHumidity().toString()));
            }
            if(humidityMin.equals(detail.getHumidity())){
                alarms.add(factory(detail,"湿度过低预警",12,detail.getHumidity().toString()));
            }
        }

        //噪音
        Double noice  =monitor.getNoise();
       if(noice!=null){
           if(noice<detail.getNoise()){
               alarms.add(factory(detail,"噪音报警",13,detail.getNoise().toString()));
           }
           if(noice.equals(detail.getNoise())){
               alarms.add(factory(detail,"噪音预警",14,detail.getNoise().toString()));
           }
       }


       //风速
       Double windSpeed  = monitor.getWindSpeed();
       if(windSpeed!=null){
           if(windSpeed<detail.getWindSpeed()){
               alarms.add(factory(detail,"风速报警",15,detail.getWindSpeed().toString()));
           }
           if(windSpeed.equals(detail.getWindSpeed())){
               alarms.add(factory(detail,"风速预警",16,detail.getWindSpeed().toString()));
           }
       }

       //tsp  17 报警  18 预警


        return alarms;
    }

    public static ProjectEnvironmentMonitorAlarm factory( ProjectEnvironmentMonitorDetail detail,String info,Integer alarmId,String value){
        ProjectEnvironmentMonitorAlarm  alarm =  new ProjectEnvironmentMonitorAlarm();
        alarm.setAlarmInfo(info);
        alarm.setDeviceNo(detail.getDeviceNo());
        alarm.setMonitorId(detail.getMonitorId());
        alarm.setDetailId(detail.getId());
        alarm.setCreateTime(new Date());
        alarm.setAlarmId(alarmId);
        alarm.setDeviceTime(new Date());
        alarm.setValue(value);
               return alarm;

    }

}
