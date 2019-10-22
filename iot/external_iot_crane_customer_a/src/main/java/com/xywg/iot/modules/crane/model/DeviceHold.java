package com.xywg.iot.modules.crane.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2018/12/28
 * 设备持有绑定
 */
@Data
public class DeviceHold {
    private String uuid;
    private ProjectCrane projectCrane;


    public DeviceHold(String uuid, ProjectCrane projectCrane) {
        this.uuid = uuid;
        this.projectCrane = projectCrane;
    }

    public DeviceHold() {
    }
}
