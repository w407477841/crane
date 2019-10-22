package com.xywg.equipmentmonitor.modular.device.model;

import java.math.BigDecimal;

public class WaterTrend {

	private String trendDate;
	
	private BigDecimal water;

	public String getTrendDate() {
		return trendDate;
	}

	public void setTrendDate(String trendDate) {
		this.trendDate = trendDate;
	}

	public BigDecimal getWater() {
		return water;
	}

	public void setWater(BigDecimal water) {
		this.water = water;
	}

	
	
}
