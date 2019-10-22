package com.xywg.attendance.modular.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * @author hjy
 * @date 2019/2/21
 * netty 启动入口主类
 */
@Service
public class NettyServer implements ApplicationRunner {
    @Value("${xywg.monitor-port}")
    private String port;

    @Autowired
    private ChannelInit channelInit;

    private Logger logger = LoggerFactory.getLogger(NettyServer.class);


    @Override
    public void run(ApplicationArguments args){
        logger.info("netty运行开始");
        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("netty运行结束");
    }


    private void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //.childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(channelInit);
            ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
            logger.info("netty启动成功,监听端口:"+port);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
