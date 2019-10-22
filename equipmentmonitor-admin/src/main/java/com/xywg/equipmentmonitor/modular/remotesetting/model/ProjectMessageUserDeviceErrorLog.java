package com.xywg.equipmentmonitor.modular.remotesetting.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import com.xywg.equipmentmonitor.modular.system.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author hjy
 */
@TableName("t_project_message_user_device_error_log")
@Data
public class ProjectMessageUserDeviceErrorLog extends BaseEntity<ProjectMessageUserDeviceErrorLog> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**

     /**
     * 1塔吊、2升降机,3扬尘、
     **/
    @TableField("device_type")
    private Integer deviceType;
    /**
     * 发送人
     **/
    @TableField("user_ids")
    private String userIds;

    @TableField(exist = false)
    private String userNames;

    @TableField(exist = false)
    private List<String> userIdList;

    @TableField(exist = false)
    private List<User> userList;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
