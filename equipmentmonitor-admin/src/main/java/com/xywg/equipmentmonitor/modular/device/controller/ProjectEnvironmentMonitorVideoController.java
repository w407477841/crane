package com.xywg.equipmentmonitor.modular.device.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorVideo;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorVideoService;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年8月21日 
*/
@RestController
@RequestMapping("ssdevice/project/projectEnvironmentMonitorVideo")
public class ProjectEnvironmentMonitorVideoController extends BaseController<ProjectEnvironmentMonitorVideo, ProjectEnvironmentMonitorVideoService>{

	@Override
	public String insertRole() {
		return null;
	}

	@Override
	public String updateRole() {
		return null;
	}

	@Override
	public String deleteRole() {
		return null;
	}

	@Override
	public String viewRole() {
		return null;
	}

}
