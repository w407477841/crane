package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 升降机短信
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_message_lift")
public class ProjectMessageLift extends BaseEntity<ProjectMessageLift> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 短信模板编号
     */
    @ApiModelProperty(value = "短信模板编号")
    private Integer model;
    /**
     * 短信标题
     */
    @ApiModelProperty(value = "短信标题")
    private String title;
    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    private Date sendTime;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 指定人
     */
    @ApiModelProperty(value = "指定人")
    @TableField("related_user")
    private String relatedUser;
    /**
     * 组织结构
     */
    @ApiModelProperty(value = "组织结构")
    @TableField("org_id")
    private Integer orgId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
