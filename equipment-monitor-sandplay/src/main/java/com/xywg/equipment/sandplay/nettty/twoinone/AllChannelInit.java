package com.xywg.equipment.sandplay.nettty.twoinone;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
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
 * Date: Created in 9:59 2018/10/15
 * Modified By : wangyifei
 */
@Component
public class AllChannelInit  extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllChannelInit.class);
    @Autowired
    private Handler    handler;
    /**存储channel*/
    public static Map<String,Channel> channelMap = new ConcurrentHashMap<>();
    /**沙盘客户端*/
    public final static String KEY = "shapan";

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

                ChannelPipeline  pi  =    ch.pipeline();
                pi.addLast(new IdleStateHandler(35, 0, 0, TimeUnit.SECONDS));
                pi.addLast(handler);



    }
}
