package com.xywg.equipmentmonitor.modular.device.model;

import java.math.BigDecimal;

public class ElectricityTrend {

	private String trendDate;
	
	private BigDecimal electricity;

	public String getTrendDate() {
		return trendDate;
	}

	public void setTrendDate(String trendDate) {
		this.trendDate = trendDate;
	}

	public BigDecimal getElectricity() {
		return electricity;
	}

	public void setElectricity(BigDecimal electricity) {
		this.electricity = electricity;
	}

	
	
	
}
