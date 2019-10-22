package com.xywg.equipmentmonitor.modular.device.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.modular.device.model.ProjectMessageLift;
import com.xywg.equipmentmonitor.modular.device.service.ProjectMessageLiftService;

/**
*Description:
*Company:星云网格
*@author changmengyu
*@date 2018年8月22日 
*/
@RestController
@RequestMapping("/ssdevice/device/projectMessageLift")
public class ProjectMessageLiftController extends BaseController<ProjectMessageLift,ProjectMessageLiftService>{

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
