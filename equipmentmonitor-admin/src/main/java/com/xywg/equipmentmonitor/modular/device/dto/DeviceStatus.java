package com.xywg.equipmentmonitor.modular.device.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:21 2018/9/2
 * Modified By : wangyifei
 */
@Data
public class DeviceStatus implements Serializable {
    int  amount;
    int isOn;
    int isOff;
    public static DeviceStatus factory(int amount,int isOn ,int isOff){
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setAmount(amount);
        deviceStatus.setIsOn(isOn);
        deviceStatus.setIsOff(isOff);
        return deviceStatus;
    }

}
