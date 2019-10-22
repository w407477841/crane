package com.xingyun.equipment.crane.modular.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.core.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-26
 */
@TableName("t_project_environment_heartbeat")
@Data
public class ProjectEnvironmentHeartbeat extends BaseEntity<ProjectEnvironmentHeartbeat> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 主表id
     */
	@TableField("monitor_id")
	private Integer monitorId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 状态
     */
	@TableField("status")
	private Integer status;
    /**
     * 创建日期
     */
	@TableField("create_time")
	private String createTimeStr;

	@TableField("end_time")
	private String endTime;

	@TableField(exist = false)
	private String beginTime;

	/**
	 * 持续时间
	 */
	@TableField(exist = false)
	private Double duration;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
