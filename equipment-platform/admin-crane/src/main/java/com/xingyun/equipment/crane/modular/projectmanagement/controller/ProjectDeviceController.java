package com.xingyun.equipment.crane.modular.projectmanagement.controller;

import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectDeviceDose;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectDeviceInfo;
import com.xingyun.equipment.crane.modular.projectmanagement.service.ProjectDeviceService;
import com.xingyun.equipment.crane.modular.projectmanagement.vo.ProjectDeviceDoseVO;
import com.xingyun.equipment.crane.modular.projectmanagement.vo.ProjectDeviceInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 *@author:hjy
 *DATE:2018/9/14
 *TIME:10:52
 * 获取项目下设备信息
 */
@RestController
@RequestMapping(value = "/admin-crane/project")
public class ProjectDeviceController{
    @Autowired
    private ProjectDeviceService projectDeviceService;

    /**
     * 当前项目下所有设备详情信息
     * @param projectDeviceInfo
     * @return
     */
    @ApiOperation("当前项目下所有电表设备详情信息")
    @PostMapping(value = "/getDeviceInfo")
    public ResultDTO<List<ProjectDeviceInfoVO>> getDeviceInfo( ProjectDeviceInfo projectDeviceInfo) {
        return projectDeviceService.getDeviceInfo(projectDeviceInfo);
    }



    /**
     * 当前项目下所有设备详情信息
     * @param projectDeviceInfo
     * @return
     */
    @ApiOperation("当前项目下所有水表设备详情信息")
    @PostMapping(value = "/getWaterDeviceInfo")
    public ResultDTO<List<ProjectDeviceInfoVO>> getDeviceWaterInfo( ProjectDeviceInfo projectDeviceInfo) {
        return projectDeviceService.getDeviceWaterInfo(projectDeviceInfo);
    }

    /**
     * 当前项目下所有设备信息用量
     * @param projectDeviceDose
     * @return
     */
    @ApiOperation("当前项目下所有设备信息用量")
    @PostMapping(value = "/getInfoDose")
    public ResultDTO<List<ProjectDeviceDoseVO>> getInfoDose( ProjectDeviceDose projectDeviceDose) {
        return projectDeviceService.getInfoDose(projectDeviceDose);
    }
    @ApiOperation("当前项目下所有设备信息用量")
    @PostMapping(value = "/getWaterInfoDose")
    public ResultDTO<List<ProjectDeviceDoseVO>> getInfoWaterDose( ProjectDeviceDose projectDeviceDose) {
        return projectDeviceService.getInfoWaterDose(projectDeviceDose);
    }
    /**
     * 零时需要
     * @param projectId
     * @return
     */
    @ApiOperation("图表数据")
    @GetMapping("getChart")
    public Object getChart(Integer projectId){



        return projectDeviceService.getChart();

    }
    /**
     * 零时需要
     * @param projectId
     * @return
     */
    @ApiOperation("图表数据")
    @GetMapping("getTypeChart")
    public Object getChart1(Integer projectId ,Integer Flag){


        return projectDeviceService.getChart1();
    }





}
