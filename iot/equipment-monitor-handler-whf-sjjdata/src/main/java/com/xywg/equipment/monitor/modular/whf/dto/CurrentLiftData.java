package com.xywg.equipment.monitor.modular.whf.dto;

import com.xywg.equipment.monitor.modular.whf.model.ProjectLift;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 19:33 2018/8/28
 * Modified By : wangyifei
 */
@Data
public class CurrentLiftData implements Serializable {
    String  deviceNo;
    String identifier;
    String manufactor;
    String  owner;
    Double  weight  ;
    Double height;
    Date deviceTime;
    Double speed;
    String frontDoorStatus;
    String backDoorStatus;
    Integer floor;
    Integer floorStart;
    Integer floorEnd;
    String status;


    public static CurrentLiftData factory(ProjectLiftDetail detail, ProjectLift lift){
        CurrentLiftData current = new CurrentLiftData();
        current.setDeviceNo(detail.getDeviceNo());
        current.setDeviceTime(new Date());
        current.setHeight(detail.getHeight());
        current.setWeight(detail.getWeight());
        current.setIdentifier(lift.getIdentifier());
        current.setManufactor(lift.getManufactor());
        current.setOwner(lift.getOwner());
        current.setFrontDoorStatus(detail.getFrontDoorStatus()==0?"关闭":"开启");
        current.setBackDoorStatus(detail.getBackDoorStatus()==0?"关闭":"开启");
        current.setSpeed(detail.getSpeed());
        Integer status =  lift.getIsOnline();
        current.setStatus("离线");
        if(status!=null){
            if(status.equals(new Integer(1))){
                current.setStatus("在线");
            }
        }
        return current;
    }

}
