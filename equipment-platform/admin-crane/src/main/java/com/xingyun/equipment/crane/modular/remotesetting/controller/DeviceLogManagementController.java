package com.xingyun.equipment.crane.modular.remotesetting.controller;


import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.*;
import com.xingyun.equipment.crane.modular.remotesetting.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备日志管理
 *
 * @author hjy
 */
@RestController
@RequestMapping("/admin-crane/remoteSetting/deviceLog")
public class DeviceLogManagementController {
    @Autowired
    private DeviceLogManagementService deviceLogManagementService;
    @Autowired
    private ProjectDeviceErrorLogService projectDeviceErrorLogService;
    @Autowired
    private ProjectMessageDeviceErrorService projectMessageDeviceErrorService;
    @Autowired
    private ProjectDeviceRestartRecordService projectDeviceRestartRecordService;
    @Autowired
    private ProjectCraneCalibrationLogService projectCraneCalibrationLogService;
    @Autowired
    private ProjectCraneSingleCollisionAvoidanceSetService projectCraneSingleCollisionAvoidanceSetService;
    /**
     * 分页查询所有设备一栏
     *
     * @param request
     * @return
     */
    @ApiOperation("条件分页查询")
    @PostMapping("/getPageList")
    public ResultDTO<DataDTO<List<DeviceLogInfo>>> getPageList(@RequestBody RequestDTO<DeviceLogInfo> request) {
        return deviceLogManagementService.getPageList(request);
    }

    /**
     * 设备重启
     *
     * @param request
     * @return
     */
    @ApiOperation("设备重启")
    @PostMapping("/deviceReboot")
    public ResultDTO<Object> deviceReboot(@RequestBody RequestDTO<List<DeviceLogInfo>> request) {
        return deviceLogManagementService.deviceReboot(request);
    }


    /**
     * 获取异常日志履历
     *
     * @param request
     * @return
     */
    @ApiOperation("获取异常日志履历")
    @PostMapping("/getExceptionLog")
    public ResultDTO<DataDTO<List<ProjectDeviceErrorLog>>> getExceptionLog(@RequestBody RequestDTO<ProjectDeviceErrorLog> request) {
        return projectDeviceErrorLogService.getPageList(request);
    }

    /**
     * 获取异常日志推送履历
     *
     * @param request
     * @return
     */
    @ApiOperation("获取异常日志推送履历")
    @PostMapping("/getExceptionSMSLog")
    public ResultDTO<DataDTO<List<ProjectMessageDeviceError>>> getExceptionSMSLog(@RequestBody RequestDTO<ProjectMessageDeviceError> request) {
        return projectMessageDeviceErrorService.getPageList(request);
    }


    /**
     * 获取设备重启履历
     *
     * @param request
     * @return
     */
    @ApiOperation("获取设备重启履历")
    @PostMapping("/getRebootLog")
    public ResultDTO<DataDTO<List<ProjectDeviceRestartRecord>>> getRebootLog(@RequestBody RequestDTO<ProjectDeviceRestartRecord> request) {
        return projectDeviceRestartRecordService.getPageList(request);
    }

    /**
     * 获取设备重启履历
     *
     * @param request
     * @return
     */
    @ApiOperation("获取校准设置履历")
    @PostMapping("/getCalibrationSet")
    public ResultDTO<DataDTO<List<ProjectCraneCalibrationLog>>> getCalibrationSet(@RequestBody RequestDTO<ProjectCraneCalibrationLog> request) {
        return projectCraneCalibrationLogService.getPageList(request);
    }

    /**
     * 获取单区防碰撞设置履历
     *
     * @param request
     * @return
     */
    @ApiOperation("获取单机防碰撞设置履历")
    @PostMapping("/getCraneSingleCollisionAvoidanceSet")
    public ResultDTO<DataDTO<List<ProjectCraneSingleCollisionAvoidanceSet>>> getCraneSingleCollisionAvoidanceSet(@RequestBody RequestDTO<ProjectCraneSingleCollisionAvoidanceSet> request) {
        return projectCraneSingleCollisionAvoidanceSetService.getPageList(request);
    }

}

