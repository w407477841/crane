package com.xywg.equipmentmonitor.modular.device.dto;

import java.util.Map;

public class MasterProtocolConfigDTO {

	private String head;
	
	private String key ;
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}



	public static MasterProtocolConfigDTO factory( Map<String, String>  config){
		
		MasterProtocolConfigDTO  dto = new MasterProtocolConfigDTO();
		dto.setHead(config.get("head"));
		dto.setKey(config.get("key"));
		
		return dto;
	}
	
}
