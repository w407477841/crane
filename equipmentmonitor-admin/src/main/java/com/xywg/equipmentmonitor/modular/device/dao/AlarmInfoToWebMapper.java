package com.xywg.equipmentmonitor.modular.device.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.xywg.equipmentmonitor.modular.device.dto.AlarmDTO;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年12月18日 
*/
@Mapper
public interface AlarmInfoToWebMapper {

	/**
	 * 升降机数量
	 * @param map
	 * @return
	 */
	Integer getLiftAlarmList(HashMap<String, Object> map);

	/**
	 * 塔吊报警数量统计
	 * @param map
	 * @return
	 */
	Integer getCraneAlarmList(HashMap<String, Object> map);

}
