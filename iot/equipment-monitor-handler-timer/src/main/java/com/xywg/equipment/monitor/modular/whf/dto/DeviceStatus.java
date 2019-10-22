package com.xywg.equipment.monitor.modular.whf.dto;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 20:54 2018/8/28
 * Modified By : wangyifei
 */
@Data
public class DeviceStatus {

    int  amount;
    int isOn;
    int isOff;

    public DeviceStatus() {
    }

    public DeviceStatus(int amount, int isOn, int isOff) {
        this.amount = amount;
        this.isOn = isOn;
        this.isOff = isOff;
    }
}
