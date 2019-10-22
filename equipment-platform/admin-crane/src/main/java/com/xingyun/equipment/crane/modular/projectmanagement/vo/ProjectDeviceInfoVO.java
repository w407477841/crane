package com.xingyun.equipment.crane.modular.projectmanagement.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:46 2018/9/17
 * Modified By : wangyifei
 */
@Data
public class ProjectDeviceInfoVO implements Serializable {




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

    /**
     * 类别
     */
    private String type;

}
