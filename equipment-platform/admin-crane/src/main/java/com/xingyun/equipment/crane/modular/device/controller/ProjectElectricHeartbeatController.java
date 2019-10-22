package com.xingyun.equipment.crane.modular.device.controller;


import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectElectricPowerHeartbeat;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentHeartbeat;
import com.xingyun.equipment.crane.modular.device.service.ProjectElectricPowerHeartbeatService;

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
@RequestMapping("admin-crane/project/projectElectricPowerHeartbeat")
public class ProjectElectricHeartbeatController extends BaseController<ProjectElectricPowerHeartbeat,ProjectElectricPowerHeartbeatService>{

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
    ResultDTO<DataDTO<List<ProjectElectricPowerHeartbeat>>> selectPageList(@RequestBody RequestDTO<ProjectElectricPowerHeartbeat> res) {
        return service.selectPageList(res);
    }
}
