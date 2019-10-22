package com.xingyun.equipment.crane.modular.infromation.controller;


import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectTargetSetEnvironment;
import com.xingyun.equipment.crane.modular.infromation.service.ProjectTargetSetEnvironmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xss
 * @since 2018-08-20
 */
@RestController
@RequestMapping("/admin-crane/infromation/projectTargetSetEnvironment")
public class ProjectTargetSetEnvironmentController extends BaseController<ProjectTargetSetEnvironment,ProjectTargetSetEnvironmentService> {

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

    @ApiOperation("条件分页查询")
    @PostMapping("/selectPageList")
    public ResultDTO<DataDTO<List<ProjectTargetSetEnvironment>>> selectPageList(@RequestBody RequestDTO<ProjectTargetSetEnvironment> request){
        return service.getPageList(request);
    }

    @ApiOperation("/判重")
    @PostMapping("/checkBySpecificationAndManufactor")
    public ResultDTO<DataDTO<List<ProjectTargetSetEnvironment>>> checkBySpecificationAndManufactor(@RequestBody RequestDTO<ProjectTargetSetEnvironment> request){
        return service.checkBySpecificationAndManufactor(request);
    }
}

