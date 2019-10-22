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
 * @since 2018-08-22
 */
@TableName("t_project_lift_detail")
public class ProjectLiftDetail extends Model<ProjectLiftDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 升降机id
     */
	@TableField("lift_id")
	private Integer liftId;
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
     * 重量
     */
	private Double weight;
    /**
     * 高度
     */
	private Double height;
    /**
     * 速度
     */
	private Double speed;
    /**
     * 人数
     */
	private Integer people;
    /**
     * 前门状态
     */
	@TableField("front_door_status")
	private Integer frontDoorStatus;
    /**
     * 后门状态
     */
	@TableField("back_door_status")
	private Integer backDoorStatus;
    /**
     * 升降机状态
     */
	private Integer status;
    /**
     * 倾角
     */
	@TableField("tilt_angle")
	private Double tiltAngle;
    /**
     * 楼层
     */
	private Integer floor;
    /**
     * 启动楼层
     */
	@TableField("floor_start")
	private Integer floorStart;
    /**
     * 停止楼层
     */
	@TableField("floor_end")
	private Integer floorEnd;
    /**
     * 预留的键值
     */
	private String key1;
    /**
     * 预留的键值
     */
	private String key2;
    /**
     * 预留的键值
     */
	private String key3;
    /**
     * 预留的键值
     */
	private String key4;
    /**
     * 预留的键值
     */
	private String key5;
    /**
     * 预留的键值
     */
	private String key6;
    /**
     * 预留的键值
     */
	private String key7;
    /**
     * 预留的键值
     */
	private String key8;
    /**
     * 预留的键值
     */
	private String key9;
    /**
     * 预留的键值
     */
	private String key10;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLiftId() {
		return liftId;
	}

	public void setLiftId(Integer liftId) {
		this.liftId = liftId;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public Integer getDriver() {
		return driver;
	}

	public void setDriver(Integer driver) {
		this.driver = driver;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}



	public Integer getPeople() {
		return people;
	}

	public void setPeople(Integer people) {
		this.people = people;
	}

	public Integer getFrontDoorStatus() {
		return frontDoorStatus;
	}

	public void setFrontDoorStatus(Integer frontDoorStatus) {
		this.frontDoorStatus = frontDoorStatus;
	}

	public Integer getBackDoorStatus() {
		return backDoorStatus;
	}

	public void setBackDoorStatus(Integer backDoorStatus) {
		this.backDoorStatus = backDoorStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getTiltAngle() {
		return tiltAngle;
	}

	public void setTiltAngle(Double tiltAngle) {
		this.tiltAngle = tiltAngle;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getFloorStart() {
		return floorStart;
	}

	public void setFloorStart(Integer floorStart) {
		this.floorStart = floorStart;
	}

	public Integer getFloorEnd() {
		return floorEnd;
	}

	public void setFloorEnd(Integer floorEnd) {
		this.floorEnd = floorEnd;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		this.key3 = key3;
	}

	public String getKey4() {
		return key4;
	}

	public void setKey4(String key4) {
		this.key4 = key4;
	}

	public String getKey5() {
		return key5;
	}

	public void setKey5(String key5) {
		this.key5 = key5;
	}

	public String getKey6() {
		return key6;
	}

	public void setKey6(String key6) {
		this.key6 = key6;
	}

	public String getKey7() {
		return key7;
	}

	public void setKey7(String key7) {
		this.key7 = key7;
	}

	public String getKey8() {
		return key8;
	}

	public void setKey8(String key8) {
		this.key8 = key8;
	}

	public String getKey9() {
		return key9;
	}

	public void setKey9(String key9) {
		this.key9 = key9;
	}

	public String getKey10() {
		return key10;
	}

	public void setKey10(String key10) {
		this.key10 = key10;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectLiftDetail{" +
			"id=" + id +
			", liftId=" + liftId +
			", deviceNo=" + deviceNo +
			", driver=" + driver +
			", deviceTime=" + deviceTime +
			", weight=" + weight +
			", height=" + height +
			", speed=" + speed +
			", people=" + people +
			", frontDoorStatus=" + frontDoorStatus +
			", backDoorStatus=" + backDoorStatus +
			", status=" + status +
			", tiltAngle=" + tiltAngle +
			", floor=" + floor +
			", floorStart=" + floorStart +
			", floorEnd=" + floorEnd +
			", key1=" + key1 +
			", key2=" + key2 +
			", key3=" + key3 +
			", key4=" + key4 +
			", key5=" + key5 +
			", key6=" + key6 +
			", key7=" + key7 +
			", key8=" + key8 +
			", key9=" + key9 +
			", key10=" + key10 +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			"}";
	}
}
