package com.xingyun.equipment.crane.modular.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dao.AlarmInfoToWebMapper;
import com.xingyun.equipment.crane.modular.device.dto.AlarmDTO;
import com.xingyun.equipment.crane.modular.device.service.AlarmInfoToWebService;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年12月18日 
*/
@Service
public class AlarmInfoToWebServiceImpl implements AlarmInfoToWebService{

	@Autowired
	private AlarmInfoToWebMapper alramMapper;
	
	@Override
	public ResultDTO<AlarmDTO> getLiftAlarmList(RequestDTO res) {
		HashMap<String, Object> map = new HashMap<>();
		List<Integer> alarmIds = new ArrayList<>();
		AlarmDTO alarmDTO = new AlarmDTO();
		Integer count=0;
        map.put("tableName","t_project_lift_alarm_" + res.getYearMonth());
        map.put("deviceNo", res.getDeviceNo());
        map.put("uuid", res.getUuid());
     /**
      *alarmId为 1,3,4,5时时报警
      */
        alarmIds.add(1);
        alarmIds.add(3);
        alarmIds.add(4);
        alarmIds.add(5);
        map.put("alarmIds", alarmIds);
         count = alramMapper.getLiftAlarmList(map);
         alarmDTO.setAlarmAmount(count);
         /**
          *alarmId为 2时是预警
          */
         alarmIds.clear();
         alarmIds.add(2);
         map.remove("alarmIds");
         map.put("alarmIds", alarmIds);
         count = alramMapper.getLiftAlarmList(map);
         alarmDTO.setWarningAmount(count);
         /**
          *alarmId为 1时既是报警也是违章
          */
         alarmIds.clear();
         alarmIds.add(1);
         map.remove("alarmIds");
         map.put("alarmIds", alarmIds);
         count = alramMapper.getLiftAlarmList(map);
         alarmDTO.setPeccancyAmount(count);
        return new  ResultDTO<>(true,alarmDTO);
	}

	@Override
	public ResultDTO<AlarmDTO> getCraneAlarmList(RequestDTO res) {
		HashMap<String, Object> map = new HashMap<>();
		List<Integer> alarmIds = new ArrayList<>();
		AlarmDTO alarmDTO = new AlarmDTO();
		Integer count=0;
        map.put("tableName","t_project_crane_alarm_" + res.getYearMonth());
        map.put("deviceNo", res.getDeviceNo());
        map.put("uuid", res.getUuid());
        /**
         *alarmId为 1,3,5,7,9,11,13,15,16时时报警
         */
           alarmIds.add(1);
           alarmIds.add(3);
           alarmIds.add(5);
           alarmIds.add(7);
           alarmIds.add(9);
           alarmIds.add(11);
           alarmIds.add(13);
           alarmIds.add(15);
           alarmIds.add(16);
           map.put("alarmIds", alarmIds);
            count = alramMapper.getCraneAlarmList(map);
            alarmDTO.setAlarmAmount(count);
            /**
             *alarmId为 2,4,6,8,10,12,14时是预警
             */
            alarmIds.clear();
            alarmIds.add(2);
            alarmIds.add(4);
            alarmIds.add(6);
            alarmIds.add(8);
            alarmIds.add(10);
            alarmIds.add(12);
            alarmIds.add(14);
            map.remove("alarmIds");
            map.put("alarmIds", alarmIds);
            count = alramMapper.getCraneAlarmList(map);
            alarmDTO.setWarningAmount(count);
            /**
             *alarmId为 1时既是报警也是违章
             */
            alarmIds.clear();
            alarmIds.add(1);
            map.remove("alarmIds");
            map.put("alarmIds", alarmIds);
            count = alramMapper.getCraneAlarmList(map);
            alarmDTO.setPeccancyAmount(count);
        return new  ResultDTO<>(true,alarmDTO);
	}

}
