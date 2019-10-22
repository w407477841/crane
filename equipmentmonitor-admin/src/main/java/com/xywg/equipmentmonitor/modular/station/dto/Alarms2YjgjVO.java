package com.xywg.equipmentmonitor.modular.station.dto;

import com.xywg.equipmentmonitor.core.util.DateUtil;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:11 2019/5/10
 * Modified By : wangyifei
 */
@Data
public class Alarms2YjgjVO {

    private Integer alarmId;
    private String type;
    private String info;
    private String day;
    private String time;
    private String deviceNo;

    public static Alarms2YjgjVO  convert(ProjectEnvironmentMonitorAlarmVO alarm){
        Alarms2YjgjVO alarmsVO = new Alarms2YjgjVO();
        alarmsVO.setAlarmId(alarm.getAlarmId());
        switch (alarm.getAlarmId()){
            case 1 :
            case 2 :
                alarmsVO.setType("起重量");
                break;
            case 3:
            case 4:
                alarmsVO.setType("幅度向内");
                break;
            case 5:
            case 6:
                alarmsVO.setType("幅度向外");
                break;
            case 7:
            case 8:
                alarmsVO.setType("高度向上");
                break;
            case 9:
            case 10:
                alarmsVO.setType("力矩");
                break;
            case 11:
            case 12:
                alarmsVO.setType("单机防碰撞");
                break;
            case 13:
            case 14:
                alarmsVO.setType("多机防碰撞");
                break;
            case 15:
                alarmsVO.setType("风速");
                break;
            case 16:
                alarmsVO.setType("倾角");
                break;

        }

            alarmsVO.setInfo(alarm.getAlarmInfo().replaceAll(alarmsVO.getType(),""));
            alarmsVO.setType(alarmsVO.getType().toUpperCase());
            alarmsVO.setDay(DateUtil.format(alarm.getCreateTime(),"yyyy-MM-dd"));
            alarmsVO.setTime(DateUtil.format(alarm.getCreateTime(),"HH:mm:ss"));
            alarmsVO.setDeviceNo(alarm.getDeviceNo());

            return alarmsVO;
    }
}
