package com.xywg.equipment.monitor.iot.modular.romote.model;

import lombok.Data;

import java.util.Date;

/**
 * @author hjy
 * @date 2018/10/16
 * 设备异常上报日志实体
 */
@Data
public class ProjectMessageUserDeviceErrorLog {
    private Integer id;
    /**

    /**
     * 1塔吊、2升降机,3扬尘、
     **/
    private Integer deviceType;
    /**
     * 发送人
     **/
    private String userIds;

    private Integer isDel;
    private Date createTime;
    private String createUser;
    private Date modifyTime;
    private String modifyUser;


}
