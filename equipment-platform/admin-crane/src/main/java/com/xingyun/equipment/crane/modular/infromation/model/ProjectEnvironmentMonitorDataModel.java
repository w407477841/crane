package com.xingyun.equipment.crane.modular.infromation.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.core.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
@TableName("t_project_environment_monitor_data_model")
public class ProjectEnvironmentMonitorDataModel extends BaseEntity<ProjectEnvironmentMonitorDataModel> {

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
     * pm10
     */
	private String pm10;
    /**
     * pm25
     */
	private String pm25;
    /**
     * 噪音（分贝）
     */
	private String noise;
    /**
     * 风速（m/s)
     */
	@TableField("wind_speed")
	private String windSpeed;
    /**
     * 风向
     */
	@TableField("wind_direction")
	private String windDirection;
    /**
     * 温度
     */
	private String temperature;
    /**
     * 湿度
     */
	private String humidity;
    /**
     * tsp
     */
	private String tsp;
    /**
     * 风力
     */
	@TableField("wind_force")
	private String windForce;
    /**
     * 大气压
     */
	private String atmospheric;
    /**
     * 设备状态
     */
	private String status;
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

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	/**
	 * 报警
	 */

	private String alarm;


    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;


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

	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getNoise() {
		return noise;
	}

	public void setNoise(String noise) {
		this.noise = noise;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getTsp() {
		return tsp;
	}

	public void setTsp(String tsp) {
		this.tsp = tsp;
	}

	public String getWindForce() {
		return windForce;
	}

	public void setWindForce(String windForce) {
		this.windForce = windForce;
	}

	public String getAtmospheric() {
		return atmospheric;
	}

	public void setAtmospheric(String atmospheric) {
		this.atmospheric = atmospheric;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}
