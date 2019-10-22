package com.xywg.equipment.monitor.iot.modular.helmet.service;


import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.core.annotion.SocketCommand;
import com.xywg.equipment.monitor.iot.modular.helmet.model.DataDomain;

/**
 * @author hjy
 */
public interface DataHandleService {

	/**
	 * 设备下线
	 * @param serverDomain
	 */
	@SocketCommand(command = GlobalStaticConstant.TERMINAL_DOWN)
	void terminalDown(DataDomain serverDomain);

	/**
	 * 设备登录
	 * @param dataDomain
	 */
	@SocketCommand(command = GlobalStaticConstant.LOGIN)
	void login(DataDomain dataDomain);

	/**
	 * 健康定位
	 * @param dataDomain
	 */
	@SocketCommand(command = GlobalStaticConstant.POSITION_HEALTH)
	void positionHealth(DataDomain dataDomain);
	
}
