package com.xywg.equipment.monitor.iot.netty.common.enums;

import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.iot.modular.base.handler.BaseDevice;
import com.xywg.equipment.monitor.iot.modular.crane.handle.CraneDevice;
import com.xywg.equipment.monitor.iot.modular.envmon.handle.EnvironmentMonitorDevice;
import com.xywg.equipment.monitor.iot.modular.lift.handle.LiftDevice;

/**
 * 所有设备
 * @author 王一飞
 *
 */
public enum DeviceEnum {
	/**扬尘设备*/
	sdsyr("扬尘",EnvironmentMonitorDevice.class,"t_project_environment_monitor_detail"),
	dltytj("塔吊",CraneDevice.class,"t_project_crane_detail"),
	dltysjj("升降机",LiftDevice.class,"t_project_lift_detai"),
	ycht("扬尘",EnvironmentMonitorDevice.class,"t_project_environment_monitor_detail"),
	tdht("塔吊",CraneDevice.class,"t_project_crane_detail"),
	sjjht("升降机",LiftDevice.class,"t_project_lift_detai");
	
	private DeviceEnum(String key, Class<? extends BaseDevice> device, String tableName) {
		this.key = key;
		this.device = device;
		this.tableName = tableName;
	}
	String key;
	Class<? extends BaseDevice> device;
	String tableName;
	public String getKey() {
		return key;
	}

	public String getTableName(){
		return tableName;
	}
	
	public Class<? extends BaseDevice> getDevice() {
		return device;
	}


/**返回 设备类*/
	public static Class<? extends BaseDevice> getDeviceClass(String key){
		if(StrUtil.isEmpty(key)) {
			return null;
		}
		for(DeviceEnum de : DeviceEnum.values() ){
			if(key.equals(de.getKey())){
				return de.getDevice();
			}
		}
		return null;
	}

/***/
	public static DeviceEnum getDeviceEnum(String key){
		if(StrUtil.isEmpty(key)) {
			return null;
		}
		for(DeviceEnum de : DeviceEnum.values() ){
			if(key.equals(de.getKey())){
				return de;
			}
		}
		return null;
		
		
	}

}
