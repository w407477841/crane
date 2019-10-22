package com.xywg.equipment.monitor.iot.modular.romote.model;

import lombok.Data;

import java.util.Date;

/**
 * @author hjy
 * @date 2018/10/16
 * 设备异常时短信推送记录
 */
@Data
public class ProjectMessageEviceError {
    private Integer id;
    /**
     * 发送人id
     */
    private String userIds;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 设备编号
     **/
    private String deviceNo;
    /**
     * 所属项目id
     */
    private Integer projectId;
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
