package com.xywg.iot.modules.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author hjy
 */
@Component
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    @Autowired
    private CraneHandler craneHandler;



    /**
     * TCP协议处理
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        //心跳检测机制  15分钟没有收到任何数据时,服务器将主动断开连接
        pipeline.addLast(new IdleStateHandler(10*60, 0, 0, TimeUnit.SECONDS));
       //解码
        pipeline.addLast(new CraneDecoder());
        // 具体处理器 真正的数据处理
        pipeline.addLast(craneHandler);
    }
}


