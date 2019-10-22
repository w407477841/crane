package com.xywg.iot.modules.helmet.model;


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
	private Integer heartRate;
	/**
	 * 血氧
	 */
	private Integer bloodOxygen;
	/**
	 * 温度
	 */
	private Double temperature;
	/**
	 * 六轴
	 */
	private String sixAxis;

}
