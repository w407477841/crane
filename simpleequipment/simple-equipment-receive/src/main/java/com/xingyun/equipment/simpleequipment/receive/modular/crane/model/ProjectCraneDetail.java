package com.xingyun.equipment.simpleequipment.receive.modular.crane.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf  塔吊 实体
 * @since 2018-08-20
 */
@TableName("t_project_crane_detail")
@Data
public class ProjectCraneDetail extends Model<ProjectCraneDetail> {


	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 塔吊id
	 */
	@TableField("crane_id")
	private Integer craneId;
	/**
	 * 设备编号
	 */
	@TableField("device_no")
	private String deviceNo;
	/**
	 * 司机
	 */
	private Integer driver;
	/**
	 * 时间
	 */
	@TableField("device_time")
	private Date deviceTime;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 重量
	 */
	private Double weight;
	@TableField("safety_weight")
	private Double safetyWeight;
	/**
	 * 力矩
	 */
	private Double moment;
	@TableField("safety_moment")
	private Double safetyMoment;
	/**
	 * 高度
	 */
	private Double height;
	/**
	 * 幅度
	 */
	private Double range;
	/**
	 * 力矩百分比
	 */
	@TableField("moment_percentage")
	private Double momentPercentage;
	/**
	 * 回转角度
	 */
	@TableField("rotary_angle")
	private Double rotaryAngle;
	/**
	 * 倾斜角度
	 */
	@TableField("tilt_angle")
	private Double tiltAngle;
	/**
	 * 风速
	 */
	@TableField("wind_speed")
	private Double windSpeed;
	/**
	 * 删除标志
	 */
	@TableField("is_del")
	private Integer isDel;
	/**
	 * 创建日期
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField("create_user")
	private Integer createUser;
	/**
	 * 修改日期
	 */
	@TableField("modify_time")
	private Date modifyTime;
	/**
	 * 修改人
	 */
	@TableField("modify_user")
	private Integer modifyUser;

	@TableField(exist = false)
	private String alarmInfo;

	@TableField("crane_height")
	private Double craneHeight;
	/**
	 * 吊钩距离力臂距离
	 */
	@TableField("rope_length")
	private Double ropeLength;

	public ProjectCraneDetail() {
	}

	public ProjectCraneDetail(Integer craneId, String deviceNo, Integer driver, Date deviceTime, Integer status, Double weight, Double safetyWeight, Double moment, Double safetyMoment, Double height, Double range, Double momentPercentage, Double rotaryAngle, Double tiltAngle, Double windSpeed, Integer isDel,
							  Date createTime, Double craneHeight, Double ropeLength) {
		this.craneId = craneId;
		this.deviceNo = deviceNo;
		this.driver = driver;
		this.deviceTime = deviceTime;
		this.status = status;
		this.weight = weight;
		this.moment = moment;
		this.height = height;
		this.range = range;
		this.momentPercentage = momentPercentage;
		this.rotaryAngle = rotaryAngle;
		this.tiltAngle = tiltAngle;
		this.windSpeed = windSpeed;
		this.isDel = isDel;
		this.createTime = createTime;
		this.safetyMoment = safetyMoment;
		this.safetyWeight = safetyWeight;
		this.craneHeight=craneHeight;
		this.ropeLength=ropeLength;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneDetail{" +
				"id=" + id +
				", craneId=" + craneId +
				", deviceNo=" + deviceNo +
				", driver=" + driver +
				", deviceTime=" + deviceTime +
				", status=" + status +
				", weight=" + weight +
				", moment=" + moment +
				", height=" + height +
				", range=" + range +
				", momentPercentage=" + momentPercentage +
				", rotaryAngle=" + rotaryAngle +
				", tiltAngle=" + tiltAngle +
				", windSpeed=" + windSpeed +
				", isDel=" + isDel +
				", createTime=" + createTime +
				", createUser=" + createUser +
				", modifyTime=" + modifyTime +
				", modifyUser=" + modifyUser +
				"}";
	}
}
