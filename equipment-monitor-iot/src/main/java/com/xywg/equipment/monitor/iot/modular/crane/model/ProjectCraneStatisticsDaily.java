package com.xywg.equipment.monitor.iot.modular.crane.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("t_project_crane_statistics_daily")
@Data
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
	private Integer builder;
    /**
     * 产权单位
     */
	private String owner;
    /**
     * 备案编号
     */
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
	@TableField("alarm_info")
	private String alarmInfo;

	private  Integer percentage0;
	private  Integer percentage40;
	private  Integer percentage60;
	private  Integer percentage80;
	private  Integer percentage90;
	private  Integer percentage110;
	private  Integer percentage120;

	public ProjectCraneStatisticsDaily() {
	}

	public ProjectCraneStatisticsDaily(Integer projectId, String projectName, Integer builder, String owner, String identifier, String craneNo, String deviceNo) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.builder = builder;
		this.owner = owner;
		this.identifier = identifier;
		this.craneNo = craneNo;
		this.deviceNo = deviceNo;
		this.liftFrequency = 0;
		this.weightWarn = 0;
		this.rangeWarn = 0;
		this.limitWarn = 0;
		this.momentWarn = 0;
		this.collisionWarn = 0;
		this.weightAlarm = 0;
		this.rangeAlarm = 0;
		this.limitAlarm = 0;
		this.momentAlarm = 0;
		this.collisionAlarm = 0;
		this.windSpeedAlarm = 0;
		this.tiltAlarm = 0;
		this.percentage0 = 0;
		this.percentage40 = 0;
		this.percentage60 = 0;
		this.percentage80 = 0;
		this.percentage90 = 0;
		this.percentage110 = 0;
		this.percentage120 = 0;
	}

	public static ProjectCraneStatisticsDaily addAlarm(int alarmId,ProjectCraneStatisticsDaily daily){
			switch (alarmId){
				case 1:
					daily.setWeightAlarm(1+daily.getWeightAlarm());
					break;
				case 2:
					daily.setWeightWarn(1+daily.getWeightWarn());
					break;
				case 3:
					daily.setRangeAlarm(1+daily.getRangeAlarm());
					break;
				case 4:
					daily.setRangeWarn(1+daily.getRangeWarn());
					break;
				case 5:
					daily.setRangeAlarm(1+daily.getRangeAlarm());
					break;
				case 6:
					daily.setRangeWarn(1+daily.getRangeWarn());
					break;
				case 7:
					daily.setLimitAlarm(1+daily.getLimitAlarm());
					break;
				case 8:
					daily.setLimitWarn(1+daily.getLimitWarn());
					break;
				case 9:
					daily.setMomentAlarm(1+daily.getMomentAlarm());
					break;
				case 10:
					daily.setMomentWarn(1+daily.getMomentWarn());
					break;
				case 11:
					daily.setCollisionAlarm(1+daily.getCollisionAlarm());
					break;
				case 12:
					daily.setCollisionWarn(1+daily.getCollisionWarn());
					break;
				case 13:
					daily.setCollisionAlarm(1+daily.getCollisionAlarm());
					break;
				case 14:
					daily.setCollisionWarn(1+daily.getCollisionWarn());
					break;
				case 15:
					daily.setWindSpeedAlarm(1+daily.getWindSpeedAlarm());
					break;
				case 16:
					daily.setTiltAlarm(1+daily.getTiltAlarm());
					break;
			}
		return daily;
	}

	public static ProjectCraneStatisticsDaily addPercentage(Double momentPercentage,ProjectCraneStatisticsDaily daily){

		if(momentPercentage<40){
			daily.setPercentage0(1+daily.getPercentage0());
		}else if(momentPercentage>=40&&momentPercentage<60){
			daily.setPercentage40(1+daily.getPercentage40());
		}else if(momentPercentage>=60&&momentPercentage<80){
			daily.setPercentage60(1+daily.getPercentage60());
		}else if(momentPercentage>=80&&momentPercentage<90){
			daily.setPercentage80(1+daily.getPercentage80());
		}else if(momentPercentage>=90&&momentPercentage<110){
			daily.setPercentage90(1+daily.getPercentage90());
		}else if(momentPercentage>=110&&momentPercentage<120){
			daily.setPercentage110(1+daily.getPercentage110());
		}else if(momentPercentage>=120){
			daily.setPercentage120(1+daily.getPercentage120());
		}

		return daily;
	}


	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
