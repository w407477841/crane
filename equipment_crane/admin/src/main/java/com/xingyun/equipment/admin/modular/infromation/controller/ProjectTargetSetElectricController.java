package com.xingyun.equipment.admin.modular.infromation.controller;


import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetElectric;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetEnvironment;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectTargetSetElectricService;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectTargetSetEnvironmentService;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  电表指标设置
 * </p>
 *
 * @author hy
 * @since 2018-08-20
 */
@RestController
@RequestMapping("/ssdevice/infromation/projectTargetSetElectric")
public class ProjectTargetSetElectricController extends BaseController<ProjectTargetSetElectric,ProjectTargetSetElectricService> {

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
    public ResultDTO<DataDTO<List<ProjectTargetSetElectric>>> selectPageList(@RequestBody RequestDTO<ProjectTargetSetElectric> request){
        return service.getPageList(request);
    }

    @ApiOperation("/判重")
    @PostMapping("/checkBySpecificationAndManufactor")
    public ResultDTO<DataDTO<List<ProjectTargetSetElectric>>> checkBySpecificationAndManufactor(@RequestBody RequestDTO<ProjectTargetSetElectric> request){
        return service.checkBySpecificationAndManufactor(request);
    }
}

