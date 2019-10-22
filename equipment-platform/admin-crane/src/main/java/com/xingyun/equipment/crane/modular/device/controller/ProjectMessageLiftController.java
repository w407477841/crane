package com.xingyun.equipment.crane.modular.device.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.crane.modular.device.model.ProjectMessageLift;
import com.xingyun.equipment.crane.modular.device.service.ProjectMessageLiftService;

/**
*Description:
*Company:星云网格
*@author changmengyu
*@date 2018年8月22日 
*/
@RestController
@RequestMapping("/admin-crane/device/projectMessageLift")
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
