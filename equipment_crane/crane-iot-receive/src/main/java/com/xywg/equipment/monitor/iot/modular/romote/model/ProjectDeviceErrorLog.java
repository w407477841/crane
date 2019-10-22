package com.xywg.equipment.monitor.iot.modular.romote.model;

import lombok.Data;

import java.util.Date;

/**
 * @author hjy
 * @date 2018/10/16
 * 设备异常上报日志实体
 */
@Data
public class ProjectDeviceErrorLog {
    private Integer id;
    /**
     * 设备编号
     **/
    private String deviceNo;
    /**
     * 异常信息
     **/
    private String content;
    /**
     * 所属模块(1扬尘、2塔吊、3升降机)
     **/
    private Integer type;
    /**
     * 所属项目id
     **/
    private Integer projectId;
    /**
     * 异常时间
     **/
    private Date errorTime;
    private Integer isDel;
    private Date createTime;
    private String createUser;
    private Date modifyTime;
    private String modifyUser;

    public ProjectDeviceErrorLog(String deviceNo, String content, Integer type, Integer projectId, Date errorTime, String createUser) {
        this.deviceNo = deviceNo;
        this.content = content;
        this.type = type;
        this.projectId = projectId;
        this.errorTime = errorTime;
        this.createUser = createUser;
    }

    public ProjectDeviceErrorLog() {
    }
}
