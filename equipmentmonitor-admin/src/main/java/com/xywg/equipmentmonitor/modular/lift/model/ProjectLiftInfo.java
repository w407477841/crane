package com.xywg.equipmentmonitor.modular.lift.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectLiftInfo  {

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
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @TableField("device_no")
    private String deviceNo;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    @TableField("device_time")
    private Date deviceTime;
    /**
     * 产权编号
     */
    @ApiModelProperty(value = "产权编号")
    private String identifier;

    /**
     * 制造厂家
     */
    @ApiModelProperty(value = "制造厂家")
    private String manufactor;
    /**
     * 产权单位
     */
    @ApiModelProperty(value = "产权单位")
    private String owner;
    /**
     * 升降机id
     */
    @ApiModelProperty(value = "升降机id")
    @TableField("lift_id")
    private Integer liftId;


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
     * 前门状态
     */
    @ApiModelProperty(value = "前门状态")
    @TableField("front_door_status")
    private String frontDoorStatus;
    /**
     * 后门状态
     */
    @ApiModelProperty(value = "后门状态")
    @TableField("back_door_status")
    private String backDoorStatus;
    /**
     * 升降机状态
     */
    @ApiModelProperty(value = "升降机状态")
    private String status;
    



}
