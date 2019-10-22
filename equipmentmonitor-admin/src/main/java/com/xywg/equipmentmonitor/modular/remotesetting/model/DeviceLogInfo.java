package com.xywg.equipmentmonitor.modular.remotesetting.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2018/10/18
 */
@Data
public class DeviceLogInfo {

    Integer id;

    private Integer projectId;

    private String projectName;

    private String version;

    private String deviceType;

    private String deviceNo;

    /**
     * 搜索条件关键字
     */
    private String key;
}
