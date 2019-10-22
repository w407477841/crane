package com.xywg.equipment.monitor.modular.whf.factory;


import com.xywg.equipment.monitor.modular.whf.model.ProjectLift;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftAlarm;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDetail;

import java.util.ArrayList;
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

        Integer status =  detail.getStatus();
        if(status ==null){
            List<Integer> statusList =  detail.getStatusList();
            for(Integer temp : statusList){
                if(temp.intValue()!=0){
                    alarms.add(factory(lift, detail,LiftAlarmEnum.getAlarmEnnum(temp)));
                }
            }
        }else{
            if(status != 0){
                alarms.add(factory(lift, detail,LiftAlarmEnum.getAlarmEnnum(status)));
            }
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
        alarm.setCreateTime(detail.getDeviceTime());
        alarm.setIsDel(0);
        return alarm;
    }
}
