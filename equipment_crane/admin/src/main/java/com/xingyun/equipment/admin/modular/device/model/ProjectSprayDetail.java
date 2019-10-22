package com.xingyun.equipment.admin.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 */
@TableName("t_project_spray_detail")
@Data
public class ProjectSprayDetail extends BaseEntity<ProjectSprayDetail> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("spray_id")
    private Integer sprayId;

    @TableField("operation_type")
    private Integer operationType;


    @TableField("control_mode")
    private Integer controlMode;

    @TableField("device_no")
    private String deviceNo;


    @TableField("detail")
    private String detail;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
