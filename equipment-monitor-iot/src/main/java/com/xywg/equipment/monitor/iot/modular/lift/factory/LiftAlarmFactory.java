package com.xywg.equipment.monitor.iot.modular.lift.factory;

import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneAlarm;
import com.xywg.equipment.monitor.iot.modular.lift.enums.LiftAlarmEnum;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLift;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftAlarm;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftDetail;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:37 2018/8/24
 * Modified By : wangyifei
 */
public class LiftAlarmFactory {
    /**
    * @author: wangyifei
    * Description:
    * Date: 15:40 2018/8/24
    */
    public static List<ProjectLiftAlarm> factory(ProjectLift lift, ProjectLiftDetail detail){
        List<ProjectLiftAlarm>  alarms  = new ArrayList<>();

        int status  =  detail.getStatus();
        if(status!= 0 ){
            alarms.add(factory(lift, detail,LiftAlarmEnum.getAlarmEnnum(status)));
        }

        return alarms;
    }

    public static ProjectLiftAlarm factory(ProjectLift lift, ProjectLiftDetail detail, LiftAlarmEnum alarmEnum){

        ProjectLiftAlarm alarm = new ProjectLiftAlarm();
        alarm.setDetailId(detail.getId());
        alarm.setDeviceNo(detail.getDeviceNo());
        alarm.setAlarmId(alarmEnum.getStatus());
        alarm.setAlarmInfo(alarmEnum.getMsg());
        alarm.setLiftId(lift.getId());
        alarm.setIsDel(0);
        return alarm;
    }

    public static ProjectLiftAlarm factory(ProjectLift lift, JSONObject object){
        ProjectLiftAlarm alarm = new ProjectLiftAlarm();
        alarm.setLiftId(lift.getId());
        alarm.setAlarmInfo(object.getString("AlarmInfo"));
        alarm.setAlarmId(object.getInt("Level"));
        alarm.setCreateTime(new Date());
        alarm.setIsDel(0);
        return alarm;
    }


}
