package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.core.BaseEntity;


/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_project_electric_power")
public class ProjectElectricPower extends BaseEntity<ProjectElectricPower> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 设备名称
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
     * 1：生活用电 2：生产用电
     */
	private Integer type;
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
	@TableField(value = "status", fill = FieldFill.INSERT)
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
     * 组织 
     */
	@TableField(value="org_id",fill = FieldFill.INSERT)
	private Integer orgId;
	/**
	 * 系数
 	 */
	private BigDecimal ratio;

	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}


	
}
