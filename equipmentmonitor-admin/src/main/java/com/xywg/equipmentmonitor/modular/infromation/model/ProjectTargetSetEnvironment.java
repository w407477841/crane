package com.xywg.equipmentmonitor.modular.infromation.model;

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
 * @since 2018-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_target_set_environment")
public class ProjectTargetSetEnvironment extends BaseEntity<ProjectTargetSetEnvironment> {

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
     * PM10
     */
    @ApiModelProperty(value = "PM10")
    private BigDecimal pm10;
    /**
     * PM2.5
     */
    @ApiModelProperty(value = "PM2.5")
    private BigDecimal pm25;
    /**
     * 噪音（分贝）
     */
    @ApiModelProperty(value = "噪音（分贝）")
    private BigDecimal noise;
    /**
     * 风速
     */
    @ApiModelProperty(value = "风速")
    @TableField("wind_speed")
    private BigDecimal windSpeed;
    /**
     * tsp
     */
    @ApiModelProperty(value = "tsp")
    private BigDecimal tsp;
    /**
     * 温度（C) 最大
     */
    @ApiModelProperty(value = "温度（C) 最大")
    @TableField("temperature_max")
    private BigDecimal temperatureMax;
    /**
     * 温度（C) 最小
     */
    @ApiModelProperty(value = "温度（C) 最小")
    @TableField("temperature_min")
    private BigDecimal temperatureMin;
    /**
     * 湿度(%rh)最大
     */
    @ApiModelProperty(value = "湿度(%rh)最大")
    @TableField("humidity_max")
    private BigDecimal humidityMax;
    /**
     * 湿度(%rh)最小
     */
    @ApiModelProperty(value = "湿度(%rh)最小")
    @TableField("humidity_min")
    private BigDecimal humidityMin;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comments;
    /**
     * 风力
     */
    @ApiModelProperty(value = "风力")
    @TableField("wind_force")
    private String windForce;
    /**
     * 大气压
     */
    @ApiModelProperty(value = "大气压")
    private BigDecimal atmospheric;
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
