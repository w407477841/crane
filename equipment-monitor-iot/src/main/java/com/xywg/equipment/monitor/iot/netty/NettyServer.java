package com.xywg.equipment.monitor.iot.netty;

import cn.hutool.core.convert.Convert;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.netty.aop.ChannelInit;
import com.xywg.equipment.monitor.iot.netty.aop.HexPowerHandler;
import com.xywg.equipment.monitor.iot.netty.aop.HexWaterHandler;
import com.xywg.equipment.monitor.iot.netty.aop.UdpServerHandler;
import com.xywg.equipment.monitor.iot.netty.device.handler.MonitorChannelInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.zbus.kit.logging.Logger;
import io.zbus.kit.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author jingyun_hu
 * @date 2018/3/20
 * netty启动入口类
 */
@SuppressWarnings("all")
public class NettyServer {
    @Autowired
    private XywgProerties xywgProerties;

    @Autowired
    private ChannelInit channelInit;
    @Autowired
    private UdpServerHandler udpNettyServer;
    @Autowired
    private HexPowerHandler hexPowerHandler;
    @Autowired
    private HexWaterHandler  hexWaterHandler;
    @Autowired
    private MonitorChannelInit monitorChannelInit;


    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public void startTcpNetty() {
        logger.info("netty运行开始");
        try {
            portServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("netty运行结束");
    }

    private void portServer() throws InterruptedException {
        /**---------------↓------------------线程池------------------↓----------------**/
        /**
         * TCP  线程池
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                // 配置服务器的NIO线程租
                ServerBootstrap tcpServerBootstrap = new ServerBootstrap();

                /**
                 *  HEX 电表
                 */
                EventLoopGroup hexPowerBossGroup = new NioEventLoopGroup();
                EventLoopGroup hexPowerWorkerGroup = new NioEventLoopGroup();
                ServerBootstrap hexPowerTcpServerBootstrap = new ServerBootstrap();

                /**
                 *  HEX 水表表
                 */
                EventLoopGroup hexWaterBossGroup = new NioEventLoopGroup();
                EventLoopGroup hexWaterWorkerGroup = new NioEventLoopGroup();
                ServerBootstrap hexWaterTcpServerBootstrap = new ServerBootstrap();


                /**
                 * 环境监控(扬尘,塔吊) 数据接收服务线程池(TCP传输方式)
                 */
                EventLoopGroup monitorBossGroup = new NioEventLoopGroup();
                EventLoopGroup monitorWorkerGroup = new NioEventLoopGroup();
                ServerBootstrap monitorServerBootstrap = new ServerBootstrap();


                /**
                 * UDP 线程池
                 */
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
                Bootstrap udpBootstrap = new Bootstrap();
                /**---------------↑-----------------线程池------------------↑----------------**/

                /**---------------↓------------------端口绑定------------------↓----------------**/
                try {
                    tcpServerBootstrap.group(bossGroup, workerGroup);
                    tcpServerBootstrap = tcpServer(tcpServerBootstrap, channelInit);
                    int[] portArray = xywgProerties.getNettyServerPorts();
                    int udpPort = xywgProerties.getNettyHeartPort();
                    //TCP监听多个端口
                    ChannelFuture[] channelFutures = new ChannelFuture[portArray.length];
                    for (int i = 0; i < channelFutures.length; i++) {
                        channelFutures[i] = tcpServerBootstrap.bind(portArray[i]).sync();

                    }
                    logger.info("netty  TCP启动成功,监听端口:" + Arrays.toString(portArray));

                    // 16进制解析(电表)
                    hexPowerTcpServerBootstrap.group(hexPowerBossGroup, hexPowerWorkerGroup);
                    hexPowerTcpServerBootstrap = tcpServer(hexPowerTcpServerBootstrap, hexPowerHandler);
                    int[] hexPorts = xywgProerties.getNettyPowerHexPorts();
                    ChannelFuture[] hexChannelFuture = new ChannelFuture[hexPorts.length];
                    if (hexPorts != null && hexPorts.length > 0) {
                        for (int i = 0; i < hexChannelFuture.length; i++) {
                            hexChannelFuture[i] = hexPowerTcpServerBootstrap.bind(hexPorts[i]).sync();
                        }
                        logger.info("netty  电表 TCP启动成功,监听端口:" + Arrays.toString(hexPorts));
                    }

                    // 16进制解析(电表)
                    hexWaterTcpServerBootstrap.group(hexWaterBossGroup, hexWaterWorkerGroup);
                    hexWaterTcpServerBootstrap = tcpServer(hexWaterTcpServerBootstrap, hexWaterHandler);
                    int[] hexWaterPorts = xywgProerties.getNettyWaterHexPorts();
                    ChannelFuture[] hexWaterChannelFuture = new ChannelFuture[hexPorts.length];
                    if (hexPorts != null && hexPorts.length > 0) {
                        for (int i = 0; i < hexWaterChannelFuture.length; i++) {
                            hexWaterChannelFuture[i] = hexWaterTcpServerBootstrap.bind(hexWaterPorts[i]).sync();
                        }
                        logger.info("netty  水表 TCP启动成功,监听端口:" + Arrays.toString(hexWaterPorts));
                    }


                    // 16进制解析 环境监控 (扬尘,塔吊)
                    monitorServerBootstrap.group(monitorBossGroup, monitorWorkerGroup);
                    monitorServerBootstrap = tcpServer(monitorServerBootstrap, monitorChannelInit);
                    Integer[] monitorPorts = xywgProerties.getNettyMonitorServerPorts();
                    ChannelFuture[] monitorChannelFuture = new ChannelFuture[monitorPorts.length];
                    if (monitorPorts != null && monitorPorts.length > 0) {
                        for (int i = 0; i < monitorChannelFuture.length; i++) {
                            monitorChannelFuture[i] = monitorServerBootstrap.bind(monitorPorts[i]).sync();
                        }
                        logger.info("netty  TCP(环境监控)启动成功,监听端口:" + Arrays.toString(monitorPorts));
                    }


                    /**
                     * UDP
                     */
                    udpBootstrap.group(eventLoopGroup);
                    udpBootstrap = udpServer(udpBootstrap);
                    ChannelFuture udpChannelFuture = udpBootstrap.bind(Convert.toInt(udpPort)).sync();
                    logger.info("netty Udp启动成功,监听端口:" + udpPort);
                    /**---------------↑--------------端口绑定---------------↑--------------**/

                    /**
                     * 同步结束,关闭通道
                     */
                    for (ChannelFuture channelFuture : channelFutures) {
                        channelFuture.channel().closeFuture().sync();
                    }
                    for (ChannelFuture channelFuture : hexChannelFuture) {
                        channelFuture.channel().closeFuture().sync();
                    }
                    for (ChannelFuture channelFuture : monitorChannelFuture) {
                        channelFuture.channel().closeFuture().sync();
                    }

                    udpChannelFuture.channel().closeFuture().await();

                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("启动失败");
                }
                finally {
                    // 优雅退出，释放线程池资源
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();

                    hexPowerBossGroup.shutdownGracefully();
                    hexPowerWorkerGroup.shutdownGracefully();

                    hexWaterBossGroup.shutdownGracefully();
                    hexWaterWorkerGroup.shutdownGracefully();

                    monitorBossGroup.shutdownGracefully();
                    monitorWorkerGroup.shutdownGracefully();

                    eventLoopGroup.shutdownGracefully();
                }
            }
        }).start();


    }

    private Bootstrap udpServer(Bootstrap udpBootstrap) {
        udpBootstrap.channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(udpNettyServer);
        return udpBootstrap;
    }

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


}
