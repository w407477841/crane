package com.xywg.equipment.monitor.iot.netty.aop;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 */
@Component
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServerHandler serverHandle;

    

    /**
     * TCP协议处理
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        // 解码器
        pipeline.addLast(new Decoder());
        // 具体处理器
        pipeline.addLast(serverHandle);
        
        
    }
}


