package com.xingyun.equipment.admin.modular.device.controller;


import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentHeartbeat;
import com.xingyun.equipment.admin.modular.device.service.ProjectEnvironmentMonitorHeartbeatService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company:星云网格
 *
 * @author zhouyujie
 * @date 2018年8月28日
 */
@RestController
@RequestMapping("ssdevice/project/projectEnvironmentMonitorHeartbeat")
public class ProjectEnvironmentHeartbeatController extends BaseController<ProjectEnvironmentHeartbeat,ProjectEnvironmentMonitorHeartbeatService>{

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

    /**
     * 实时监控一览
     * 没有分表
     * @param res
     * @return
     */
    @ApiOperation("监控状态列表")
    @PostMapping("selectPageList")
    ResultDTO<DataDTO<List<ProjectEnvironmentHeartbeat>>> selectPageList(@RequestBody RequestDTO<ProjectEnvironmentHeartbeat> res) {
        return service.selectPageList(res);
    }
}
