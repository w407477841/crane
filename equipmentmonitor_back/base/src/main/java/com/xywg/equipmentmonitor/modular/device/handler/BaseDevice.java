package com.xywg.equipmentmonitor.modular.device.handler;

/**
 *  设备基类
 */
public abstract class BaseDevice {
	
	//abstract public void handler(String data ,ZbusProducerHolder producerHolder ,MasterProtocolConfigDTO config);
	
	protected String replaceKey(String data, String oldkey,String newKey){
		return data.replace(","+oldkey+":",","+ newKey+":");
	}  
	protected String replaceHead(String data, String oldkey,String newKey){
		return data.replace(oldkey+":", newKey+":");
	}
	/**
	 * 出去  :,   和 :结尾的
	 */
	protected String removeSpace(String data){
    	data=data.replace(":,", ":0,");//去除空
    	if(data.endsWith(":")){//尾部去除空
    		data += "0";
    	}
    	
    	return data;
	}
	
}
