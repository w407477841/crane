package com.xywg.equipment.monitor.iot.netty.device.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author hjy
 * @date 2018/9/18
 * 监控队列中的 channel 初始化
 */
@Component
public class MonitorChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    private MonitorHandler monitorHandler;

    /**
     * TCP协议处理
     */
    @Override
    protected void initChannel(SocketChannel ch) {
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
}


