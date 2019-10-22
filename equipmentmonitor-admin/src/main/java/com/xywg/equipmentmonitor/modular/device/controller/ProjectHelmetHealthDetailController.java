package com.xywg.equipmentmonitor.modular.device.controller;


import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHealthDetail;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetHealthDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * <p>
 * 健康信息(采集数据) 前端控制器
 * </p>
 *
 * @author xss
 * @since 2018-12-06
 */
@RestController
@RequestMapping("ssdevice/device/projectHelmetHealthDetail")
public class ProjectHelmetHealthDetailController extends BaseController<ProjectHelmetHealthDetail,IProjectHelmetHealthDetailService> {
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


    @GetMapping("/getDetailsById")
    @ApiOperation("/")
    public ResultDTO<DataDTO<List<ProjectHelmetHealthDetail>>> getDetailsById(RequestDTO request)throws Exception{
        return service.getDetailsById(request);
    }
}

