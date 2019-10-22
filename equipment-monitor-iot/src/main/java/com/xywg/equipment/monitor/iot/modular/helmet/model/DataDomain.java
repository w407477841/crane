package com.xywg.equipment.monitor.iot.modular.helmet.model;


import lombok.Data;

/**
 * @author hjy
 */
@Data
public class DataDomain {

	private String imei;
	private int positionHealthCommand;
	/**
	 * 经度
	 */
	private Double lng;
	/**
	 * 纬度
	 */
	private Double lat;
	private Double baiduLng;
	private Double baiduLat;
	/**
	 * 心率
	 */
	private String heartRate;
	/**
	 * 血氧
	 */
	private String bloodOxygen;
	/**
	 * 温度
	 */
	private String temperature;
	/**
	 * 六轴
	 */
	private String sixAxis;

}
