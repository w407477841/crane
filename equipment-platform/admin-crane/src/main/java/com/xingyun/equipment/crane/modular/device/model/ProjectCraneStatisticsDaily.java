package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigInteger;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-06-19
 */
@Data
@TableName("t_project_crane_statistics_daily")
public class ProjectCraneStatisticsDaily extends Model<ProjectCraneStatisticsDaily> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 工程id
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 工程名称
     */
	@TableField("project_name")
	private String projectName;
    /**
     * 施工单位
     */
	@TableField("builder")
	private Integer builder;
    /**
     * 产权单位
     */
	@TableField("owner")
	private String owner;
    /**
     * 备案编号
     */
	@TableField("identifier")
	private String identifier;
    /**
     * 设备编号
     */
	@TableField("crane_no")
	private String craneNo;
    /**
     * 黑匣子编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 吊重次数
     */
	@TableField("lift_frequency")
	private Integer liftFrequency;
    /**
     * 40以下
     */
	private Integer percentage0;
    /**
     * 40-60
     */
	private Integer percentage40;
    /**
     * 60-80
     */
	private Integer percentage60;
    /**
     * 80-90
     */
	private Integer percentage80;
    /**
     * 90-110
     */
	private Integer percentage90;
    /**
     * 110-120
     */
	private Integer percentage110;
    /**
     * 120以上
     */
	private Integer percentage120;
    /**
     * 重量预警
     */
	@TableField("weight_warn")
	private Integer weightWarn;
    /**
     * 幅度预警
     */
	@TableField("range_warn")
	private Integer rangeWarn;
    /**
     * 高度预警
     */
	@TableField("limit_warn")
	private Integer limitWarn;
    /**
     * 力矩预警
     */
	@TableField("moment_warn")
	private Integer momentWarn;
    /**
     * 碰撞预警
     */
	@TableField("collision_warn")
	private Integer collisionWarn;
    /**
     * 重量报警
     */
	@TableField("weight_alarm")
	private Integer weightAlarm;
    /**
     * 幅度报警
     */
	@TableField("range_alarm")
	private Integer rangeAlarm;
    /**
     * 高度报警
     */
	@TableField("limit_alarm")
	private Integer limitAlarm;
    /**
     * 力矩报警
     */
	@TableField("moment_alarm")
	private Integer momentAlarm;
    /**
     * 碰撞报警
     */
	@TableField("collision_alarm")
	private Integer collisionAlarm;
    /**
     * 风速报警
     */
	@TableField("wind_speed_alarm")
	private Integer windSpeedAlarm;
    /**
     * 倾斜报警
     */
	@TableField("tilt_alarm")
	private Integer tiltAlarm;
    /**
     * 日期
     */
	@TableField("work_date")
	private Date workDate;

	/**
	 * 总运行时长
	 */
	private BigInteger timeSum;

	/**
	 * 在线状态
	 */
	private Integer isOnline;
	@TableField(exist = false)
	private Long dayAccount;

	@TableField(exist = false)
	private String specification;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneStatisticsDaily{" +
			"id=" + id +
			", projectId=" + projectId +
			", projectName=" + projectName +
			", builder=" + builder +
			", owner=" + owner +
			", identifier=" + identifier +
			", craneNo=" + craneNo +
			", deviceNo=" + deviceNo +
			", liftFrequency=" + liftFrequency +
			", percentage0=" + percentage0 +
			", percentage40=" + percentage40 +
			", percentage60=" + percentage60 +
			", percentage80=" + percentage80 +
			", percentage90=" + percentage90 +
			", percentage110=" + percentage110 +
			", percentage120=" + percentage120 +
			", weightWarn=" + weightWarn +
			", rangeWarn=" + rangeWarn +
			", limitWarn=" + limitWarn +
			", momentWarn=" + momentWarn +
			", collisionWarn=" + collisionWarn +
			", weightAlarm=" + weightAlarm +
			", rangeAlarm=" + rangeAlarm +
			", limitAlarm=" + limitAlarm +
			", momentAlarm=" + momentAlarm +
			", collisionAlarm=" + collisionAlarm +
			", windSpeedAlarm=" + windSpeedAlarm +
			", tiltAlarm=" + tiltAlarm +
			", workDate=" + workDate +
			"}";
	}
}
