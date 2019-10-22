package com.xywg.equipment.monitor.modular.whf.factory;


import com.xywg.equipment.monitor.modular.whf.model.ProjectCrane;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneAlarm;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneDetail;

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
        Integer status =  detail.getStatus();
        if(status ==null){
            List<Integer> statusList =  detail.getStatusList();
            for(Integer temp : statusList){
                if(temp.intValue()!=0){
                    alarms.add(factory(crane,detail,CraneAlarmEnum.getAlarm(temp)));
                }
            }
        }else{
            if(status != 0){
                alarms.add(factory(crane,detail,CraneAlarmEnum.getAlarm(status)));
            }
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
        alarm.setCreateTime(detail.getDeviceTime());
        alarm.setIsDel(0);
        return alarm;
    }
}
