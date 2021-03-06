package com.xingyun.equipment.admin.modular.infromation.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
@TableName("t_project_lift_data_model")
public class ProjectLiftDataModel extends BaseEntity<ProjectLiftDataModel> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 制造厂家
     */
	private String manufactor;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 司机
     */
	private String driver;
    /**
     * 时间
     */
	@TableField("device_time")
	private String deviceTime;
    /**
     * 重量
     */
	private String weight;
    /**
     * 高度
     */
	private String height;
    /**
     * 速度
     */
	private String speed;
    /**
     * 人数
     */
	private String people;
    /**
     * 前门状态
     */
	@TableField("front_door_status")
	private String frontDoorStatus;
    /**
     * 后门状态
     */
	@TableField("back_door_status")
	private String backDoorStatus;
    /**
     * 升降机状态
     */
	private String status;
    /**
     * 楼层
     */
	private String floor;
    /**
     * 启动楼层
     */
	@TableField("floor_start")
	private String floorStart;
    /**
     * 停止楼层
     */
	@TableField("floor_end")
	private String floorEnd;
    /**
     * 倾角
     */
	@TableField("tilt_angle")
	private BigDecimal tiltAngle;
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
	 * 报警
	 */
	private String alarm;



	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getManufactor() {
		return manufactor;
	}

	public void setManufactor(String manufactor) {
		this.manufactor = manufactor;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(String deviceTime) {
		this.deviceTime = deviceTime;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getFrontDoorStatus() {
		return frontDoorStatus;
	}

	public void setFrontDoorStatus(String frontDoorStatus) {
		this.frontDoorStatus = frontDoorStatus;
	}

	public String getBackDoorStatus() {
		return backDoorStatus;
	}

	public void setBackDoorStatus(String backDoorStatus) {
		this.backDoorStatus = backDoorStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getFloorStart() {
		return floorStart;
	}

	public void setFloorStart(String floorStart) {
		this.floorStart = floorStart;
	}

	public String getFloorEnd() {
		return floorEnd;
	}

	public void setFloorEnd(String floorEnd) {
		this.floorEnd = floorEnd;
	}

	public BigDecimal getTiltAngle() {
		return tiltAngle;
	}

	public void setTiltAngle(BigDecimal tiltAngle) {
		this.tiltAngle = tiltAngle;
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




	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
