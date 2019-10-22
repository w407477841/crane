package com.xywg.equipment.monitor.iot.modular.base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.modular.base.dao.MasterProtocolConfigMapper;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import com.xywg.equipment.monitor.iot.modular.base.model.MasterProtocolConfig;
import com.xywg.equipment.monitor.iot.modular.base.service.IMasterProtocolConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        List<Map<String, String>> protocols = baseMapper.getProtocols();
        for (Map<String, String> protocol : protocols) {
            String head = protocol.get("head");
            if (data.startsWith(head)) {
                return MasterProtocolConfigDTO.factory(protocol);
            }
        }
        throw new RuntimeException("数据[" + data + "]未匹配到配置");
    }


}
