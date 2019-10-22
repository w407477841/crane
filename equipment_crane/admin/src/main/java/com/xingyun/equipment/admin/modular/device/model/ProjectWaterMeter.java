package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-09-27
 */
@TableName("t_project_water_meter")
@Data
public class ProjectWaterMeter extends BaseEntity<ProjectWaterMeter> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 设备编号
     */
	private String name;
    /**
     * 通信地址
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 采集器编号
     */
	@TableField("collecter_no")
	private String collecterNo;
    /**
     * 1：生活用水 2：生产用水 3：消防用水
     */
	private Integer type;
    /**
     * 水表类型 1：冷水表 2：热水表
     */
	@TableField("device_type")
	private Integer deviceType;
    /**
     * 项目
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * GPRS
     */
	private String gprs;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 制造厂商
     */
	private String manufactor;
    /**
     * 每秒耗电量(千瓦时)
     */
	private BigDecimal dissipation;
    /**
     * 是否在线
     */
	@TableField("is_online")
	private Integer isOnline;
    /**
     * 设备状态
     */
	private Integer status;
    /**
     * 采集器状态
     */
	@TableField("is_online_collecter")
	private Integer isOnlineCollecter;
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
     * 组织 
     */
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;
	/**
	 * 互感系数
	 */
	private BigDecimal ratio;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
