package com.xywg.equipmentmonitor.modular.device.model;

import java.math.BigDecimal;

public class WaterType {

	private String typeName;
	
	private BigDecimal water;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public BigDecimal getWater() {
		return water;
	}

	public void setWater(BigDecimal water) {
		this.water = water;
	}
	
	
}
