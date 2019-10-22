package com.xingyun.equipment.admin.modular.device.controller;

import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.modular.device.service.RealTimeMonitoringService;
import com.xingyun.equipment.admin.modular.device.vo.RealTimeMonitoringTowerVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2018/8/26
 *TIME:16:08
 */
@RestController
@RequestMapping(value = "ssdevice/crane/craneInfo")
public class CraneForSmartController extends BaseController<RealTimeMonitoringTowerVo,RealTimeMonitoringService> {

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

    @ApiOperation("获取塔吊信息给智慧工地")
    @GetMapping("/getCraneInfo")
    public byte[] getCraneInfo(@RequestParam Map<String,Object> map) {
        byte[] result = {};
        try {
            result =  service.getCraneInfo(map);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("/获取报警信息及详情给智慧工地")
    @PostMapping("/getAlarmInfo")
    public byte[] getAlarmInfo(@RequestParam Map<String,Object> map) {
        byte[] result = {};
        try {
            result =  service.getAlarmInfo(map);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("/获取报警详情给智慧工地")
    @GetMapping("/getAlarmDetail")
    public byte[] getAlarmDetail(@RequestParam Map<String,Object> map) {
        byte[] result = {};
        try {
            result =  service.getAlarmDetail(map);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
