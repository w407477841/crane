package com.xywg.equipment.monitor.iot.modular.crane.dto;

import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:25 2018/8/28
 * Modified By : wangyifei
 */
@Data
public class CurrentCraneData implements Serializable {

    String  deviceNo;
    String identifier;
    String manufactor;
    String  owner;
    Double  weight  ;
    Double height;
    Double rang ;
    Double rotaryAngle;
    Double moment;
    String status;
    Date deviceTime;
    Double safetyMoment;
    Double safetyWeight;


    public static CurrentCraneData factory(ProjectCraneDetail detail,ProjectCrane crane){
        CurrentCraneData current  =new CurrentCraneData();
        current.setDeviceNo(detail.getDeviceNo());
        current.setDeviceTime(new Date());
        current.setHeight(detail.getHeight());
        current.setWeight(detail.getWeight());
        current.setSafetyWeight(detail.getSafetyWeight());
        current.setIdentifier(crane.getIdentifier());
        current.setMoment(detail.getMoment());
        current.setSafetyMoment(detail.getSafetyMoment());
        current.setRang(detail.getRange());
        current.setRotaryAngle(detail.getRotaryAngle());
        current.setManufactor(crane.getManufactor());
        current.setOwner(crane.getOwner());
        Integer status =  crane.getIsOnline();
        current.setSafetyWeight(detail.getSafetyWeight());
        current.setSafetyMoment(detail.getSafetyMoment());
        if(status!=null){
            if(status.equals(new Integer(1))){
                current.setStatus("在线");
            }else{
                current.setStatus("离线");
            }
        }else {
            current.setStatus("未知");
        }
        return current;
    }


}
