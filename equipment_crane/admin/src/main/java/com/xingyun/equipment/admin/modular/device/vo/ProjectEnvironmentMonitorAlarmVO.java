package com.xingyun.equipment.admin.modular.device.vo;

import java.sql.Date;
import java.util.List;

import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitorAlarm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年8月28日 
*/
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectEnvironmentMonitorAlarmVO extends ProjectEnvironmentMonitorAlarm{

	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 报警时间
	 */
	private Date deviceTime;
	/**
	 * 报警次数
	 */
	private Integer amount;
	
	/**
	 * 报警信息
	 */
	private String info;
	
	private List<ProjectEnvironmentMonitorAlarmVO> infoList;
}
