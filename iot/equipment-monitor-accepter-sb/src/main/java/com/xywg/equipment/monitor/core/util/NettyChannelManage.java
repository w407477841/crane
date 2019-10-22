package com.xywg.equipment.monitor.core.util;


import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.core.netty.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.List;
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
            List<String> deviceCode = session.getServerNo();
            for (int i = 0;i < deviceCode.size();i++) {
                if(StrUtil.isNotBlank(deviceCode.get(i))){
                    CHANNEL_MAP.remove(deviceCode);
                }
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

    /**
     *
     * @param sn 设备号
     * @param type 设备类型 monitor , lift , crane , water , ammeter
     * @param redisUtil 缓存工具类
     * @param ctx channel上下文
     * @param serverName 服务名
     * @return
     */
    public static boolean isOn(String sn,String type,RedisUtil redisUtil,ChannelHandlerContext ctx,String serverName){
        String localKey = type+":"+sn;
        if(NettyChannelManage.getChannel(localKey)==null){
            // 第一次
            //本地添加 设备与通道的关系
            NettyChannelManage.CHANNEL_MAP.put(localKey,ctx.channel());
            Session session =Session.factory(ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).get(),sn,type);
            ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(session);

            //维护缓存中设备与服务器的关系
            String redisDeviceKey =  "device_platform:devices:"+localKey;
            if(redisUtil.exists(redisDeviceKey)){
                //此设备之前异常断开 , 删除原channel缓存
                String server = (String) redisUtil.get(redisDeviceKey);
                String redisKey = "device_platform:channel:"+server;
                redisUtil.remove(redisKey);
            }

            String server = serverName+"#"+ctx.channel().remoteAddress().toString();
            //设备与服务器的关系
            redisUtil.set(redisDeviceKey,server);
            //服务器与设备的关系
            redisUtil.set("device_platform:channel:"+server,localKey);
            return true;
        }
        return false;
    }


}
