package com.xingyun.equipment.simpleequipment.receive;

import com.xingyun.equipment.simpleequipment.core.properties.XywgProerties;
import com.xingyun.equipment.simpleequipment.receive.handler.MonitorHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:49 2019/7/30
 * Modified By : wangyifei
 */
@Slf4j
public class NettyServer {
    private final XywgProerties xywgProjerties;
    private final MonitorHandler monitorHandler;

    public NettyServer(XywgProerties xywgProjerties, MonitorHandler monitorHandler) {
        this.xywgProjerties = xywgProjerties;
        this.monitorHandler = monitorHandler;
    }

    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 环境监控(扬尘,塔吊) 数据接收服务线程池(TCP传输方式)
                 * 初始化用于Acceptor的主"线程池"以及用于I/O工作的从"线程池"
                 */
                EventLoopGroup monitorBossGroup = new NioEventLoopGroup();
                EventLoopGroup monitorWorkerGroup = new NioEventLoopGroup();
                /**
                 初始化ServerBootstrap实例， 此实例是netty服务端应用开发的入口
                 */
                ServerBootstrap monitorServerBootstrap = new ServerBootstrap();
                monitorServerBootstrap.group(monitorBossGroup,monitorWorkerGroup);
                /**---------------↑-----------------线程池------------------↑----------------**/

                /**---------------↓------------------端口绑定------------------↓----------------**/
                try {
                    /**
                     * 指定通道channel的类型，由于是服务端，故而是NioServerSocketChannel
                     */
                    monitorServerBootstrap.channel(NioServerSocketChannel.class)
                            /**
                             *配置ServerSocketChannel参数，握手字符串长度设置 (最大连接数)
                             */
                            .option(ChannelOption.SO_BACKLOG, 1024*10)
                            /**
                             * 设置子通道也就是SocketChannel的处理器， 其内部是实际业务在这里完成
                             */
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();

                                    //心跳检测机制  5*60秒没有收到任何数据时,服务器将主动断开连接
                                    pipeline.addLast(new IdleStateHandler(5*60, 0, 0, TimeUnit.SECONDS));
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
                                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 2, 7, 2, -9, 0));
                                    // 具体处理器 真正的数据处理
                                    pipeline.addLast(monitorHandler);
                                }
                            })
                            /**
                             * //配置子通道也就是SocketChannel的选项,保持连接 开启心跳保活机制，就是客户端、
                             *                 // 服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                             */
                            .childOption(ChannelOption.SO_KEEPALIVE, true);
                            Integer  nettyPort =   xywgProjerties.getNettyPort();
                            ChannelFuture channelFuture = monitorServerBootstrap.bind(nettyPort).sync();
                            log.info("启动成功 绑定[{}]",nettyPort);
                            channelFuture.channel().closeFuture().sync();

                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("启动失败");
                }
                finally {
                    // 优雅退出，释放线程池资源
                    monitorBossGroup.shutdownGracefully();
                    monitorWorkerGroup.shutdownGracefully();
                }
            }
        }).start();
    }
}
