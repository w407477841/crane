package com.xywg.equipment.monitor.core.util;


import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.core.netty.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hjy
 * 用于统一管理所有的链接  通道与设备编号互相持有
 */
@Component
public class NettyChannelManage {

    /**
     * 管理所有的连接
     */
    public static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    public static final AttributeKey<Session> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");

    /**
     * 移除连接
     */
    public static void removeChannel(Channel channel) {
        Session session =  getDeviceCodeByChannel(channel);
        if(session != null){
            String deviceCode = session.getSn();
            if(StrUtil.isNotBlank(deviceCode)){
                CHANNEL_MAP.remove(deviceCode);
            }
        }
    }

    /**
     * 根据信道返回设备唯一标识
     *
     * @param channel
     * @return
     */
    public static Session getDeviceCodeByChannel(Channel channel) {
        Session session  = channel.attr(NETTY_CHANNEL_KEY).get();
        return session;
    }

    /**
     * 根据设备编号获取通道
     * @param deviceCode
     * @return
     */
    public static Channel getChannel(String deviceCode) {
        return CHANNEL_MAP.get(deviceCode);
    }


    public static void sendHt(ChannelHandlerContext ctx){
        ByteBuf msg = Unpooled.copiedBuffer("0".getBytes());
        ctx.writeAndFlush(msg);
    }


}
