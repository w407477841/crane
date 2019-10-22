package com.xywg.equipmentmonitor.modular.device.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONUtil;
import com.xywg.equipmentmonitor.core.util.StringCompress;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftDetail;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftHeartbeat;
import com.xywg.equipmentmonitor.modular.device.service.ProjectLiftDetailService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftAlarmVO;

import io.swagger.annotations.ApiOperation;

import static com.xywg.equipmentmonitor.core.common.constant.FlagEnum.LIFT_7_HOURS_DATA;

/**
 * Description:
 * Company:星云网格
 *
 * @author changmengyu
 * @date 2018年8月22日
 */
@RestController
@RequestMapping("/ssdevice/project/projectLiftDetail")
public class ProjectLiftDetailController extends BaseController<ProjectLiftDetail, ProjectLiftDetailService> {

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

    @ApiOperation("查询运行数据")
    @PostMapping("/selectOperationData")
    public ResultDTO<DataDTO<List<ProjectLiftDetail>>> selectOperationData(@RequestBody RequestDTO<ProjectLiftAlarmVO> request) {
        return service.selectOperationData(request);
    }

    @ApiOperation("查询监控状态")
    @PostMapping("/selectMonitorStatus")
    public ResultDTO<DataDTO<List<ProjectLiftHeartbeat>>> selectMonitorStatus(@RequestBody RequestDTO<ProjectLiftHeartbeat> request) {
        return service.selectMonitorStatus(request);
    }
    @ApiOperation("查询运行时间")
    @PostMapping("/selectRunningTime")
    public ResultDTO<DataDTO<List<ProjectLiftHeartbeat>>> selectRunningTime(@RequestBody RequestDTO<ProjectLiftHeartbeat> request) {
        return service.selectRunningTime(request);
    }

    @ApiOperation("从缓存中查询对应升降机设备最后一条数据")
    @GetMapping("getMonitorData")
    public Map<String,Object> getMonitorData(@RequestParam() Map<String, Object> param) {
        String uuid= (String)param.get("uuid");
        String  deviceNo = (String)param.get("deviceNo");
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        result.put("data",service.getMonitorData(deviceNo,uuid));

        return result;
    }

    @ApiOperation("切换到图表")
    @GetMapping("changeToChart")
    public ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate){
    	return service.changeToChart(id,columnName,type,beginDate,endDate);
    }

}
