package com.xywg.equipmentmonitor.modular.device.service.impl;

import com.xywg.equipmentmonitor.modular.device.model.MasterProtocolConfig;
import com.xywg.equipmentmonitor.modular.device.dao.MasterProtocolConfigMapper;
import com.xywg.equipmentmonitor.modular.device.dto.MasterProtocolConfigDTO;
import com.xywg.equipmentmonitor.modular.device.service.IMasterProtocolConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
@Service
public class MasterProtocolConfigServiceImpl extends ServiceImpl<MasterProtocolConfigMapper, MasterProtocolConfig> implements IMasterProtocolConfigService {
/**
 * 返回 匹配的协议配置
 */

@Override
public MasterProtocolConfigDTO protocolConfig(String data) {
	List<Map<String,String>>  protocols =  baseMapper.getProtocols();
	for(Map<String,String> protocol :  protocols ){
		 String head  =  protocol.get("head");
		 if(data.startsWith(head)){
			 return MasterProtocolConfigDTO.factory(protocol);
		 }
	}
	
	throw new RuntimeException( "数据["+data+"]未匹配到配置");
}

	


}
