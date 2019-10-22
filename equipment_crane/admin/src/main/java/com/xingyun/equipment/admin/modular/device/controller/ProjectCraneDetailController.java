package com.xingyun.equipment.admin.modular.device.controller;


import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneDetail;
import com.xingyun.equipment.admin.modular.device.service.ProjectCraneDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xss
 * @since 2018-12-20
 */
@RestController
@RequestMapping("ssdevice/device/projectCraneDetail")
public class ProjectCraneDetailController extends BaseController<ProjectCraneDetail,ProjectCraneDetailService> {

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

    @ApiOperation("切换到图表")
    @GetMapping("changeToChart")
    public ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate){
    	return service.changeToChart(id,columnName,type,beginDate,endDate);
    }
}

