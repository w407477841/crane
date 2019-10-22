package com.xywg.equipmentmonitor.modular.device.service;

import org.springframework.stereotype.Service;

import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.AlarmDTO;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年12月18日 
*/
@Service
public interface AlarmInfoToWebService {

	/**
	 * 获取升降机报警信息
	 * @param res
	 * @return
	 */
	ResultDTO<AlarmDTO> getLiftAlarmList(RequestDTO res);

	/**
	 * 获取塔吊报警信息
	 * @param res
	 * @return
	 */
	ResultDTO<AlarmDTO> getCraneAlarmList(RequestDTO res);

	

	
}
