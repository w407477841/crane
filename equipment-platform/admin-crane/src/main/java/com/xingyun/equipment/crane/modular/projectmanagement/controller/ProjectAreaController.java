package com.xingyun.equipment.crane.modular.projectmanagement.controller;

import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectArea;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectAreaService;
import com.xingyun.equipment.crane.modular.projectmanagement.vo.ProjectAreaVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/8/26
 *TIME:15:49
 */
@RestController
@RequestMapping(value = "/admin-crane/projectManagement/projectArea")
public class ProjectAreaController extends BaseController<ProjectArea,IProjectAreaService> {

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

    @ApiOperation("获取项目区域不分页")
    @PostMapping(value = "/selectArea")
    public ResultDTO<List<ProjectAreaVo>> selectArea(@RequestBody RequestDTO<ProjectAreaVo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectArea());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }
}
