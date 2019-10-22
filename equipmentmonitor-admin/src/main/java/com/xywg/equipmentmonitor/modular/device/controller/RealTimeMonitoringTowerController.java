package com.xywg.equipmentmonitor.modular.device.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.StringCompress;
import com.xywg.equipmentmonitor.modular.device.service.RealTimeMonitoringService;
import com.xywg.equipmentmonitor.modular.device.vo.RealTimeMonitoringTowerVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xywg.equipmentmonitor.core.common.constant.FlagEnum.CRANE_7_HOURS_DATA;

/***
 *@author:jixiaojun
 *DATE:2018/8/23
 *TIME:9:27
 */
@RestController
@RequestMapping(value = "/ssdevice/project/realTimeMonitoringTower")
public class RealTimeMonitoringTowerController extends BaseController<RealTimeMonitoringTowerVo,RealTimeMonitoringService> {

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
        return "information:realTower:view";
    }

    @ApiOperation("获取塔吊列表")
    @PostMapping(value = "/selectCrane")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectCrane(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> ts = service.selectCrane(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取运行数据及吊重数据")
    @PostMapping(value = "/selectCraneData")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectCraneData(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> ts = service.selectCraneData(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取预警信息")
    @PostMapping(value = "/selectPromptingAlarm")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectPromptingAlarm(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectPromptingAlarm(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取报警信息")
    @PostMapping(value = "/selectWarningAlarm")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectWarningAlarm(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectWarningAlarm(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取违章信息")
    @PostMapping(value = "/selectViolationAlarm")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectViolationAlarm(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectViolationAlarm(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取报警详情")
    @PostMapping(value = "/selectAlarmData")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectAlarmData(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectAlarmData(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("发送短信")
    @PostMapping(value = "/insertMessage")
    public ResultDTO<RealTimeMonitoringTowerVo> insertMessage(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> t) {
        try {
            if(service.insertMessage(t.getBody())) {
                return ResultDTO.resultFactory(OperationEnum.SEND_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.SEND_ERROR);
    }

    @ApiOperation("获取监控状态")
    @PostMapping(value = "/selectMonitorStatus")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectMonitorStatus(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> list = service.selectMonitorStatus(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取运行时间")
    @PostMapping(value = "/selectRunTime")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectRunTime(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> list = service.selectRunTime(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("近期最后一条数据")
    @GetMapping("getMonitorData")
    public Map<String,Object> getMonitorData(@RequestParam() Map<String, Object> param) {
        String uuid= (String)param.get("uuid");
        String  deviceNo = (String)param.get("deviceNo");
        Map<String,Object> result = new HashMap<>();

        result.put("data",service.getMonitorData(deviceNo,uuid));
        return result;
    }

}
