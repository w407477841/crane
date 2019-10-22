package com.xywg.equipmentmonitor.modular.device.controller;


import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHeartbeat;
import com.xywg.equipmentmonitor.modular.device.service.ProjectHelmetHeartbeatService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 安全帽在线离线状态切换履历表 前端控制器
 * </p>
 *
 * @author xss
 * @since 2018-12-05
 */
@RestController
@RequestMapping("/ssdevice/device/projectHelmetHeartbeat")
public class ProjectHelmetHeartbeatController extends BaseController<ProjectHelmetHeartbeat,ProjectHelmetHeartbeatService> {

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

    @GetMapping("/selectMonitorStatus")
    @ApiOperation("运行数据")
    public ResultDTO<DataDTO<List<ProjectHelmetHeartbeat>>> getList( RequestDTO request){
        return service.getList(request);
    }


}

