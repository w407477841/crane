package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_project_environment_monitor_alarm")
public class ProjectEnvironmentMonitorAlarm extends BaseEntity<ProjectEnvironmentMonitorAlarm> {

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

	@TableField("alarm_id")
	private Integer alarmId;

    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 报警类型
     */
	@TableField("alarm_info")
	private String alarmInfo;

	/**
	 * 报警时间
	 */
	@TableField(exist = false)
	private String alarmTime;
	/**状态*/
	private Integer status;
	/**修改人*/
	@TableField("modify_user_name")
	private String modifyUserName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
