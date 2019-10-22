package com.xywg.equipmentmonitor.modular.device.service;

import com.xywg.equipmentmonitor.modular.device.dto.MasterProtocolConfigDTO;
import com.xywg.equipmentmonitor.modular.device.model.MasterProtocolConfig;


import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
public interface IMasterProtocolConfigService extends IService<MasterProtocolConfig> {
	/**  传入数据  ，返回协议头对应的协议配置, 未匹配返回空   */
	public MasterProtocolConfigDTO protocolConfig(String data);

}
