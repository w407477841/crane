package com.xywg.equipment.sandplay.nettty;

import com.xywg.equipment.sandplay.nettty.handler.SandplayDecoder;
import com.xywg.equipment.sandplay.nettty.handler.SandplayHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:57 2018/9/21
 * Modified By : wangyifei
 */
@Component
public class ChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    private SandplayHandler sandplayHandler;
    @Autowired
    private SandplayDecoder   sandplayDecoder;


    /** 所有接入的通道 */
    public static Map<String,Channel> channelMap = new ConcurrentHashMap(0);
    /** 发送的最后一条消息*/
    public static Map<String,String> messageMap = new ConcurrentHashMap(0);


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline  pipeline  = ch.pipeline();
        pipeline.addLast(new FixedLengthFrameDecoder(8));
      //  pipeline.addLast(new StringDecoder());
        pipeline.addLast(sandplayHandler);
    }




}
