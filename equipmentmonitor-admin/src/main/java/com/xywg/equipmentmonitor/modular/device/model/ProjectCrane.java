package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xywg.equipmentmonitor.core.model.BaseEntity;
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
@TableName("t_project_crane")
public class ProjectCrane extends BaseEntity<ProjectCrane> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 工程名称
     */
    @ApiModelProperty(value = "工程名称")
    @TableField("project_id")
    private Integer projectId;
    /**
     * 产权编号
     */
    @ApiModelProperty(value = "产权编号")
    private String identifier;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String specification;
    /**
     * 产权单位
     */
    @ApiModelProperty(value = "产权单位")
    private String owner;
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
     * 在线状态
     */
    @ApiModelProperty(value = "在线状态")
    @TableField("is_online")
    private Integer isOnline;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 位置
     */
    @ApiModelProperty(value = "位置")
    @TableField("place_point")
    private String placePoint;
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
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @TableField("device_no")
    private String deviceNo;
    /**
     * GPRS
     */
    @ApiModelProperty(value = "GPRS")
    @TableField("gprs")
    private Integer gprs;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name ;

    @TableField("need_dispatch")
    private Integer needDispatch;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
