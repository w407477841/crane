package com.xingyun.equipment.crane.modular.baseinfo.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.core.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
@Data
@TableName("t_project_master_device_type")
public class MasterDeviceType extends BaseEntity<MasterDeviceType> {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String comments;

	/**
	 * 占用
	 */
	@TableField("call_times")
	private Integer callTimes;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
