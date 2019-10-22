package com.xingyun.equipment.admin.modular.device.controller;

import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.modular.device.model.ProjectMessageWater;
import com.xingyun.equipment.admin.modular.device.service.IProjectMessageWaterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 *@author:jixiaojun
 *DATE:2018/9/29
 *TIME:11:23
 */
@RestController
@RequestMapping("/ssdevice/device/projectMessageWater")
public class ProjectMessageWaterController extends BaseController<ProjectMessageWater,IProjectMessageWaterService> {
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
