package com.xywg.equipment.monitor.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author 严鹏
 * @date 2019/7/29
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    private NettyClientService service;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        //打印服务端的发送数据
        System.out.println(s);
    }


    // TODO 断线重连


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("exceptionCaught");
        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(()->{
            System.out.println("尝试重连");
            service.init();
        },10L,TimeUnit.SECONDS);
    }
}
