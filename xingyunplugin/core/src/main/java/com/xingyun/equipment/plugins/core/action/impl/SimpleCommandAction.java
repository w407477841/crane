package com.xingyun.equipment.plugins.core.action.impl;

import com.xingyun.equipment.plugins.core.action.CommandAction;
import com.xingyun.equipment.plugins.core.common.Const;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:53 2019/7/9
 * Modified By : wangyifei
 */
@Slf4j
public class SimpleCommandAction implements CommandAction {

    @Override
    public void reboot(String sn) {
        log.info("下发重启指令,sn:[{}]",sn);
        if(!Const.CHANNEL_MAP.containsKey(sn)){
            throw  new RuntimeException("设备"+sn+"未上线");
        }
        Channel channel = Const.CHANNEL_MAP.get(sn);
        Map<String,Object> objectMap = new HashMap<>(16);
        objectMap.put(Const.CMD_NAME,9);
        objectMap.put(Const.SERIAL_NAME,sn);
        Const.combinationAndSend(objectMap,channel);
    }
}
