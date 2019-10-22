package com.xywg.equipmentmonitor.core.security.enums;

public enum LoginType {
	//手机登录
	phone(1),
	//用户名登录
	username(2);
	
	private int type;

	private LoginType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
}
