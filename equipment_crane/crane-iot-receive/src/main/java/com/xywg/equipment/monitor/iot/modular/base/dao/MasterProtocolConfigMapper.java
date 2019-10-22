package com.xywg.equipment.monitor.iot.modular.base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.base.model.MasterProtocolConfig;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
public interface MasterProtocolConfigMapper extends BaseMapper<MasterProtocolConfig> {
	/**查询 协议头  及对应的设备类型*/
@Select("select head ,   (select name from  t_project_master_device_type  where id =t_project_master_protocol_config.type  ) `key` from t_project_master_protocol_config where  is_del = 0")
	public List<Map<String,String>>   getProtocols();
	
}
