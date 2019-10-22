package com.xingyun.equipment.crane.modular.device.controller;


import com.xingyun.equipment.crane.core.dto.AlarmInfoDTO;
import com.xingyun.equipment.cache.RedisUtil;

import java.util.*;

import com.xingyun.equipment.crane.modular.device.dto.ProjectCraneStaticsticsByDateDTO;
import com.xingyun.equipment.crane.modular.device.service.IProjectCraneStatisticsDailyService;
import com.xingyun.equipment.crane.modular.device.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dto.ProjectCraneDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 * 塔吊
 * @author xss
 * @since 2018-08-22
 */
@RestController
@RequestMapping("/admin-crane/device/projectCrane")
public class ProjectCraneController extends BaseController<ProjectCrane, ProjectCraneService> {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProjectCraneStatisticsDailyService projectCraneStaticsDailyService;
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
    public ResultDTO<DataDTO<List<ProjectCraneVO>>> selectPageList(@RequestBody RequestDTO<ProjectCraneVO> request) {
        return service.getPageList(request);
    }

    @ApiOperation("判重")
    @PostMapping("/checkByDeviceNoAndProjectId")
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByDeviceNoAndProjectId(@RequestBody RequestDTO<ProjectCrane> request) {
        return service.checkByDeviceNoAndProjectId(request);
    }

    @ApiOperation("判重")
    @PostMapping("/checkByCraneNoAndProjectId")
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByCraneNoAndProjectId(@RequestBody RequestDTO<ProjectCrane> request) {
        return service.checkByCraneNoAndProjectId(request);
    }
    @ApiOperation("判重")
    @PostMapping("/checkByGprsAndProjectId")
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByGprsAndProjectId(@RequestBody RequestDTO<ProjectCrane> request) {
        return service.checkByGprsAndProjectId(request);
    }
    @ApiOperation("新增")
    @PostMapping("insertInfo")
    public ResultDTO<Object> insertInfo(@RequestBody RequestDTO<ProjectCraneDTO> request) {
        try {
            return service.insertInfo(request);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("查询单条")
    @PostMapping("selectInfo")
    public ResultDTO<ProjectCraneDTO> selectInfo(@RequestBody RequestDTO<ProjectCrane> request) {
        return service.selectInfo(request);
    }

    @ApiOperation("编辑")
    @PostMapping("updateInfo")
    public ResultDTO<Object> updateInfo(@RequestBody RequestDTO<ProjectCraneDTO> request) {
        try {
            return service.updateInfo(request);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("启用")
    @PostMapping("updateStatus")
    public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectCrane> request) {
        try {
            return service.updateStatus(request);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("地图不分页查询")
    @PostMapping("/selectListMap")
    public ResultDTO<List<ProjectCraneVO>> selectListMap(@RequestBody RequestDTO<ProjectCraneVO> res){
        return service.selectListMap(res);
    }
    @ApiOperation("查询集团下所有塔吊(接口)")
    @PostMapping("/getCraneList")
    public ResultDTO<ProjectCraneOrgVO> getCraneList(@RequestBody RequestDTO<ProjectCraneVO> request) {
        return service.selectCraneList(request);
    }
    @ApiOperation("查询塔吊最近一条数据(接口)")
    @PostMapping("/getCraneDetail")
    public ResultDTO<ProjectCraneDetailVO> getCraneDetail(@RequestBody RequestDTO<ProjectCraneVO> request) {
        return service.selectCraneDetail(request);
    }

    /**
     *
     * @param uuid
     * @param deviceNo
     * @return
     */
    @ApiOperation("查询塔吊模拟数据")
    @GetMapping("/getCraneDetails")
    public ResultDTO<ProjectCraneDetailVO> getTop100CraneDetails(String uuid,String deviceNo){


        return   service.selectTop100CraneDetails(uuid,deviceNo);

    }

    /**
     * 分时段统计列表
     *edit by caolj
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/getAnalysisList")
    public ResultDTO<DataDTO<List<ProjectCraneVO>>> getAnalysisList(@RequestBody RequestDTO<ProjectCraneVO> requestDTO) {
        return service.getAnalysisList(requestDTO);
    }


    /**
     * 设备工作分时段统计（日期排序列表）
     *edit by caolj
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/selectAnalysisListByDate")
    public ResultDTO<DataDTO<List<ProjectCraneStaticsticsByDateDTO>>> selectAnalysisListByDate(@RequestBody RequestDTO<ProjectCraneStaticsticsByDateDTO> requestDTO) {
        return service.getAnalysisListByDate(requestDTO);
    }


    /**
     * 设备工作分时段统计（综合平均数）
     *edit by caolj
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/selectAnalysisListAvg")
    public ResultDTO<DataDTO<List<ProjectCraneStatisticsByDateVO>>> selectAnalysisListAvg(@RequestBody RequestDTO<ProjectCraneStatisticsByDateVO> requestDTO) {
        return service.getAnalysisListAvg(requestDTO);
    }


    /**
     * 设备台账
     *
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/selectDeviceAccount")
    public ResultDTO<DataDTO<List<GetInfoVO>>> selectDeviceAccount(@RequestBody RequestDTO<GetInfoVO> requestDTO)
    {
        return service.getDeviceAccount(requestDTO);
    }

    /**
     * 设备台账导出
     * *edit by caolj
     * @param response
     * @param projectId
     * @return
     */
    @GetMapping(value = "/getDeviceAccountExcel")
    public Boolean getDeviceAccountExcel(HttpServletResponse response, Integer projectId) {
        service.getDeviceAccountExcel(response,projectId);
        return true;
    }


    /**
     * 分时段统计列表导出
     * @param response
     * @param projectId
     * @return
     */
    @GetMapping(value = "/getAnalysisListExcel")
    public Boolean getAnalysisListExcel(HttpServletResponse response, Integer projectId) {
        service.getAnalysisListExcel(response,projectId);
        return true;
    }

    /**
     * 违章信息已处理未处理
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/selectAlarmInfoCount")
    public ResultDTO<AlarmInfoDTO> selectAlarmInfoCount(@RequestBody RequestDTO<AlarmInfoVO> requestDTO)
    {
        return service.selectAlarmInfoCount(requestDTO);
    }





}

