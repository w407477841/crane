package com.xingyun.equipment.crane.modular.device.model;

import java.math.BigDecimal;

public class ElectricityType {

	private String typeName;
	
	private BigDecimal electricity;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public BigDecimal getElectricity() {
		return electricity;
	}

	public void setElectricity(BigDecimal electricity) {
		this.electricity = electricity;
	}
	
	
}
