package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-09-29
 */
@Data
@TableName("t_project_electric_power_heartbeat")
public class ProjectElectricPowerHeartbeat extends BaseEntity<ProjectElectricPowerHeartbeat> {

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
     * 状态
     */
	private Integer status;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private String endTime;
	
	@TableField(exist = false)
	private String beginTime;
	
	@TableField(exist = false)
	private String createTimeStr;
    /**
     * 备注
     */
	private String comments;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
