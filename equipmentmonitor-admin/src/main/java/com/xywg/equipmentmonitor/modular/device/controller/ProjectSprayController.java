package com.xywg.equipmentmonitor.modular.device.controller;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSpray;
import com.xywg.equipmentmonitor.modular.device.service.ProjectSprayService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:53
 */
@RestController
@RequestMapping("/ssdevice/device/projectSpray")
public class ProjectSprayController extends BaseController<ProjectSpray,ProjectSprayService>{
    @Autowired
    private ProjectSprayService projectSprayService;

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

    @PostMapping("/getPageList")
    @ApiOperation("分页查询")
    public ResultDTO<DataDTO<List<ProjectSprayVO>>> selectPageList(@RequestBody RequestDTO<ProjectSprayVO> request) {
        return service.getPageList(request);
    }

    @PostMapping("/checkByDeviceNo")
    @ApiOperation("分页查询")
    public ResultDTO<ProjectSprayVO> checkByDeviceNo(@RequestBody RequestDTO<ProjectSprayVO> request) {
        return service.checkByDeviceNo(request);
    }

    @PostMapping("/selectInfo")
    @ApiOperation("分页查询")
    public ResultDTO<ProjectSprayVO> selectInfo(@RequestBody RequestDTO<ProjectSprayVO> request) {
        return service.selectInfo(request);
    }


    /**
     * 设备重启
     *
     * @param request
     * @return
     */
    @ApiOperation("设备重启")
    @PostMapping("/deviceReboot")
    public ResultDTO<ProjectSpray> deviceReboot(@RequestBody RequestDTO<List<ProjectSpray>> request) {
        return service.deviceReboot(request);
    }

    /**
     * 设备开关
     *
     * @param request
     * @return
     */
    @ApiOperation("设备开关")
    @PostMapping("/deviceOpenClose")
    public ResultDTO<ProjectSpray> deviceOpenClose(@RequestBody RequestDTO<List<ProjectSpray>> request) {
        return service.deviceOpenClose(request);
    }

    /**
     * 根据扬尘编号获取对应的喷淋设备状态
     * @param deviceNo 黑匣子编号
     * @return 查询的信息
     */
    @RequestMapping("/getSpraysByEnvironDevice")
    public ResultDTO<List<ProjectSpray>> getSpraysByEnvironDevice(String deviceNo)
    {
        return projectSprayService.getSpraysByEnvironDevice(deviceNo);
    }

    @ApiOperation("开启")
    @PostMapping("/deviceOperation")
    public ResultDTO<ProjectSpray> deviceOperation(@RequestBody RequestDTO<ProjectSpray> request) {
        RequestDTO<List<ProjectSpray>> requestDTO = new RequestDTO<>();
        List<ProjectSpray> list = new ArrayList<>();
        ProjectSpray projectSpray = new ProjectSpray();
        projectSpray.setDeviceNo(request.getDeviceNo());
        list.add(projectSpray);
        requestDTO.setType(request.getType());
        requestDTO.setBody(list);
        return service.deviceOpenClose(requestDTO);
    }
}
