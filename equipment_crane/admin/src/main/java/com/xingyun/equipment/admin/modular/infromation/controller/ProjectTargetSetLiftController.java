package com.xingyun.equipment.admin.modular.infromation.controller;


import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetLift;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectTargetSetLiftService;
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
 * @since 2018-08-21
 */
@RestController
@RequestMapping("/ssdevice/infromation/projectTargetSetLift")
public class ProjectTargetSetLiftController extends BaseController<ProjectTargetSetLift,ProjectTargetSetLiftService> {

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
    public ResultDTO<DataDTO<List<ProjectTargetSetLift>>> selectPageList(@RequestBody RequestDTO<ProjectTargetSetLift> request){
        return service.selectPageList(request);
    }

    @ApiOperation("判重")
    @PostMapping("/checkBySpecificationAndManufactor")
    public ResultDTO<DataDTO<List<ProjectTargetSetLift>>> checkBySpecificationAndManufactor(@RequestBody RequestDTO<ProjectTargetSetLift> request){
        return service.checkBySpecificationAndManufactor(request);
    }

}

