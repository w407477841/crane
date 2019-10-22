package com.xingyun.equipment.plugins.core.bootstrap;

import com.xingyun.equipment.plugins.core.common.IpUtils;
import com.xingyun.equipment.plugins.core.common.RemotingUtil;
import com.xingyun.equipment.plugins.core.config.properties.EnviromentProperties;
import com.xingyun.equipment.plugins.core.handler.BaseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty 服务启动类
 *
 * @author lxr
 * @create 2017-11-18 14:03
 **/
@Setter
@Getter
@lombok.extern.slf4j.Slf4j
public class NettyBootstrapServer implements BootstrapServer {

    private EnviromentProperties serverBean;
    private BaseHandler handler;



    public EnviromentProperties getServerBean() {
        return serverBean;
    }
    @Override
    public void setServerBean(EnviromentProperties serverBean) {
        this.serverBean = serverBean;
    }

    public BaseHandler getHandler() {
        return handler;
    }
    @Override
    public void setHandler(BaseHandler handler) {
        this.handler = handler;
    }

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;
    /**
     * 启动辅助类
     */
    ServerBootstrap bootstrap=null ;
    @Override
    public void shutdown() {

    }


    @Override
    public void start() {
        initEventPool();
        bootstrap.group(bossGroup, workGroup)
                .channel(useEpoll()?EpollServerSocketChannel.class:NioServerSocketChannel.class)
                // 地址复用
                .option(ChannelOption.SO_REUSEADDR, true)
                // Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。
                .option(ChannelOption.SO_BACKLOG,1024)

                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_RCVBUF, 10485760)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline =   ch.pipeline();
                        channelPipeline.addLast(new IdleStateHandler(serverBean.getHeart(),0,0));
                        /*
                         * 重要参数说明:
                         * 第一个参数:报文允许的最大长度
                         * 第二个参数:报文头中标识数据长度的起始位
                         * 第三个参数:报文中表示长度所占用的字节偏移量
                         * 第四个参数:添加到长度字段的补偿值
                         *          如: 原始报文为: 01 02 07 04 05 06 03   报文头为前3个  07表示长度位  表示的是一共7个字节
                         *              那么我们如果需要的一个完整报文的话  就需要左偏移 (07的位置(2)+长度所占用的字节数(1))=3
                         * 第五个参数:从解码帧中第一次去除的字节数(可用于除去报文头)
                         */
                        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 2, 7, 2, -9, 0));
                        channelPipeline.addLast(handler);
                    }
                })
                // 立即发送
                .childOption(ChannelOption.TCP_NODELAY, true)
                // Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.bind(serverBean.getPort()).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()){
                log.info("服务端启动成功【" + IpUtils.getHost() + ":" + serverBean.getPort() + "】");
            }
            else{
                log.info("服务端启动失败【" + IpUtils.getHost() + ":" + serverBean.getPort() + "】");
            }
        });
    }

    /**
     * 初始化EnentPool 参数
     */

    private void  initEventPool(){
        bootstrap= new ServerBootstrap();
        if(useEpoll()){
            bossGroup = new EpollEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_BOSS_" + index.incrementAndGet());
                }
            });
            workGroup = new EpollEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_WORK_" + index.incrementAndGet());
                }
            });

        }
        else {
            bossGroup = new NioEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "BOSS_" + index.incrementAndGet());
                }
            });
            workGroup = new NioEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "WORK_" + index.incrementAndGet());
                }
            });
        }
    }
    private boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform()
                && Epoll.isAvailable();
    }
}

