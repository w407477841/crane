package com.xywg.equipment.monitor.iot.netty.aop;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.zbus.kit.logging.Logger;
import io.zbus.kit.logging.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 
 * 数据接收
 * 
 */
@Component
@ChannelHandler.Sharable
@SuppressWarnings("all")
public class HeartbeatHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
    private Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);


    /**
     * @param ctx     通道处理的上下文信息
     * @param message 接收的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf  message) {

        logger.info("<<<<<<message>>>>>>>");
        logger.info(getMessage(message));


    }
    private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

