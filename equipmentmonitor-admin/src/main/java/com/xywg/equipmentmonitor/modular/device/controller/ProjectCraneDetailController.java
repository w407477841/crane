package com.xywg.equipmentmonitor.modular.device.controller;


import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCraneDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneDetailService;
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

