package com.xywg.equipmentmonitor.modular.device.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPowerDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectElectricPowerDetailService;

import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Company:星云网格
 *
 * @author hy
 * @date 2018年8月21日
 */
@RestController
@RequestMapping("ssdevice/project/projectElectricPowerDetail")
public class ProjectElectricPowerDetailController extends BaseController<ProjectElectricPowerDetail, ProjectElectricPowerDetailService> {
    @Autowired
    private RedisUtil redisUtil;

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

    /**
     * 电表设备监控一览
     * 有分表
     *
     * @param res
     * @return
     */
    @ApiOperation("电表设备监控一览")
    @PostMapping("selectPageList")
    ResultDTO<DataDTO<List<ProjectElectricPowerDetail>>> selectPageList(@RequestBody RequestDTO<ProjectElectricPowerDetail> res) {
        return service.selectPageList(res);
    }

    @SuppressWarnings("rawtypes")
	@ApiOperation("获取用电统计信息给智慧工地")
    @GetMapping("/getElectricInfo")
    public byte[] getElectricInfo(RequestDTO request) {
        byte[] result = {};
        try {
            result =  service.getElectricInfos(request);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @SuppressWarnings("rawtypes")
	@ApiOperation("获取用水统计信息给智慧工地")
    @GetMapping("/getWaterInfo")
    public byte[] getWaterInfo(RequestDTO request) {
        byte[] result = {};
        try {
            result =  service.getWaterInfos(request);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
