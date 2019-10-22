package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@TableName("t_project_crane_data_model")
public class ProjectCraneDataModel extends Model<ProjectCraneDataModel> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 重量
     */
	private String weight;
    /**
     * 力矩
     */
	private String moment;
	@TableField("moment_percentage")
	private String momentPercentage;
    /**
     * 高度
     */
	private String height;
    /**
     * 幅度
     */
	private String rang;
    /**
     * 回转角度
     */
	@TableField("rotary_angle")
	private String rotaryAngle;
    /**
     * 风速
     */
	@TableField("wind_speed")
	private String windSpeed;
	@TableField("tilt_angle")
	private String tiltAngle;
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
    /**
     * 1：带标志位 2：不带标志位
     */
	private Integer flag;
	@TableField("device_no")
	private String deviceNo;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMoment() {
		return moment;
	}

	public void setMoment(String moment) {
		this.moment = moment;
	}

	public String getMomentPercentage() {
		return momentPercentage;
	}

	public void setMomentPercentage(String momentPercentage) {
		this.momentPercentage = momentPercentage;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getRang() {
		return rang;
	}

	public void setRang(String rang) {
		this.rang = rang;
	}

	public String getRotaryAngle() {
		return rotaryAngle;
	}

	public void setRotaryAngle(String rotaryAngle) {
		this.rotaryAngle = rotaryAngle;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getTiltAngle() {
		return tiltAngle;
	}

	public void setTiltAngle(String tiltAngle) {
		this.tiltAngle = tiltAngle;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(Integer modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneDataModel{" +
			"id=" + id +
			", weight=" + weight +
			", moment=" + moment +
			", momentPercentage=" + momentPercentage +
			", height=" + height +
			", rang=" + rang +
			", rotaryAngle=" + rotaryAngle +
			", windSpeed=" + windSpeed +
			", tiltAngle=" + tiltAngle +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			", flag=" + flag +
			", deviceNo=" + deviceNo +
			"}";
	}
}
