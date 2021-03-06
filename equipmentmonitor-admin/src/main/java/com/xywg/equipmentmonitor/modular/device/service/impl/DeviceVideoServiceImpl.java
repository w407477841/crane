package com.xywg.equipmentmonitor.modular.device.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xywg.equipmentmonitor.modular.device.service.DeviceVideoService;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年11月22日 
*/
@Service
public class DeviceVideoServiceImpl implements DeviceVideoService{

	@Override
	public String getYSYCameraInfo(String appKey, String secret,Integer pageSize,Integer pageIndex) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("appKey", appKey);
		paramMap.put("appSecret", secret);

		String resultStr = HttpUtil.post("https://open.ys7.com/api/lapp/token/get", paramMap);
		
		JSONObject json = JSONObject.fromObject(resultStr);
		String token = JSONObject.fromObject(json.getString("data")).getString("accessToken");

		Map<String, Object> map = new HashMap<>();
		map.put("accessToken", token);
		map.put("pageStart", pageIndex);
		map.put("pageSize", pageSize);
		String dataList = HttpRequest.post("https://open.ys7.com/api/lapp/live/video/list")
				.header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded").form(map).execute().body();

		return dataList;
	}

}
