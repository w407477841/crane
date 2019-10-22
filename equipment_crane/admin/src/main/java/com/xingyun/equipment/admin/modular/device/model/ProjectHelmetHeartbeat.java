package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xingyun.equipment.admin.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 安全帽在线离线状态切换履历表
 * </p>
 *
 * @author xss
 * @since 2018-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_helmet_heartbeat")
public class ProjectHelmetHeartbeat extends BaseEntity<ProjectHelmetHeartbeat> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 主表id
     */
    @ApiModelProperty(value = "主表id")
    @TableField("helmet_id")
    private Integer helmetId;
    /**
     * imei
     */
    @ApiModelProperty(value = "imei")
    private String imei;
    /**
     * 状态 0:离线,1在线
     */
    @ApiModelProperty(value = "状态 0:离线,1在线")
    private Integer status;
    @TableField("end_time")
    private Date endTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
