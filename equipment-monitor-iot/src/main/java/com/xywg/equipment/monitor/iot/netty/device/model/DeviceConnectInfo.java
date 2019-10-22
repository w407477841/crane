package com.xywg.equipment.monitor.iot.netty.device.model;

import lombok.Data;

import java.util.Map;

/**
 * @author hjy
 * @date 2019/1/8
 */
@Data
public class DeviceConnectInfo {
    private String uuid;

    private String sn;

    private Map<String,Object> map;

    public DeviceConnectInfo(String uuid, String sn) {
        this.uuid = uuid;
        this.sn = sn;
    }

    public DeviceConnectInfo() {
    }

    public DeviceConnectInfo(String uuid, String sn, Map<String, Object> map) {
        this.uuid = uuid;
        this.sn = sn;
        this.map = map;
    }
}
