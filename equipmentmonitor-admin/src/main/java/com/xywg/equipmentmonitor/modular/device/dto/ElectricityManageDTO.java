package com.xywg.equipmentmonitor.modular.device.dto;

import java.util.List;

import com.xywg.equipmentmonitor.modular.device.model.ElectricityChangeInfo;
import com.xywg.equipmentmonitor.modular.device.model.ElectricityTrend;
import com.xywg.equipmentmonitor.modular.device.model.ElectricityType;

import lombok.Data;
@Data
public class ElectricityManageDTO {

	private List<ElectricityType> electricityTypeList;
	
	private List<ElectricityTrend> trendList;
	
	private List<ElectricityTrend> lifeTrendList;
	
	private List<ElectricityTrend> proTrendList;
	
	private ElectricityChangeInfo electricityChangeInfo;
	
}
