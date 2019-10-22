package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangyifei
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_project_environment_monitor")
public class ProjectEnvironmentMonitor extends Model<ProjectEnvironmentMonitor> {

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
    /**
     * 最低温度
     */
    @TableField("temperature_min")
	private Double temperatureMin;
	/**
	 * 最高温度
	 */
	@TableField("temperature_max")
	private Double temperatureMax;

    /**
     * 最低湿度
     */
    @TableField("humidity_min")
	private Double humidityMin;

	/**
	 * 最高湿度
	 */
	@TableField("humidity_max")
	private Double humidityMax;

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
	@TableField("org_id")
	private Integer orgId;

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
			", temperatureMin=" + temperatureMin +
				", temperatureMan=" + temperatureMax +
			", humidityMin=" + humidityMin +
				", humidityMax=" + humidityMax +
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
