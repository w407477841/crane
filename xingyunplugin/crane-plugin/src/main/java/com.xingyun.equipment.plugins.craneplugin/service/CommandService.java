package com.xingyun.equipment.plugins.craneplugin.service;

import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:44 2019/7/8
 * Modified By : wangyifei
 */
public interface CommandService{
    /** 执行入库操作 */
    void commandExec(ProtocolDTO protocolDTO,ChannelHandlerContext ctx);

}
