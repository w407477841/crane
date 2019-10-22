package com.xywg.equipment.monitor.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author 严鹏
 * @date 2019/7/31
 */
public class NettyConnectionListener implements ChannelFutureListener {

    @Autowired
    private NettyClientService nettyClientService;

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(() -> {
                System.err.println("netty服务端链接不上，开始重连操作...");
                // 重连
                nettyClientService.init();
            }, 1L, TimeUnit.SECONDS);
        } else {
            System.out.println("netty服务端链接成功...");
        }
    }
}
