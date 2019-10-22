package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.core.BaseEntity;
import lombok.Data;


/**
 * <p>
 * 健康信息(采集数据)
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@Data
@TableName("t_project_helmet_health_detail")
public class ProjectHelmetHealthDetail extends BaseEntity<ProjectHelmetHealthDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	@TableField("helmet_id")
	private Integer helmetId;
    /**
     * imei
     */
	private String imei;
    /**
     * 心率
     */
	@TableField("heart_rate")
	private Integer heartRate;
    /**
     * 血氧
     */
	@TableField("blood_oxygen")
	private Integer bloodOxygen;
    /**
     * 体温
     */
	private BigDecimal temperature;
    /**
     * 姿态,正常:00,跌倒:02
     */
	private String sixAxis;
    /**
     * 采集时间
     */
	@TableField("collect_time")
	private Date collectTime;
    /**
     * 备注
     */
	private String comments;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	/**
     * 删除标志
     */
//	@TableField("is_del")
//	private Integer isDel;
//    /**
//     * 创建日期
//     */
//	@TableField("create_time")
//	private Date createTime;
//    /**
//     * 创建人
//     */
//	@TableField("create_user")
//	private Integer createUser;
//    /**
//     * 修改日期
//     */
//	@TableField("modify_time")
//	private Date modifyTime;
//    /**
//     * 修改人
//     */
//	@TableField("modify_user")
//	private Integer modifyUser;


//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public Integer getHelmetId() {
//		return helmetId;
//	}
//
//	public void setHelmetId(Integer helmetId) {
//		this.helmetId = helmetId;
//	}
//
//	public String getImei() {
//		return imei;
//	}
//
//	public void setImei(String imei) {
//		this.imei = imei;
//	}
//
//	public Integer getHeartRate() {
//		return heartRate;
//	}
//
//	public void setHeartRate(Integer heartRate) {
//		this.heartRate = heartRate;
//	}
//
//	public Integer getBloodOxygen() {
//		return bloodOxygen;
//	}
//
//	public void setBloodOxygen(Integer bloodOxygen) {
//		this.bloodOxygen = bloodOxygen;
//	}
//
//	public BigDecimal getTemperature() {
//		return temperature;
//	}
//
//	public void setTemperature(BigDecimal temperature) {
//		this.temperature = temperature;
//	}
//
//	public String getSixAxis() {
//		return sixAxis;
//	}
//
//	public void setSixAxis(String sixAxis) {
//		this.sixAxis = sixAxis;
//	}
//
//	public Date getCollectTime() {
//		return collectTime;
//	}
//
//	public void setCollectTime(Date collectTime) {
//		this.collectTime = collectTime;
//	}
//
//	public String getComments() {
//		return comments;
//	}
//
//	public void setComments(String comments) {
//		this.comments = comments;
//	}
//
//	public Integer getIsDel() {
//		return isDel;
//	}
//
//	public void setIsDel(Integer isDel) {
//		this.isDel = isDel;
//	}
//
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
//
//	public Integer getCreateUser() {
//		return createUser;
//	}
//
//	public void setCreateUser(Integer createUser) {
//		this.createUser = createUser;
//	}
//
//	public Date getModifyTime() {
//		return modifyTime;
//	}
//
//	public void setModifyTime(Date modifyTime) {
//		this.modifyTime = modifyTime;
//	}
//
//	public Integer getModifyUser() {
//		return modifyUser;
//	}
//
//	public void setModifyUser(Integer modifyUser) {
//		this.modifyUser = modifyUser;
//	}
//
//
//	@Override
//	protected Serializable pkVal() {
//		return this.id;
//	}
//
//	@Override
//	public String toString() {
//		return "ProjectHelmetHealthDetail{" +
//			"id=" + id +
//			", helmetId=" + helmetId +
//			", imei=" + imei +
//			", heartRate=" + heartRate +
//			", bloodOxygen=" + bloodOxygen +
//			", temperature=" + temperature +
//			", sixAxis=" + sixAxis +
//			", collectTime=" + collectTime +
//			", comments=" + comments +
//			", isDel=" + isDel +
//			", createTime=" + createTime +
//			", createUser=" + createUser +
//			", modifyTime=" + modifyTime +
//			", modifyUser=" + modifyUser +
//			"}";
//	}
}
