package com.xingyun.equipment.admin.modular.device.dto;

import java.util.List;

import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitorVideo;

import com.xingyun.equipment.admin.modular.device.vo.ProjectEnvironmentMonitorVO;
import lombok.Data;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年8月22日 
*/
@Data
public class ProjectEnvironmentMonitorDTO {

	private ProjectEnvironmentMonitorVO monitor;
	private List<ProjectEnvironmentMonitorVideo> videos;
}
