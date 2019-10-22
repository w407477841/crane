package com.xywg.equipment.monitor.modular.sb.model;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
@Data
public class ProjectEnvironmentMonitorDataModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Integer id;
	private String deviceNo;
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
	private String windSpeed;
    /**
     * 风向
     */
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
    /**
     * 删除标志
     */
	private Integer isDel;
    /**
     * 创建日期
     */
	private Date createTime;
    /**
     * 创建人
     */
	private Integer createUser;
    /**
     * 修改日期
     */
	private Date modifyTime;
    /**
     * 修改人
     */
	private Integer modifyUser;




}
