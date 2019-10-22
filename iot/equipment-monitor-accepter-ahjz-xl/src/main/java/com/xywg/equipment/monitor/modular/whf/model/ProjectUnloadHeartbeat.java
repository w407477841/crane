package com.xywg.equipmentmonitor.modular.device.model;

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
 * @author hy
 * @since 2019-07-11
 */
@TableName("t_project_unload_heartbeat")
@Data
public class ProjectUnloadHeartbeat extends Model<ProjectUnloadHeartbeat> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 主表id
	 */
	@TableField("unload_id")
	private Integer unloadId;
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
	 * 创建日期
	 */
	@TableField("create_time")
	private Date createTime;
	@TableField("end_time")
	private Date endTime;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	@Override
	public String toString() {
		return "ProjectUnloadHeartbeat{" +
				"id=" + id +
				", unloadId=" + unloadId +
				", deviceNo=" + deviceNo +
				", status=" + status +
				", createTime=" + createTime +
				", endTime=" + endTime +
				"}";
	}
}
