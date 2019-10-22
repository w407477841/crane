package com.xingyun.equipment.admin.modular.remotesetting.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author hjy
 * 设备异常日志短信推送履历实体
 */
@TableName("t_project_message_device_error")
@Data
public class ProjectMessageDeviceError extends BaseEntity<ProjectMessageDeviceError> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("user_ids")
    private String userIds;

    @TableField("device_no")
    private String deviceNo;


    private String  content;

    /**
     * 项目ID
     **/
    @TableField("project_id")
    private Integer projectId;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;


    @TableField(exist = false)
    private String userNames;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
