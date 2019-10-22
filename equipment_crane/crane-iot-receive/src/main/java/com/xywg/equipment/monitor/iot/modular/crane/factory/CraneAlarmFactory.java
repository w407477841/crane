package com.xywg.equipment.monitor.iot.modular.crane.factory;

import com.xywg.equipment.monitor.iot.modular.crane.enums.CraneAlarmEnum;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneAlarm;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneDetail;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : wangyifei
 * @Description
 * @Date: Created in 9:18 2018/8/24
 * @Modified By : wangyifei
 */
public class CraneAlarmFactory {
    /**
    * @author: wangyifei
    * Description: 根据阈值 ， 和实时数据生成 报警数据
    * Date: 9:21 2018/8/24
    */
    public static List<ProjectCraneAlarm> factory(ProjectCrane crane, ProjectCraneDetail detail){
        List<ProjectCraneAlarm> alarms = new ArrayList<>();
         //状态
        int status =  detail.getStatus();
        if(status != 0){
            alarms.add(factory(crane,detail,CraneAlarmEnum.getAlarm(status)));
        }

        return alarms;
    }

    public static ProjectCraneAlarm factory(ProjectCrane crane,ProjectCraneDetail detail,CraneAlarmEnum alarmEnum){

        ProjectCraneAlarm  alarm= new ProjectCraneAlarm();
        alarm.setAlarmId(alarmEnum.getStatus());
        alarm.setCraneId(crane.getId());
        alarm.setDetailId(detail.getId());
        alarm.setDeviceNo(crane.getDeviceNo());
        alarm.setAlarmInfo(alarmEnum.getMsg());
        alarm.setIsDel(0);
        return alarm;
    }

    public static ProjectCraneAlarm factory(ProjectCrane crane,JSONObject  object){
        ProjectCraneAlarm  alarm  = new ProjectCraneAlarm();
        alarm.setCraneId(crane.getId());
        alarm.setAlarmInfo(object.getString("AlarmInfo"));
        alarm.setAlarmId(object.getInt("Level"));
        alarm.setIsDel(0);
        alarm.setCreateTime(new Date());
        return alarm;
    }

}
