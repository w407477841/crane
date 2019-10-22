package com.xingyun.equipment.timer.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	private  Integer percentage0;
	private  Integer percentage40;
	private  Integer percentage60;
	private  Integer percentage80;
	private  Integer percentage90;
	private  Integer percentage110;
	private  Integer percentage120;


	public ProjectCraneStatisticsDaily() {
	}

	public ProjectCraneStatisticsDaily(Integer projectId, String projectName, Integer builder, String owner, String identifier, String craneNo, String deviceNo,Date workDate) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.builder = builder;
		this.owner = owner;
		this.identifier = identifier;
		this.craneNo = craneNo;
		this.deviceNo = deviceNo;
		this.workDate = workDate;
		liftFrequency = 0;
		weightWarn = 0;
		rangeWarn = 0;
		limitWarn = 0;
		momentWarn = 0;
		collisionWarn = 0;
		weightAlarm = 0;
		rangeAlarm= 0 ;
		limitAlarm = 0;
		momentAlarm= 0;
		collisionAlarm =0;
		windSpeedAlarm =0;
		tiltAlarm = 0;
		 percentage0 =0;
		percentage40 =0;
		percentage60 =0 ;
		 percentage80 =0;
		 percentage90 =0;
		 percentage110 =0;
		 percentage120 =0;
	}

	public static ProjectCraneStatisticsDaily packageAlarmAmout(ProjectCraneStatisticsDaily daily,List< Map<String,Integer>> alarmGroup){
		if(alarmGroup == null || alarmGroup.size() == 0) {return daily;}


		for (Map<String,Integer> map:
				alarmGroup) {
		Integer  alarmId = (Integer) map.get("alarm_id");
		if(alarmId!=null){
			switch (alarmId.intValue()){
				case 1:
					daily.setWeightAlarm((Integer) map.get("amount"));
					break;
				case 2:
					daily.setWeightWarn((Integer) map.get("amount"));
					break;
				case 3:
					//有幅度向内向外之分，需要累加
					daily.setRangeAlarm(daily.getRangeAlarm()+(Integer) map.get("amount"));
					break;
				case 4:
					//有幅度向内向外之分，需要累加
					daily.setRangeWarn(daily.getRangeWarn()+(Integer) map.get("amount"));
					break;
				case 5:
					//有幅度向内向外之分，需要累加
					daily.setRangeAlarm(daily.getRangeAlarm()+(Integer) map.get("amount"));
					break;
				case 6:
					//有幅度向内向外之分，需要累加
					daily.setRangeWarn(daily.getRangeWarn()+(Integer) map.get("amount"));
					break;
				case 7:
					daily.setLimitAlarm((Integer) map.get("amount"));
					break;
				case 8:
					daily.setLimitWarn((Integer) map.get("amount"));
					break;
				case 9:
					daily.setMomentAlarm((Integer) map.get("amount"));
					break;
				case 10:
					daily.setMomentWarn((Integer) map.get("amount"));
					break;
				case 11:
					//有单机 多机之分，需要累加
					daily.setCollisionAlarm(daily.getCollisionAlarm()+(Integer) map.get("amount"));
					break;
				case 12:
					//有单机 多机之分，需要累加
					daily.setCollisionWarn(daily.getCollisionWarn()+(Integer) map.get("amount"));
					break;
				case 13:
					//有单机 多机之分，需要累加
					daily.setCollisionAlarm(daily.getCollisionAlarm()+(Integer) map.get("amount"));
					break;
				case 14:
					//有单机 多机之分，需要累加
					daily.setCollisionWarn(daily.getCollisionWarn()+(Integer) map.get("amount"));
					break;
				case 15:
					daily.setWindSpeedAlarm((Integer) map.get("amount"));
					break;
				case 16:
					daily.setTiltAlarm((Integer) map.get("amount"));
					break;
			}
		}
	}

		return daily;
	}

	/**
	 * 组装工作循环数据
	 * @param daily
	 * @param cyclicWorkDurationList
	 * @return
	 */
	public static ProjectCraneStatisticsDaily packageCyclic(ProjectCraneStatisticsDaily daily,List<ProjectCraneCyclicWorkDuration> cyclicWorkDurationList){

		int percentage0 = 0;
		int percentage40 = 0;
		int percentage60 = 0;
		int percentage80 = 0;
		int percentage90 = 0;
		int percentage110 = 0;
		int percentage120 = 0;
		if(cyclicWorkDurationList == null || cyclicWorkDurationList.size() == 0) {
			daily.setLiftFrequency(0);
		}else{
			daily.setLiftFrequency(cyclicWorkDurationList.size());
			for(ProjectCraneCyclicWorkDuration workDuration:cyclicWorkDurationList){
				double momentPercentage =  workDuration.getMomentPercentage().doubleValue();
				if(momentPercentage<40){
					percentage0+=1;
				}else if(momentPercentage>=40&&momentPercentage<60){
					percentage40+=1;
				}else if(momentPercentage>=60&&momentPercentage<80){
					percentage60+=1;
				}else if(momentPercentage>=80&&momentPercentage<90){
					percentage80+=1;
				}else if(momentPercentage>=90&&momentPercentage<110){
					percentage90+=1;
				}else if(momentPercentage>=110&&momentPercentage<120){
					percentage110+=1;
				}else if(momentPercentage>=120){
					percentage120+=1;
				}
			}
		}

		daily.setPercentage0(percentage0);
		daily.setPercentage40(percentage40);
		daily.setPercentage60(percentage60);
		daily.setPercentage80(percentage80);
		daily.setPercentage90(percentage90);
		daily.setPercentage110(percentage110);
		daily.setPercentage120(percentage120);
		return daily;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
