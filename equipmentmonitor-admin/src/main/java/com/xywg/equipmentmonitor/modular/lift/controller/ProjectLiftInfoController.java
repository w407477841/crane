package com.xywg.equipmentmonitor.modular.lift.controller;


import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftDetail;
import com.xywg.equipmentmonitor.modular.device.vo.ElectricAlarmVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.lift.model.ProjectLiftAlarmInfo;
import com.xywg.equipmentmonitor.modular.lift.service.ProjectLiftInfoService;
import com.xywg.equipmentmonitor.modular.lift.vo.ProjectLiftAlarmCountVO;
import com.xywg.equipmentmonitor.modular.lift.vo.ProjectLiftListVO;
import com.xywg.equipmentmonitor.modular.lift.vo.ProjectLiftOrgVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.testcontainers.shaded.javax.ws.rs.GET;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
@RestController
@RequestMapping("/ssdevice/lift/liftInfo")
@Api(tags = { "升降机"})
public class ProjectLiftInfoController  {

	@Autowired
	protected ProjectLiftInfoService projectLiftInfoService;
	 public String insertRole() {
	        return null;
	    }

	    public String updateRole() {
	        return null;
	    }

	    public String deleteRole() {
	        return null;
	    }

	    public String viewRole() {
	        return null;
	    }
    @ApiOperation("升降机设备列表")
    @GetMapping("getLiftInfo")
    public byte[] getLiftInfo(RequestDTO request){
    	 byte[] result = {};
    	 result= projectLiftInfoService.getLiftInfo(request);
        return result;
    }
    @ApiOperation("升降机设备列表")
    @PostMapping("getAlarmInfo")
    public byte[] getAlarmInfo(RequestDTO request){
    	 byte[] result = {};
    	 result= projectLiftInfoService.getAlarmInfo(request);
        return result;
    }
    
    /**
	  * 智慧工地拉取报警信息明细
	  * @param res
	  * @return
	  */
	 @GetMapping("/getAlarmDetail")
	    public byte[] getAlarmDetail(RequestDTO<ProjectLiftAlarmInfo> res) {
	        byte[] result = {};
	        try {
	            result =  projectLiftInfoService.getAlarmDetail(res);
	            return result;
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
    
    @ApiOperation("查询集团下所有升降机(接口)")
    @PostMapping("/getLiftList")
    public ResultDTO<ProjectLiftOrgVO> getLiftList(@RequestBody RequestDTO<ProjectLiftListVO> request) {
        return projectLiftInfoService.getLiftList(request);
    }
    @ApiOperation("查询升降机最近一条数据(接口)")
    @PostMapping("/getLiftDetail")
    public ResultDTO<ProjectLiftListVO> getLiftDetail(@RequestBody RequestDTO<ProjectLiftListVO> request) {
        return projectLiftInfoService.getLiftDetail(request);
    }
    @ApiOperation("查询升降机当月重量报警/预警 数量(接口)")
    @PostMapping("/getLiftAlarmCount")
    public ResultDTO<ProjectLiftAlarmCountVO> getLiftAlarmCount(@RequestBody RequestDTO<ProjectLiftListVO> request) {
        return projectLiftInfoService.getLiftAlarmCount(request);
    }
    /**造假数据*/
    @ApiOperation("固定返回升降机100条数据")
    @GetMapping("/getLiftDetails")
    public ResultDTO<ProjectLiftDetail> getTop100LiftDetail(String uuid, String deviceNo) {
        return projectLiftInfoService.getTop100LiftDetail(uuid,deviceNo);
    }

}

