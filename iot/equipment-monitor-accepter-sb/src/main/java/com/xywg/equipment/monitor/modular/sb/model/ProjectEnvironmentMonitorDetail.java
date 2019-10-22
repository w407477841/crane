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
public class ProjectEnvironmentMonitorDetail {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;
    /**
     * monitor_id
     */
    private Integer monitorId;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * pm10
     */
    private Double pm10;
    /**
     * pm25
     */
    private Double pm25;
    /**
     * 噪音（分贝）
     */
    private Double noise;
    /**
     * 风速（m/s)
     */
    private Double windSpeed;
    /**
     * 风向
     */
    private String windDirection;

    private Date deviceTime;
    /**
     * 温度
     */
    private Double temperature;
    /**
     * 湿度
     */
    private Double humidity;
    /**
     * tsp
     */
    private Double tsp;
    /**
     * 风力
     */
    private Double windForce;
    /**
     * 大气压
     */
    private Double atmospheric;
    /**
     * 设备状态
     */
    private Integer status;
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




    public ProjectEnvironmentMonitorDetail(Integer monitorId, String deviceNo, Double pm10, Double pm25, Double noise, Double windSpeed, String windDirection, Date deviceTime, Double temperature, Double humidity, Double windForce, Double atmospheric, Integer status, Integer isDel, Date createTime, Integer createUser) {
        this.monitorId = monitorId;
        this.deviceNo = deviceNo;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.noise = noise;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.deviceTime = deviceTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windForce = windForce;
        this.atmospheric = atmospheric;
        this.status = status;
        this.isDel = isDel;
        this.createTime = createTime;
        this.createUser = createUser;
    }

    public ProjectEnvironmentMonitorDetail() {
    }
}
