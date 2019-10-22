package com.xywg.equipment.monitor.modular.onlinetest.service.impl;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.modular.onlinetest.dto.RemoteDTO;
import com.xywg.equipment.monitor.modular.onlinetest.service.OnlineTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:05 2019/1/21
 * Modified By : wangyifei
 */
@Component
public class HfTdImpl implements OnlineTestService {

    @Autowired
    private ZbusProducerHolder  zbusProducerHolder  ;
    @Override
    public void test(String v,String type, String sn,String data) {
        RemoteDTO remoteDTO  =new RemoteDTO();
        remoteDTO.setTopic("/topic/"+v+"/"+type+"/"+sn);
        remoteDTO.setData(data);
        zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
    }
}
