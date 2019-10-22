package com.xywg.equipment.monitor.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author 严鹏
 * @date 2019/7/29
 */
public class NettyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
    private String host;

    private Integer port;

    private Channel channel;

    public NettyClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group) // 注册线程池
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(this.host, this.port)) // 绑定连接端口和host信息
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            LOGGER.info("正在连接中...");
                            ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                            ch.pipeline().addLast(new ClientHandler());

                        }
                    });

            ChannelFuture cf = b.connect().sync(); // 异步连接服务器
            channel = cf.channel();
            LOGGER.info("服务端连接成功..."); // 连接完成

            cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
            LOGGER.info("连接已关闭.."); // 关闭完成

        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
        }
    }

    public void sendMsg(Object o){
        if(channel != null){
            channel.writeAndFlush(o);
        }else{
            LOGGER.error("服务端连接丢失");
        }
    }
}
