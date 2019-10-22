package com.xywg.equipment.monitor.core.model;

import lombok.Data;

/**
 * @Author : wangyifei
 * @Description 设备信息
 * @Date: Created in 20:15 2018/8/22
 * @Modified By : wangyifei
 */
@Data
public class DeviceInfo {

    String deviceSN ;
    int monitorType;
    int isOnline;

    public DeviceInfo(String deviceSN, int monitorType, int isOnline) {
        this.deviceSN = deviceSN;
        this.monitorType = monitorType;
        this.isOnline = isOnline;
    }
}
