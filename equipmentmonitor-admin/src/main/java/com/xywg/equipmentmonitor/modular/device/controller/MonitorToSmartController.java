package com.xywg.equipmentmonitor.modular.device.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;
import com.xywg.equipmentmonitor.modular.device.vo.MonitorAlarmVO;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年8月28日 
*/
@RestController
@RequestMapping("ssdevice/environment/environmentInfo")
public class MonitorToSmartController extends BaseController<ProjectEnvironmentMonitor, ProjectEnvironmentMonitorService>{

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
	 * 智慧工地拉取用接口
	 * @param res
	 * @return
	 */
	 @GetMapping("/getEnvironmentInfo")
	    public byte[] getEnvironmentInfo(RequestDTO<MonitorAlarmVO> res) {
	        byte[] result = {};
	        try {
	            result =  service.getEnvironmentInfo(res);
	           
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	 /**
	  * 智慧工地拉取报警信息
	  * @param res
	  * @return
	  */
	 @PostMapping("/getAlarmInfo")
	    public byte[] getAlarmInfo(RequestDTO<MonitorAlarmVO> res) {
	        byte[] result = {};
	        try {
	            result =  service.getAlarmInfo(res);
	            return result;
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	 
	 /**
	  * 智慧工地拉取报警信息明细
	  * @param res
	  * @return
	  */
	 @GetMapping("/getAlarmDetail")
	    public byte[] getAlarmDetail(RequestDTO<MonitorAlarmVO> res) {
	        byte[] result = {};
	        try {
	            result =  service.getAlarmDetail(res);
	            return result;
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

}
