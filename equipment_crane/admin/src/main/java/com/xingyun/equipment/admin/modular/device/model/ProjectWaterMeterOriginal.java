package com.xingyun.equipment.admin.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("t_project_water_meter_original")
@Data
public class ProjectWaterMeterOriginal extends BaseEntity<ProjectWaterMeterOriginal> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 发送数据
     */
	@TableField("send_data")
	private String sendData;
    /**
     * 接收数据
     */
	@TableField("receive_data")
	private String receiveData;
    /**
     * 时间
     */
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
