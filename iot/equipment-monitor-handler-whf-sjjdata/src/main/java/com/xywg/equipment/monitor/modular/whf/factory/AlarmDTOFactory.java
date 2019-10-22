package com.xywg.equipment.monitor.modular.whf.factory;

import cn.hutool.core.date.DateUtil;
import com.xywg.equipment.monitor.modular.whf.dto.AlarmDTO;
import com.xywg.equipment.monitor.modular.whf.model.DeviceAlarm;
import com.xywg.equipment.monitor.modular.whf.model.ProjectInfo;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDetail;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:34 2018/8/28
 * Modified By : wangyifei
 */
public class AlarmDTOFactory {
/**
* @author: wangyifei
* Description:  升降机报警 转
* Date: 9:11 2018/8/28
*/
/*
    public static AlarmDTO  factory(ProjectLiftAlarm  projectLiftAlarm , ProjectInfo project){
        AlarmDTO  alarmDTO  =new AlarmDTO();
        alarmDTO.setDeviceNo(projectLiftAlarm.getDeviceNo());
        alarmDTO.setDeviceTime(projectLiftAlarm.getCreateTime());
        alarmDTO.setDeviceType("升降机设备");
        alarmDTO.setUuid(project.getUuid());
        alarmDTO.setInfo(projectLiftAlarm.getAlarmInfo());
        return alarmDTO;
    }
    */
/**
* @author: wangyifei
* Description: 塔吊报警 转
* Date: 9:47 2018/8/28
*/
/*
    public static AlarmDTO factory(ProjectCraneAlarm alarm,ProjectInfo project){
        AlarmDTO  alarmDTO  =new AlarmDTO();
        alarmDTO.setDeviceNo(alarm.getDeviceNo());
        alarmDTO.setDeviceTime(alarm.getCreateTime());
        alarmDTO.setDeviceType("塔吊设备");
        alarmDTO.setUuid(project.getUuid());
        alarmDTO.setInfo(alarm.getAlarmInfo());
        return alarmDTO;
    }
*/
    /**
     * @author: wangyifei
     * Description: 设备报警 转
     * Date: 9:47 2018/8/28
     */
    public static AlarmDTO factory(DeviceAlarm alarm, ProjectInfo project, String deviceType,ProjectLiftDetail lift,String name){
        AlarmDTO  alarmDTO  =new AlarmDTO();
        alarmDTO.setDeviceNo(alarm.getDeviceNo());
        alarmDTO.setDeviceTime(alarm.getCreateTime());
        alarmDTO.setDeviceType(deviceType);
        alarmDTO.setUuid(project.getUuid());
        alarmDTO.setInfo(alarm.getAlarmInfo());
        alarmDTO.setValue(getValue(alarm,lift));
        alarmDTO.setName(name);
        return alarmDTO;
    }

    public static  String factoryMessage(DeviceAlarm alarm, ProjectInfo project,String deviceType,ProjectLiftDetail lift,String name){

        StringBuilder sb = new StringBuilder();
                sb.append("项目名称:")
                        .append(project.getName())
                        .append("\n")
                        .append("设备类型:")
                         .append(deviceType)
                         .append("\n")
                .append("设备号为:")
                .append(alarm.getDeviceNo())
                         .append("\n")
                        .append("设备名为:")
                        .append(name)
                        .append("\n")
                .append("错误信息为:")
                .append(alarm.getAlarmInfo())
                         .append("\n")
                        .append("当前值为:")
                        .append(getValue(alarm,lift))
                        .append("\n")
                .append("时间: ")
                .append(DateUtil.format(new Date(),"HH:mm:ss"));

        return sb.toString();
    }


    private static String  getValue(DeviceAlarm alarm,ProjectLiftDetail lift){
        String  value = "";
        switch (alarm.getAlarmId()){
            case 1 :
            case 2 :
                value=lift.getWeight()+" T";
                break ;
                  case 3 :
                value=lift.getSpeed()+" m/s";
                break;
            case 4 :
                value=lift.getFrontDoorStatus()==1?"打开":"关闭";
                break;
            case 5 :
                value=lift.getBackDoorStatus()==1?"打开":"关闭";
                break;

        }
        return value ;
    }
}
