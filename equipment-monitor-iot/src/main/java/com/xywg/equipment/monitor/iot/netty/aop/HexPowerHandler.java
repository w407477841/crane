package com.xywg.equipment.monitor.iot.netty.aop;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.iot.modular.ammeter.handler.AmmeterHandler;
import com.xywg.equipment.monitor.iot.modular.base.handler.HexBaseDevice;
import com.xywg.equipment.monitor.iot.modular.base.util.ApplicationContextProvider;
import com.xywg.equipment.monitor.iot.modular.watermeter.handler.WatermeterHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:42 2018/9/10
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
@SuppressWarnings("all")
public class HexPowerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    //所有电表设备
    public static Map<String,Channel> ameterChannel = new ConcurrentHashMap(0);
    //电表推送
    public static Map<String,String> sendAmeterMessage = new ConcurrentHashMap(0);

    //所有水表设备
    public static Map<String,Channel> waterChannel = new ConcurrentHashMap(0);
    //水表推送
    public static Map<String,String> sendWaterMessage = new ConcurrentHashMap(0);

    private static final Logger  log = LoggerFactory.getLogger(HexPowerHandler.class);

    /**
     *
     * 接收到数据后触发
     * @param ctx  连接上下文
     * @param msg  消息
     * */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        // 得到 下位机发送的16进制数据包
        String hexStr = ByteBufUtil.hexDump(msg);
        System.out.println(hexStr);
        HexBaseDevice device = ApplicationContextProvider.getBean(AmmeterHandler.class);

      //  HexBaseDevice deviceWater = ApplicationContextProvider.getBean(WatermeterHandler.class);

        device.doBusiness(hexStr.toUpperCase(),ctx);

        //deviceWater.doBusiness(hexStr.toUpperCase(),ctx);

    }

    /**
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String address  = getRemoteAddress(ctx);
        log.info("###############设备"+getRemoteAddress(ctx)+"##################");

    }

    public static String getRemoteAddress(ChannelHandlerContext ctx){
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }


    public static void main(String[] args){
        String hexStr  = "FA071302FA02284E68096A48FAFF";
        Pattern pattern = Pattern.compile("FA071302FA02284E(\\w{8})FAFF");
        Matcher matcher  =pattern.matcher(hexStr);
        if(matcher.find()){
            int count =  matcher.groupCount();

                String addr = matcher.group(1);

                System.out.println(addr);


        }else{

        }

    }

}
