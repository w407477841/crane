package com.xywg.equipment.sandplay.config;

import com.xywg.equipment.sandplay.nettty.ChannelInit;
import com.xywg.equipment.sandplay.nettty.handler.SandplayHandler;
import com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit;
import com.xywg.equipment.sandplay.nettty.weight.WeightChannelInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:47 2018/9/21
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "xywg.netty")
public class NettyConfig implements ApplicationRunner {
    /**端口*/
    Integer [] ports ;
    /**称重端口*/
    Integer [] weightPorts;
    /**  二合一端口 */
    Integer [] allPorts;


    /**是服务端还是客户端*/
    boolean   isServer ;


    @Autowired
    private ChannelInit  channelInit;
    @Autowired
    private AllChannelInit   allChannelInit;

    @Autowired
    private WeightChannelInit   weightChannelInit;

    public Integer[] getAllPorts() {
        return allPorts;
    }

    public void setAllPorts(Integer[] allPorts) {
        this.allPorts = allPorts;
    }

    private static final Logger logger = LoggerFactory.getLogger(NettyConfig.class);

    public Integer[] getWeightPorts() {
        return weightPorts;
    }

    public void setWeightPorts(Integer[] weightPorts) {
        this.weightPorts = weightPorts;
    }

    public Integer[] getPorts() {
        return ports;
    }

    public void setPorts(Integer[] ports) {
        this.ports = ports;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public void server(){

        /**---------------↓------------------线程池------------------↓----------------**/

        /**
         *  沙盘线程池
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap tcpServerBootstrap = new ServerBootstrap();

        /**
         *  称重线程池
         */
        EventLoopGroup weightBossGroup = new NioEventLoopGroup();
        EventLoopGroup weightWorkerGroup = new NioEventLoopGroup();
        ServerBootstrap weightTcpServerBootstrap = new ServerBootstrap();

        /**
         *
         * 二合一线程池
         */

        EventLoopGroup twoinoneBossGroup = new NioEventLoopGroup();
        EventLoopGroup twoinoneWorkerGroup = new NioEventLoopGroup();
        ServerBootstrap twoinoneTcpServerBootstrap = new ServerBootstrap();



        server(bossGroup,workerGroup,channelInit,tcpServerBootstrap,ports);

        server(weightBossGroup,weightWorkerGroup,weightChannelInit,weightTcpServerBootstrap,weightPorts);

        server(twoinoneBossGroup,twoinoneWorkerGroup,allChannelInit,twoinoneTcpServerBootstrap,allPorts);

        while(true){
            try {
                Thread.sleep(24*60*60*1000);
            } catch (InterruptedException e) {
            }finally {
                logger.info("tcp 关闭");
                close(bossGroup,workerGroup,weightBossGroup,weightWorkerGroup);
            }
        }






    }

    private void close( EventLoopGroup ... groups){

        for(EventLoopGroup group: groups){
            group.shutdownGracefully();
        }
    }

    private void server(EventLoopGroup boss,EventLoopGroup worker, ChannelInitializer  channelInitializer,ServerBootstrap serverBootstrap,Integer [] ports ) {
    try {

        serverBootstrap.group(boss,worker);
        //指定NIO的模式
        serverBootstrap.channel(NioServerSocketChannel.class)
                //设置TCP缓冲区
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(channelInitializer)
                //保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        //TCP监听多个端口
        if(ports!=null && ports.length>0) {
            ChannelFuture[] channelFutures = new ChannelFuture[ports.length];

            for (int i = 0; i < channelFutures.length; i++) {
                channelFutures[i] = serverBootstrap.bind(ports[i]).sync();
            }
            logger.info(" TCP启动成功,监听端口:" + Arrays.toString(ports));
        }

    }catch (InterruptedException ex){
        ex.printStackTrace();
        logger.error("TCP启动失败,监听端口:" + Arrays.toString(ports));
    }

    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        server();
    }
}
