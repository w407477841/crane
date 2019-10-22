package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_project_environment_monitor_detail")
public class ProjectEnvironmentMonitorDetail extends BaseEntity<ProjectEnvironmentMonitorDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
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
	private BigDecimal pm10;
    /**
     * pm25
     */
	private BigDecimal pm25;
    /**
     * 噪音（分贝）
     */
	private BigDecimal noise;
    /**
     * 风速（m/s)
     */
	@TableField("wind_speed")
	private BigDecimal windSpeed;
    /**
     * 风向
     */
	@TableField("wind_direction")
	private String windDirection;
    /**
     * 温度
     */
	private BigDecimal temperature;
    /**
     * 湿度
     */
	private BigDecimal humidity;
    /**
     * tsp
     */
	private BigDecimal tsp;
    /**
     * 风力
     */
	@TableField("wind_force")
	private BigDecimal windForce;
    /**
     * 大气压
     */
	private BigDecimal atmospheric;
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
	 * 开始时间
	 */
	@TableField(exist = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private String beginTime;
	/**
	 * 结束时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField(exist = false)
	private String endTime;

	/**
	 * 运行时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("device_time")
	private String deviceTime;

	

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
