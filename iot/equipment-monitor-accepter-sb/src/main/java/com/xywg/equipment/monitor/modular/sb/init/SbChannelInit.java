package com.xywg.equipment.monitor.modular.sb.init;

import com.xywg.equipment.monitor.modular.sb.decoder.SbDecoder;
import com.xywg.equipment.monitor.modular.sb.handler.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
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
 * Date: Created in 16:37 2018/10/15
 * Modified By : wangyifei
 */
@Component
public class SbChannelInit extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SbChannelInit.class);
    /**存储所有通道  ， 用于向设备回消息
     * key 为 客户端 IP+PORT 的hash
     * */
    public static Map<String ,Channel>   channels = new ConcurrentHashMap();

    public static Map<String,String> sendMessage = new ConcurrentHashMap<>();

    /**缓存 分钟数
     * 暂存 1天
     * */
    public static final  Long CACHE_MINS = 60*24L ;


    @Autowired
    private SbHandler sbDataHandler;
    @Autowired
    private LinkSNHandler  linkSNHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline()
                .addLast(new SbDecoder())
                //心跳检测  60秒未收到数据，服务器主动断开连接
                .addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                .addLast(sbDataHandler,linkSNHandler);



    }
}
