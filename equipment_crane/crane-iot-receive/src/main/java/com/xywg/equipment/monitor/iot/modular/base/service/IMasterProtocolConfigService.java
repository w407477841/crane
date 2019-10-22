package com.xywg.equipment.monitor.iot.modular.base.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import com.xywg.equipment.monitor.iot.modular.base.model.MasterProtocolConfig;

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
	MasterProtocolConfigDTO protocolConfig(String data);

}
