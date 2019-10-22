package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingyun.equipment.core.BaseEntity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_project_electric_power_detail")
public class ProjectElectricPowerDetail extends BaseEntity<ProjectElectricPowerDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 电力id
     */
	@TableField("electric_id")
	private Integer electricId;
    /**
     * 当前
     */
	@TableField("current_degree")
	private BigDecimal currentDegree;
	/**
	 * 真实值
	 */
	@TableField("actual_degree")
	private BigDecimal actualDegree;
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
	@TableField("device_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date deviceTime;
    /**
     * 备注
     */
	private String comments;
	
	 /**
     * 设备状态
     */
	private Integer status;
	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}


	
}
