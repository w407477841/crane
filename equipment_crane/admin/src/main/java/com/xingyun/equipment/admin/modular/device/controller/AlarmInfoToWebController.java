package com.xingyun.equipment.admin.modular.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dto.AlarmDTO;
import com.xingyun.equipment.admin.modular.device.service.AlarmInfoToWebService;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年12月18日 
*/
@RestController
@RequestMapping("ssdevice/rfid/device")
public class AlarmInfoToWebController {

	@Autowired
	private AlarmInfoToWebService alarmService;
	
	@GetMapping("liftAlarm/getAlarmList")
	public ResultDTO<AlarmDTO> getLiftAlarmList(RequestDTO res){
	 return	alarmService.getLiftAlarmList(res);
		
	}
	
	@GetMapping("craneAlarm/getAlarmList")
	public ResultDTO<AlarmDTO> getCraneAlarmList(RequestDTO res){
		return alarmService.getCraneAlarmList(res);
		
	}
}
