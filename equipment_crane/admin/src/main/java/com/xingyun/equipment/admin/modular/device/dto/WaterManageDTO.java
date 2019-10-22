package com.xingyun.equipment.admin.modular.device.dto;

import java.util.List;

import com.xingyun.equipment.admin.modular.device.model.WaterChangeInfo;
import com.xingyun.equipment.admin.modular.device.model.WaterTrend;
import com.xingyun.equipment.admin.modular.device.model.WaterType;

import lombok.Data;
@Data
public class WaterManageDTO {

	private List<WaterType> waterTypeList;
	
	private List<WaterTrend> trendList;
	
	private List<WaterTrend> lifeTrendList;
	
	private List<WaterTrend> proTrendList;
	
	private List<WaterTrend> fireTrendList;
	
	private WaterChangeInfo waterChangeInfo;
	
}
