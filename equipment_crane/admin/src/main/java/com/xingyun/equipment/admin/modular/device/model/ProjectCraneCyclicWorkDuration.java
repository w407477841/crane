package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-06-20
 */
@TableName("t_project_crane_cyclic_work_duration")
@Data
public class ProjectCraneCyclicWorkDuration extends Model<ProjectCraneCyclicWorkDuration> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@ApiModelProperty(value = "ID")
	@TableId(value = "id", type = IdType.AUTO)
	private String id;
    /**
     * 主表id
     */
	@TableField("crane_id")
	private Integer craneId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 起始高度
     */
	@TableField("begin_height")
	private BigDecimal beginHeight;
    /**
     * 终止高度
     */
	@TableField("end_height")
	private BigDecimal endHeight;
    /**
     * 起始幅度
     */
	@TableField("begin_range")
	private BigDecimal beginRange;
    /**
     * 终止幅度
     */
	@TableField("end_range")
	private BigDecimal endRange;
    /**
     * 起始力矩
     */
	@TableField("begin_moment")
	private BigDecimal beginMoment;
    /**
     * 起始重量
     */
	@TableField("weight")
	private BigDecimal weight;
    /**
     * 安全吊重
     */
	@TableField("safety_weight")
	private BigDecimal safetyWeight;
    /**
     * 风速
     */
	@TableField("wind_speed")
	private BigDecimal windSpeed;
    /**
     * 起始角度
     */
	@TableField("begin_angle")
	private BigDecimal beginAngle;
    /**
     * 终止角度
     */
	@TableField("end_angle")
	private BigDecimal endAngle;
    /**
     * 力矩百分比
     */
	@TableField("moment_percentage")
	private BigDecimal momentPercentage;
    /**
     * 吊绳倍率
     */
	@TableField("multiple_rate")
	private BigDecimal multipleRate;
    /**
     * 开始日期
     */
	@TableField("begin_time")
	private Date beginTime;
    /**
     * 结束日期
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 开始吊钩吊绳长度
	 */
	@TableField("begin_rope_length")
	private Double beginRopeLength;
	/**
	 * 结束吊钩吊绳长度
	 */
	@TableField("end_rope_length")
	private Double endRopeLength;
	@TableField("alarm_info")
	private String alarmInfo;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneCyclicWorkDuration{" +
			"id=" + id +
			", craneId=" + craneId +
			", deviceNo=" + deviceNo +
			", beginHeight=" + beginHeight +
			", endHeight=" + endHeight +
			", beginRange=" + beginRange +
			", endRange=" + endRange +
			", beginMoment=" + beginMoment +
			", weight=" + weight +
			", safetyWeight=" + safetyWeight +
			", windSpeed=" + windSpeed +
			", beginAngle=" + beginAngle +
			", endAngle=" + endAngle +
			", momentPercentage=" + momentPercentage +
			", multipleRate=" + multipleRate +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", createTime=" + createTime +
			"}";
	}
}
