package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

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
@TableName("t_project_electric_power_alarm")
public class ProjectElectricPowerAlarm extends BaseEntity<ProjectElectricPowerAlarm> {

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
     * 明细id
     */
	@TableField("detail_id")
	private Integer detailId;
    /**
     * 报警编号
     */
	@TableField("alarm_id")
	private Integer alarmId;
    /**
     * 报警明细
     */
	@TableField("alarm_info")
	private String alarmInfo;
    /**
     * 处理状态
     */
	private Integer status;
    /**
     * 处理人
     */
	@TableField("modify_user_name")
	private String modifyUserName;
    /**
     * 备注
     */
	private String comments;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
