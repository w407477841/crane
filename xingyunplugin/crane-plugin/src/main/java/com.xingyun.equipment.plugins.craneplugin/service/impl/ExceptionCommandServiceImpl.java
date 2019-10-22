package com.xingyun.equipment.plugins.craneplugin.service.impl;

import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.common.Const;
import com.xingyun.equipment.plugins.core.common.enums.LogEnum;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.craneplugin.service.BaseCommandService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:42 2019/7/9
 * Modified By : wangyifei
 */
@Slf4j
public class ExceptionCommandServiceImpl extends BaseCommandService {


    public ExceptionCommandServiceImpl(CommandCallback callback) {
        super(callback);
    }

    @Override
    public void commandExec(ProtocolDTO protocolDTO,ChannelHandlerContext ctx) {
        log.info("异常信息,params:[{}]",protocolDTO.toString());
        this.callback.exceptionCallback(protocolDTO);
        Map<String,Object> objectMap = new HashMap<>(16);
        objectMap.put(Const.CMD_NAME,protocolDTO.getCommand());
        objectMap.put(Const.SERIAL_NAME,protocolDTO.getSn());
        Const.combinationAndSend(objectMap,ctx);
    }
}
