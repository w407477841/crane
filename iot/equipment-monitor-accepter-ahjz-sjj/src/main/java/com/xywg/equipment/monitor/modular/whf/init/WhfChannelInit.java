package com.xywg.equipment.monitor.modular.whf.init;

import cn.hutool.core.util.HexUtil;
import com.xywg.equipment.monitor.modular.whf.handler.*;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:37 2018/10/15
 * Modified By : wangyifei
 */
@Component
public class WhfChannelInit extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhfChannelInit.class);
    /**存储所有通道  ， 用于向设备回消息
     * key 为 客户端 IP+PORT 的hash
     * */
    public static Map<String ,Channel>   channels = new ConcurrentHashMap();
    /**缓存 分钟数
     * 暂存 1天
     * */
    public static final  Long CACHE_MINS = 60*24L ;


    @Autowired
    private NettyHandler nettyHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline()
            //    .addLast(new WhfDecoder())
               .addLast(new DelimiterBasedFrameDecoder(1024,false,Unpooled.copiedBuffer(HexUtil.decodeHex("CC33C33C"))))
                .addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                .addLast(nettyHandler);
    }
}
