package com.xywg.equipment.monitor.iot.modular.crane.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-26
 */
@TableName("t_project_crane_heartbeat")
@Data
public class ProjectCraneHeartbeat extends Model<ProjectCraneHeartbeat> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 主表id
     */
	@TableField("crane_id")
	private Integer craneId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 结束时间
	 */
	@TableField("end_time")
	private Date endTime;
	/**
	 * 离线原因
	 */
	private String reason;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneHeartbeat{" +
			"id=" + id +
			", craneId=" + craneId +
			", deviceNo=" + deviceNo +
			", status=" + status +
			", createTime=" + createTime +
			"}";
	}
}
