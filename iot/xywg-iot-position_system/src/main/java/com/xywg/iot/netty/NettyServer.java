package com.xywg.iot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jingyun_hu
 * @date 2019/2/19
 * netty启动入口类
 */
@Component
public class NettyServer {
    @Value("${xywg.netty-position-port}")
    private String port;
    @Autowired
    private ChannelInit channelInit;

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100,400,200,TimeUnit.MILLISECONDS,new ArrayBlockingQueue(500)) ;


    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public void startNetty() {
        logger.info("netty运行开始");
        try {
            portServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("netty运行结束");
    }

    private void portServer() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 配置服务器的NIO线程租
        ServerBootstrap tcpServerBootstrap = new ServerBootstrap();
        try {
            tcpServerBootstrap.group(bossGroup, workerGroup);
            tcpServerBootstrap = tcpServer(tcpServerBootstrap, channelInit);
            bindingPort(tcpServerBootstrap);
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 配置参数
     * @param tcpServerBootstrap
     * @param channelHandler
     * @return
     */
    private ServerBootstrap tcpServer(ServerBootstrap tcpServerBootstrap, ChannelHandler channelHandler) {
        //指定NIO的模式
        tcpServerBootstrap.channel(NioServerSocketChannel.class)
                //设置TCP缓冲区  配置TCP参数，握手字符串长度设置 (最大连接数)
                .option(ChannelOption.SO_BACKLOG, 1024*10)
                .childHandler(channelHandler)
                //保持连接 开启心跳保活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return tcpServerBootstrap;
    }

    /**
     * 绑定端口
     * @param tcpServerBootstrap
     * @throws InterruptedException
     */
    private void bindingPort(ServerBootstrap tcpServerBootstrap) throws InterruptedException {
        String[] portArray = port.split(",");
        if(portArray.length<=0){
            logger.info("netty  TCP启动失败,监听端口未设置");
            return;
        }
        //TCP监听多个端口
        ChannelFuture[] channelFutures = new ChannelFuture[portArray.length];
        for (int i = 0; i < channelFutures.length; i++) {
            channelFutures[i] = tcpServerBootstrap.bind(Integer.parseInt(portArray[i])).sync();

        }
        logger.info("netty  TCP启动成功,监听端口:" + Arrays.toString(portArray));
        for (ChannelFuture channelFuture : channelFutures) {
            channelFuture.channel().closeFuture().sync();
        }
    }
}
