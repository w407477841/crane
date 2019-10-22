package com.xywg.equipment.monitor.core.netty;

import com.xywg.equipment.monitor.modular.whf.init.WhfChannelInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:13 2018/10/15
 * Modified By : wangyifei
 */
@Component
public class TimeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServer.class);
    @Autowired
    private WhfChannelInit timeChannelInit;


    public void bind(int port)throws Exception{
        // 配置服务器端的NIO线程组
        EventLoopGroup   bossGroup  =new NioEventLoopGroup();
        EventLoopGroup  workGroup  =new NioEventLoopGroup();
        try {
            //
        ServerBootstrap  bootstrap  =  new ServerBootstrap();

        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(timeChannelInit);
        //绑定端口，同步等待成功
        ChannelFuture channelFuture  =bootstrap.bind(port).sync();
            LOGGER.info("#####################");
            LOGGER.info("######["+port+"]绑定 成功#######");
            LOGGER.info("#####################");
        //等待服务端监听端口关闭
       channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        LOGGER.info("#####################");
        LOGGER.info("######优雅退出#######");
        LOGGER.info("#####################");
        }
    }


}
