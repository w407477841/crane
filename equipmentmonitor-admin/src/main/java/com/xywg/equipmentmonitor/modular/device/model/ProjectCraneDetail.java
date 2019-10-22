package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xywg.equipmentmonitor.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
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
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_crane_detail")
public class ProjectCraneDetail extends BaseEntity<ProjectCraneDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 塔吊id
     */
    @ApiModelProperty(value = "塔吊id")
    @TableField("crane_id")
    private Integer craneId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @TableField("device_no")
    private String deviceNo;
    /**
     * 司机
     */
    @ApiModelProperty(value = "司机")
    private Integer driver;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    @TableField("device_time")
    private Date deviceTime;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private BigDecimal weight;
    /**
     * 力矩
     */
    @ApiModelProperty(value = "力矩")
    private BigDecimal moment;
    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private BigDecimal height;
    /**
     * 幅度
     */
    @ApiModelProperty(value = "幅度")
    private BigDecimal range;
    /**
     * 力矩百分比
     */
    @ApiModelProperty(value = "力矩百分比")
    @TableField("moment_percentage")
    private BigDecimal momentPercentage;
    /**
     * 回转角度
     */
    @ApiModelProperty(value = "回转角度")
    @TableField("rotary_angle")
    private BigDecimal rotaryAngle;
    /**
     * 倾斜角度
     */
    @ApiModelProperty(value = "倾斜角度")
    @TableField("tilt_angle")
    private BigDecimal tiltAngle;
    /**
     * 风速
     */
    @ApiModelProperty(value = "风速")
    @TableField("wind_speed")
    private BigDecimal windSpeed;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key1;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key2;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key3;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key4;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key5;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key6;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key7;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key8;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key9;
    /**
     * 预留的键值
     */
    @ApiModelProperty(value = "预留的键值")
    private String key10;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
