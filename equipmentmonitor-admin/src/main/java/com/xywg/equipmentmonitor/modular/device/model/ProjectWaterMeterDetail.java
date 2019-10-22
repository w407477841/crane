package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
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
@Data
@TableName("t_project_water_meter_detail")
public class ProjectWaterMeterDetail extends BaseEntity<ProjectWaterMeterDetail> {

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
	@TableField("device_time")
	private Date deviceTime;
    /**
     * 备注
     */
	private String comments;
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

	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 真实值
	 */
	//@TableField("actual_degree")
	@TableField(exist = false)
	private BigDecimal actualDegree;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
