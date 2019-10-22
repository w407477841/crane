package com.xingyun.equipment.admin.modular.infromation.controller;
import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetCrane;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectTargetSetCraneService;
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
 * @since 2018-08-22
 */
@RestController
@RequestMapping("/ssdevice/infromation/projectTargetSetCrane")
public class ProjectTargetSetCraneController extends BaseController<ProjectTargetSetCrane,ProjectTargetSetCraneService> {

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
    public ResultDTO<DataDTO<List<ProjectTargetSetCrane>>> selectPageList(@RequestBody RequestDTO<ProjectTargetSetCrane> request){
        return service.selectPageList(request);
    }

    @ApiOperation("判重")
    @PostMapping("/checkBySpecificationAndManufactor")
    public ResultDTO<DataDTO<List<ProjectTargetSetCrane>>> checkBySpecificationAndManufactor(@RequestBody RequestDTO<ProjectTargetSetCrane> request){
        return service.checkBySpecificationAndManufactor(request);
    }
}

