package com.xingyun.equipment.plugins.environmentplugin.service.impl;

import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.common.Const;
import com.xingyun.equipment.plugins.core.common.enums.LogEnum;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.environmentplugin.service.BaseCommandService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:53 2019/7/10
 * Modified By : wangyifei
 */
@Slf4j
public class CyclicCommandServiceImpl extends BaseCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicCommandServiceImpl.class);

    public CyclicCommandServiceImpl(CommandCallback callback) {
        super(callback);
    }

    @Override
    public void commandExec(ProtocolDTO protocolDTO, ChannelHandlerContext ctx) {
        log.info(LogEnum.COMMON.format("工作循环:",protocolDTO.toString()));
        this.callback.cyclicCallback(protocolDTO);
        Map<String,Object> objectMap = new HashMap<>(16);
        objectMap.put(Const.CMD_NAME,protocolDTO.getCommand());
        objectMap.put(Const.SERIAL_NAME,protocolDTO.getSn());
        Const.combinationAndSend(objectMap,ctx);
    }
}
