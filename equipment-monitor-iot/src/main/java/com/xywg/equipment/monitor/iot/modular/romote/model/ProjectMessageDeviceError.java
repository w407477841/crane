package com.xywg.equipment.monitor.iot.modular.romote.model;

import lombok.Data;

import java.util.Date;


/**
 * @author hjy
 */
@Data
public class ProjectMessageDeviceError {

   /* private static final long serialVersionUID = 1L;*/

    /**
     * ID
     */
    private Integer id;
    /**
     * 发送人ID
     */
    private String userIds;
    /**
     * 发送内容
     */
    private String content;

    private Integer projectId;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 发送时间
     */
    private Date sendTime;


    private Integer isDel;
    private Date createTime;
    private String createUser;
    private Date modifyTime;
    private String modifyUser;
}
