package com.xingyun.equipment.crane.modular.projectmanagement.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hjy
 */
@Data
public class ProjectDeviceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 项目uuid
     */
    private String uuid;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备状态
     */
    private String state;

}
