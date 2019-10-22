package com.xingyun.equipment.plugins.craneplugin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.common.Const;
import com.xingyun.equipment.plugins.core.common.enums.LogEnum;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.core.libraryserver.XingyunCall;
import com.xingyun.equipment.plugins.craneplugin.service.BaseCommandService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:35 2019/7/9
 * Modified By : wangyifei
 */
@Slf4j
public class HeatbeatCommandServiceImpl extends BaseCommandService {


    public HeatbeatCommandServiceImpl(CommandCallback callback) {
        super(callback);
    }

    @Override
    public void commandExec(ProtocolDTO protocolDTO,ChannelHandlerContext ctx) {
        log.info("心跳数据,params:[{}]",protocolDTO.toString());
        this.callback.heartbeatCallback(protocolDTO);
        Map<String,Object> objectMap = new HashMap<>(16);
        objectMap.put(Const.CMD_NAME,protocolDTO.getCommand());
        objectMap.put(Const.SERIAL_NAME,protocolDTO.getSn());
        Const.combinationAndSend(objectMap,ctx);
    }
}
