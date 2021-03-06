package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
@TableName("t_project_environment_monitor_alarm")
@Data
public class ProjectEnvironmentMonitorAlarm extends DeviceAlarm<ProjectEnvironmentMonitorAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * monitor_id
     */
	@TableField("monitor_id")
	private Integer monitorId;
	@TableField("detail_id")
	private Integer detailId;





	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectEnvironmentMonitorAlarm{" +
			"id=" + id +
			", monitorId=" + monitorId +

			"}";
	}
}
