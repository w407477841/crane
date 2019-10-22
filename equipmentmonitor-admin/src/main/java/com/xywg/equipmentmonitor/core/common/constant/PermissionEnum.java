package com.xywg.equipmentmonitor.core.common.constant;
/**
* @author: wangyifei
* Description:
* Date: 16:15 2018/9/13
*/
public enum PermissionEnum{
	/**用户 -权限*/
	USER_INSERT("system:user:insert"),
	USER_UPDATE("system:user:update"),
	USER_DELETE("system:user:delete"),
	USER_VIEW("system:user:view"),
	/**角色 权限*/
	ROLE_INSERT("system:role:insert"),
	ROLE_UPDATE("system:role:update"),
	ROLE_DELETE("system:role:update"),
	ROLE_VIEW("system:role:view")

	;
	String el;

	private PermissionEnum(String el) {
		this.el = el;
	}

	public String getEl() {
		return el;
	}

	
	
	
}
