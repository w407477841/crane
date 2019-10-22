package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
public class ProjectEnvironmentHeartbeat extends Model<ProjectEnvironmentHeartbeat> {

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
		return "ProjectEnvironmentHeartbeat{" +
			"id=" + id +
			", monitorId=" + monitorId +
			", deviceNo=" + deviceNo +
			", status=" + status +
			", createTime=" + createTime +
			"}";
	}
}
