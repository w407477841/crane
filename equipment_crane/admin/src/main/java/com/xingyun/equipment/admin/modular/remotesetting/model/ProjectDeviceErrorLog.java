package com.xingyun.equipment.admin.modular.remotesetting.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author hjy
 * 设备异常日志履历实体
 */
@TableName("t_project_device_error_log")
@Data
public class ProjectDeviceErrorLog extends BaseEntity<ProjectDeviceErrorLog> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("device_no")
    private String deviceNo;


    private String  content;

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
     * 异常时间
     */
    @TableField("error_time")
    private Date errorTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
