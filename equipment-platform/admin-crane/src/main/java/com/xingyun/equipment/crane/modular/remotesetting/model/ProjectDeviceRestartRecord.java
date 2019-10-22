package com.xingyun.equipment.crane.modular.remotesetting.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.core.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author hjy
 * 重启履历实体
 */
@TableName("t_project_device_restart_record")
@Data
public class ProjectDeviceRestartRecord extends BaseEntity<ProjectDeviceRestartRecord> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("device_no")
    private String deviceNo;

     /**
     * 1塔吊、2升降机,3扬尘、
     **/
    @TableField("type")
    private String type;
    /**
     * 项目ID
     **/
    @TableField("project_id")
    private Integer projectId;

    /**
     * 重启时间
     */
    @TableField("restart_time")
    private Date restartTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public ProjectDeviceRestartRecord(String deviceNo, String type, Integer projectId, Date restartTime) {
        this.deviceNo = deviceNo;
        this.type = type;
        this.projectId = projectId;
        this.restartTime = restartTime;
    }


    public ProjectDeviceRestartRecord() {
    }
}
