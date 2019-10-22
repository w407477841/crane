package com.xingyun.equipment.admin.modular.device.controller;


import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneVideo;
import com.xingyun.equipment.admin.modular.device.service.ProjectCraneVideoService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xss
 * @since 2018-08-23
 */
@RestController
@RequestMapping("ssdevice/device/projectCraneVideo")
public class ProjectCraneVideoController extends BaseController<ProjectCraneVideo,ProjectCraneVideoService> {

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

