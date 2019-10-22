package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.core.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
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
@TableName("t_project_environment_monitor")
public class ProjectEnvironmentMonitor extends BaseEntity<ProjectEnvironmentMonitor> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 工程名称
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;

    /**
     * 产权编号
     */
	private String identifier;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 产权单位
     */
	private String owner;
	/**
     * GPRS
     */
	private Integer gprs;
    /**
     * 厂家
     */
	private String manufactor;
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
     * 最小温度
     */
	@TableField("temperature_min")
	private BigDecimal temperatureMin;
    /**
     * 最小湿度
     */
	@TableField("humidity_min")
	private BigDecimal humidityMin;
	/**
     * 最小温度
     */
	@TableField("temperature_max")
	private BigDecimal temperatureMax;
	/**
     * 最小温度
     */
	@TableField("humidity_max")
	private BigDecimal humidityMax;
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
     * 在线状态
     */
	@TableField("is_online")
	private Integer isOnline;
    /**
     * 设备状态
     */
	private Integer status;
    /**
     * 位置
     */
	@TableField("place_point")
	private String placePoint;
    /**
     * 备注
     */
	private String comments;
  
    /**
     * 组织结构
     */
	@TableField(value="org_id",fill = FieldFill.INSERT)
	private Integer orgId;

	@ApiModelProperty(value = "名称")
	private String name ;
	

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectEnvironmentMonitor{" +
			"id=" + id +
			", projectId=" + projectId +
			", deviceNo=" + deviceNo +
			", identifier=" + identifier +
			", specification=" + specification +
			", owner=" + owner +
			", manufactor=" + manufactor +
			", pm10=" + pm10 +
			", pm25=" + pm25 +
			", noise=" + noise +
			", windSpeed=" + windSpeed +
			", windDirection=" + windDirection +
		
			", tsp=" + tsp +
			", windForce=" + windForce +
			", atmospheric=" + atmospheric +
			", isOnline=" + isOnline +
			", status=" + status +
			", placePoint=" + placePoint +
			", comments=" + comments +
			", orgId=" + orgId +
			"}";
	}
}
