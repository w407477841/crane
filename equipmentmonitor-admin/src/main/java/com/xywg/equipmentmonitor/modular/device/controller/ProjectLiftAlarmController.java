package com.xywg.equipmentmonitor.modular.device.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftAlarm;
import com.xywg.equipmentmonitor.modular.device.service.ProjectLiftAlarmService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftAlarmVO;
import com.xywg.equipmentmonitor.modular.device.vo.RealTimeMonitoringTowerVo;

import io.swagger.annotations.ApiOperation;

/**
*Description:
*Company:星云网格
*@author changmengyu
*@date 2018年8月22日 
*/
@RestController
@RequestMapping("/ssdevice/project/projectLiftAlarm")
public class ProjectLiftAlarmController extends BaseController<ProjectLiftAlarm, ProjectLiftAlarmService>{

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
	    public ResultDTO<DataDTO<List<ProjectLiftAlarm>>> selectPageList(@RequestBody RequestDTO<ProjectLiftAlarm> request){
	        return service.getPageList(request);
	    }
	 
	 @ApiOperation("查询预警个数")
	    @PostMapping("/countEarlyWarning")
	    public ResultDTO<ProjectLiftAlarmVO> countEarlyWarning(@RequestBody RequestDTO<ProjectLiftAlarmVO> request){
	        return service.countEarlyWarning(request.getBody());
	    }
	 @ApiOperation("查询警告个数")
	    @PostMapping("/countCallWarning")
	    public ResultDTO<ProjectLiftAlarmVO> countCallWarning(@RequestBody RequestDTO<ProjectLiftAlarmVO> request){
	        return service.countCallWarning(request.getBody());
	    }
	 @ApiOperation("查询违章个数")
	    @PostMapping("/countViolation")
	    public ResultDTO<ProjectLiftAlarmVO> countViolation(@RequestBody RequestDTO<ProjectLiftAlarmVO> request){
	        return service.countViolation(request.getBody());
	    }
	 @ApiOperation("查看详情")
	    @PostMapping("/earlyWarningDetail")
	    public ResultDTO<DataDTO<List<ProjectLiftAlarmVO>>> earlyWarningDetail(@RequestBody RequestDTO<ProjectLiftAlarmVO> request){
	        return service.earlyWarningDetail(request);
	    }
	 @ApiOperation("实时监控查询")
	    @PostMapping("/realTimeMonitoring")
	    public ResultDTO<DataDTO<List<ProjectLift>>> realTimeMonitoring(@RequestBody RequestDTO<ProjectLift> request){
	        return service.realTimeMonitoring(request);
	    }
	 @ApiOperation("新增短信")
	    @PostMapping(value = "/insertMessage")
	    public ResultDTO<RealTimeMonitoringTowerVo> insertMessage(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> t) {
	        try {
	            if(service.insertMessage(t.getBody())) {
	                return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
	            }
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	    }

}
