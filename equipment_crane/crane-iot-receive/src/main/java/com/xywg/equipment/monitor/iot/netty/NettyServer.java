package com.xywg.equipment.monitor.iot.netty;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import com.xywg.equipment.monitor.iot.netty.aop.UdpServerHandler;
import com.xywg.equipment.monitor.iot.netty.device.crane.CraneBusinessLogicService;
import com.xywg.equipment.monitor.iot.netty.device.dto.RequestDTO;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.MonitorChannelInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.zbus.kit.logging.Logger;
import io.zbus.kit.logging.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author jingyun_hu
 * @date 2018/3/20
 * netty启动入口类
 */
@SuppressWarnings("all")
@Slf4j
@Service
public class NettyServer implements ApplicationRunner {
    @Autowired
    private XywgProerties xywgProerties;

    @Autowired
    private UdpServerHandler udpNettyServer;
    @Autowired
    private MonitorChannelInit monitorChannelInit;

    @Autowired
    private IProjectCraneService projectCraneService;
    @Autowired
    private CraneBusinessLogicService craneBusinessLogicService;
    @Autowired
    private CommonMethod commonMethod;

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
                /**---------------↑-----------------线程池------------------↑----------------**/

                /**---------------↓------------------端口绑定------------------↓----------------**/
                try {
                    //心跳端口
                    int udpPort = xywgProerties.getNettyHeartPort();
                    //通过ServerBootstrap的group方法，设置初始化的主从"线程池"
                    monitorServerBootstrap.group(monitorBossGroup, monitorWorkerGroup);
                    monitorServerBootstrap = tcpServer(monitorServerBootstrap, monitorChannelInit);
                    Integer[] monitorPorts = xywgProerties.getNettyMonitorServerPorts();
                    ChannelFuture[] monitorChannelFuture = new ChannelFuture[monitorPorts.length];
                    if (monitorPorts != null && monitorPorts.length > 0) {
                        for (int i = 0; i < monitorChannelFuture.length; i++) {
                            //绑定并侦听某个端口
                            monitorChannelFuture[i] = monitorServerBootstrap.bind(monitorPorts[i]).sync();
                        }
                        logger.info("netty  TCP(塔吊)启动成功,监听端口:" + Arrays.toString(monitorPorts));
                    }
                    /**---------------↑--------------端口绑定---------------↑--------------**/

                    /**
                     * 同步结束,关闭通道
                     */
                    for (ChannelFuture channelFuture : monitorChannelFuture) {
                        channelFuture.channel().closeFuture().sync();
                    }


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



    private ServerBootstrap tcpServer(ServerBootstrap tcpServerBootstrap, ChannelHandler channelHandler) {
        /**
         * 指定通道channel的类型，由于是服务端，故而是NioServerSocketChannel
         */
        tcpServerBootstrap.channel(NioServerSocketChannel.class)
                /**
                 *配置ServerSocketChannel参数，握手字符串长度设置 (最大连接数)
                 */
                .option(ChannelOption.SO_BACKLOG, 1024*10)
                /**
                 * 设置子通道也就是SocketChannel的处理器， 其内部是实际业务在这里完成
                 */
                .childHandler(channelHandler)
                /**
                 * //配置子通道也就是SocketChannel的选项,保持连接 开启心跳保活机制，就是客户端、
                 *                 // 服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                 */
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return tcpServerBootstrap;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("维护所有在线设备的状态");
        RequestDTO requestDTO =new RequestDTO(null,"0017",null);
        commonMethod.send2Mq(JSONUtil.toJsonStr(requestDTO));
        startTcpNetty();
    }
}
