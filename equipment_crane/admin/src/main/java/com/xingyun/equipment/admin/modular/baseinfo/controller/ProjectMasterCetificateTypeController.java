package com.xingyun.equipment.admin.modular.baseinfo.controller;

import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.baseinfo.model.ProjectMasterCetificateType;
import com.xingyun.equipment.admin.modular.baseinfo.service.IProjectMasterCetificateTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/8/28
 *TIME:16:46
 */
@RestController
@RequestMapping(value = "/ssdevice/baseInfo/projectMasterCetificateType")
public class ProjectMasterCetificateTypeController extends BaseController<ProjectMasterCetificateType,IProjectMasterCetificateTypeService> {

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

    @ApiOperation("获取证书类型不分页")
    @PostMapping(value = "/selectCetificateType")
    public ResultDTO<List<ProjectMasterCetificateType>> selectCetificateType(@RequestBody RequestDTO<ProjectMasterCetificateType> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectCetificateType());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }
}
