package com.xywg.equipmentmonitor.modular.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.AlarmDTO;
import com.xywg.equipmentmonitor.modular.device.service.AlarmInfoToWebService;

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
