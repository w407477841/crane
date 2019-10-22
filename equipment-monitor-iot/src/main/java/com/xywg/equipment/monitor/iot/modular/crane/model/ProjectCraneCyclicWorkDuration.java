package com.xywg.equipment.monitor.iot.modular.crane.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-06-17
 */
@TableName("t_project_crane_cyclic_work_duration")
@Data
public class ProjectCraneCyclicWorkDuration extends Model<ProjectCraneCyclicWorkDuration> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
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
	private Double beginHeight;
    /**
     * 终止高度
     */
	@TableField("end_height")
	private Double endHeight;
    /**
     * 起始幅度
     */
	@TableField("begin_range")
	private Double beginRange;
    /**
     * 终止幅度
     */
	@TableField("end_range")
	private Double endRange;
    /**
     * 起始力矩
     */
	@TableField("begin_moment")
	private Double beginMoment;
    /**
     * 起始重量
     */
	private Double weight;
    /**
     * 安全吊重
     */
	@TableField("safety_weight")
	private Double safetyWeight;
    /**
     * 风速
     */
	@TableField("wind_speed")
	private Double windSpeed;
    /**
     * 起始角度
     */
	@TableField("begin_angle")
	private Double beginAngle;
    /**
     * 终止角度
     */
	@TableField("end_angle")
	private Double endAngle;
    /**
     * 力矩百分比
     */
	@TableField("moment_percentage")
	private Double momentPercentage;
	@TableField("alarm_info")
	private String alarmInfo;
    /**
     * 吊绳倍率
     */
	@TableField("multiple_rate")
	private Double multipleRate;
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


	public ProjectCraneCyclicWorkDuration(Integer craneId, String deviceNo, Double beginHeight, Double endHeight, Double beginRange, Double endRange, Double beginMoment, Double weight, Double safetyWeight, Double windSpeed, Double beginAngle, Double endAngle, Double momentPercentage, Double multipleRate, String alarmInfo, Date beginTime, Date endTime, Date createTime,
										  Double beginRopeLength,Double endRopeLength) {
		this.craneId = craneId;
		this.deviceNo = deviceNo;
		this.beginHeight = beginHeight;
		this.endHeight = endHeight;
		this.beginRange = beginRange;
		this.endRange = endRange;
		this.beginMoment = beginMoment;
		this.weight = weight;
		this.safetyWeight = safetyWeight;
		this.windSpeed = windSpeed;
		this.beginAngle = beginAngle;
		this.endAngle = endAngle;
		this.momentPercentage = momentPercentage;
		this.multipleRate = multipleRate;
		this.alarmInfo = alarmInfo;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.createTime = createTime;
		this.beginRopeLength=beginRopeLength;
		this.beginRopeLength=beginRopeLength;
	}

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
			"}";
	}
}
