package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_lift_detail")
public class ProjectLiftDetail extends BaseEntity<ProjectLiftDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 升降机id
     */
    @ApiModelProperty(value = "升降机id")
    @TableField("lift_id")
    private Integer liftId;
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
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private BigDecimal weight;
    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private BigDecimal height;
    /**
     * 速度
     */
    @ApiModelProperty(value = "速度")
    private BigDecimal speed;
    /**
     * 人数
     */
    @ApiModelProperty(value = "人数")
    private Integer people;
    /**
     * 前门状态
     */
    @ApiModelProperty(value = "前门状态")
    @TableField("front_door_status")
    private Integer frontDoorStatus;
    /**
     * 后门状态
     */
    @ApiModelProperty(value = "后门状态")
    @TableField("back_door_status")
    private Integer backDoorStatus;
    /**
     * 升降机状态
     */
    @ApiModelProperty(value = "升降机状态")
    private Integer status;
    /**
     * 倾角
     */
    @ApiModelProperty(value = "倾角")
    @TableField("tilt_angle")
    private BigDecimal tiltAngle;
    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private Integer floor;
    /**
     * 启动楼层
     */
    @ApiModelProperty(value = "启动楼层")
    @TableField("floor_start")
    private Integer floorStart;
    /**
     * 停止楼层
     */
    @ApiModelProperty(value = "停止楼层")
    @TableField("floor_end")
    private Integer floorEnd;
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
