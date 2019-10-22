package com.xingyun.equipment.admin.modular.infromation.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xingyun.equipment.admin.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;

import com.baomidou.mybatisplus.annotations.TableName;



import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_target_set_crane")
public class ProjectTargetSetCrane extends BaseEntity<ProjectTargetSetCrane> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String specification;
    /**
     * 制造厂家
     */
    @ApiModelProperty(value = "制造厂家")
    private String manufactor;
    /**
     * 最大幅度
     */
    @ApiModelProperty(value = "最大幅度")
    @TableField("max_range")
    private BigDecimal maxRange;
    /**
     * 最大载重量
     */
    @ApiModelProperty(value = "最大载重量")
    @TableField("max_weight")
    private BigDecimal maxWeight;
    /**
     * 标准高度
     */
    @ApiModelProperty(value = "标准高度")
    @TableField("standard_height")
    private BigDecimal standardHeight;
    /**
     * 额定力矩
     */
    @ApiModelProperty(value = "额定力矩")
    @TableField("fix_moment")
    private BigDecimal fixMoment;
    /**
     * 风速
     */
    @ApiModelProperty(value = "风速")
    @TableField("wind_speed")
    private BigDecimal windSpeed;
    /**
     * 倾角
     */
    @ApiModelProperty(value = "倾角")
    @TableField("tilt_angle")
    private BigDecimal tiltAngle;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comments;
    /**
     * 组织结构
     */
    @ApiModelProperty(value = "组织结构")
    @TableField(value = "org_id",fill = FieldFill.INSERT)
    private Integer orgId;
    /**
     * 占用
     */
    @ApiModelProperty(value = "占用")
    @TableField("call_times")
    private Integer callTimes;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
