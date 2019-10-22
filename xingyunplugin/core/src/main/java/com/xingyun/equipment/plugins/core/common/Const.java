package com.xingyun.equipment.plugins.core.common;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.xingyun.equipment.plugins.core.common.enums.LogEnum;
import com.xingyun.equipment.plugins.core.libraryserver.XingyunCall;
import com.xingyun.equipment.plugins.core.pojo.XingyunSession;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:34 2019/7/8
 * Modified By : wangyifei
 */
public class Const {
    /** session 缓存KEY */
    public static  final AttributeKey<XingyunSession> XINGYUN_SESSION_ATTRIBUTE_KEY =AttributeKey.valueOf("xingyun.session");
    /** 通道Map */
    public static final Map<String ,Channel> CHANNEL_MAP = new ConcurrentHashMap<>(2048);

    /** 连接计数器 */
    public static LongAdder LINK_ACCOUNT = new LongAdder();

    public static final String CMD_NAME = "CMD";

    public static final String SERIAL_NAME = "Serial";

    public static final String PROJECT_NAME = "PrjID";


    /**
     * 打包并发送
     * @param objectMap
     * @param ctx
     */
    public static void combinationAndSend(Map<String,Object> objectMap,ChannelHandlerContext ctx){
        combinationAndSend(objectMap,ctx.channel());
    }
    /**
     * 打包并发送
     * @param objectMap
     * @param channel
     */
    public static void combinationAndSend(Map<String,Object> objectMap,Channel channel){
        byte [] packet = new byte[2048];
        int length =  XingyunCall.EnviromentPluginService.INSTANCE.combination_packet(packet,JSONUtil.toJsonStr(objectMap).getBytes());
        if(length>0){
            byte [] realpacket = new byte[length];
            ArrayUtil.copy(packet,realpacket,length);
            channel.writeAndFlush(Unpooled.copiedBuffer(realpacket));
        }else{
            throw  new RuntimeException(LogEnum.COMMON.format("封包错误(",length+"",")"));
        }
    }
}
