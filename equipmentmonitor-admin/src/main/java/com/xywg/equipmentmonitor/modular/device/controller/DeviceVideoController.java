package com.xywg.equipmentmonitor.modular.device.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.modular.device.service.DeviceVideoService;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年11月22日 
*/
@RestController
@RequestMapping("/ssdevice/device/cameraInfo")
public class DeviceVideoController {

	@Autowired
	private DeviceVideoService deviceVideoService;
	
	@ApiOperation("萤石云")
	@GetMapping("getYSCameraInfo")
	public Object getYSYCameraInfo(String appKey,String secret,Integer pageSize,Integer pageIndex) {
		String data =deviceVideoService.getYSYCameraInfo(appKey,secret, pageSize, pageIndex);
		System.out.println(data);
		JSONObject result = JSONObject.fromObject(data);
		System.out.println(result);
		return result;
	}
}
