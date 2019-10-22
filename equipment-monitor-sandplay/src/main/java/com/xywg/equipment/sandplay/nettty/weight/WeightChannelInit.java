package com.xywg.equipment.sandplay.nettty.weight;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:25 2018/10/11
 * Modified By : wangyifei
 */
@Component
public class WeightChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    private  WeightHandler weightHandler;

    public static Map<String,Channel> channelMap = new ConcurrentHashMap<>();


    @Autowired
    private WeightDecoder  weightDecoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightChannelInit.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline  = ch.pipeline();
        pipeline.addLast(weightDecoder);
        pipeline.addLast(weightHandler);
    }
}
