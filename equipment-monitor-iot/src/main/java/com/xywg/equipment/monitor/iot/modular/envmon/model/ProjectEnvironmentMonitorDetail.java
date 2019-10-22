package com.xywg.equipment.monitor.iot.modular.envmon.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
@Data
@TableName("t_project_environment_monitor_detail")
public class ProjectEnvironmentMonitorDetail extends Model<ProjectEnvironmentMonitorDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * monitor_id
     */
    @TableField("monitor_id")
    private Integer monitorId;
    /**
     * 设备编号
     */
    @TableField("device_no")
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
    @TableField("wind_speed")
    private Double windSpeed;
    /**
     * 风向
     */
    @TableField("wind_direction")
    private String windDirection;

    @TableField("device_time")
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
    @TableField("wind_force")
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ProjectEnvironmentMonitorDetail{" +
                "id=" + id +
                ", monitorId=" + monitorId +
                ", deviceNo=" + deviceNo +
                ", pm10=" + pm10 +
                ", pm25=" + pm25 +
                ", noise=" + noise +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", tsp=" + tsp +
                ", windForce=" + windForce +
                ", atmospheric=" + atmospheric +
                ", status=" + status +
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
