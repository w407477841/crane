package com.xywg.equipmentmonitor.modular.station.dto;

import com.xywg.equipmentmonitor.core.util.DateUtil;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:45 2019/3/28
 * Modified By : wangyifei
 */
@Data
public class AlarmsVO {


    private Integer alarmId;
    private String type;
    private String info;
    private String day;
    private String time;
    private String deviceNo;

    public static AlarmsVO  convert(ProjectEnvironmentMonitorAlarmVO alarm, ProjectEnvironmentMonitor projectEnvironmentMonitor){
        AlarmsVO alarmsVO = new AlarmsVO();
        alarmsVO.setAlarmId(alarm.getAlarmId());
        BigDecimal minValue = null;
        BigDecimal maxValue = null;
        switch (alarm.getAlarmId()){
            case 1 :
            case 2 :
                alarmsVO.setType("pm2.5");
                maxValue = projectEnvironmentMonitor.getPm25();
                break;
            case 3:
            case 4:
                alarmsVO.setType("pm10");
                maxValue = projectEnvironmentMonitor.getPm10();
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                alarmsVO.setType("温度");
                minValue = projectEnvironmentMonitor.getTemperatureMax();
                maxValue = projectEnvironmentMonitor.getTemperatureMin();
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                alarmsVO.setType("湿度");
                minValue = projectEnvironmentMonitor.getHumidityMax();
                maxValue = projectEnvironmentMonitor.getHumidityMin();
                break;
            case 13:
            case 14:
                alarmsVO.setType("噪音");
                maxValue = projectEnvironmentMonitor.getNoise();
                break;
            case 15:
            case 16:
                alarmsVO.setType("风速");
                maxValue = projectEnvironmentMonitor.getWindSpeed();
                break;
            case 19:
            case 20:
                alarmsVO.setType("风力");
                maxValue = projectEnvironmentMonitor.getWindForce();
                break;

        }
        boolean isAlarm = false;
        if(maxValue!=null){
            if(maxValue.doubleValue()> alarm.getDataInfo() ){
                // 阈值大于数据 数据是OK的
                isAlarm = false;
            }else{
                isAlarm = true;
            }
        }
        if(!isAlarm&&minValue!=null){
            if(minValue.doubleValue()< alarm.getDataInfo() ){
                // 阈值大于数据 数据是OK的
                isAlarm = false;
            }else{
                isAlarm = true;
            }
        }
        if(isAlarm){
            alarmsVO.setInfo(alarm.getAlarmInfo().replaceAll(alarmsVO.getType(),""));
            alarmsVO.setType(alarmsVO.getType().toUpperCase());
            alarmsVO.setDay(DateUtil.format(alarm.getCreateTime(),"yyyy-MM-dd"));
            alarmsVO.setTime(DateUtil.format(alarm.getCreateTime(),"HH:mm:ss"));
            alarmsVO.setDeviceNo(alarm.getDeviceNo());

            return alarmsVO;
        }
        return null;
    }

}
