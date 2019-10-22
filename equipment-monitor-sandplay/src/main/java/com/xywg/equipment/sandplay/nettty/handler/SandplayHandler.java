package com.xywg.equipment.sandplay.nettty.handler;

import com.xywg.equipment.sandplay.nettty.ChannelInit;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:00 2018/9/21
 * Modified By : wangyifei
 */
@Component
public class SandplayHandler  extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger loger  = LoggerFactory.getLogger(SandplayHandler.class);

    /**
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String address  = getRemoteAddress(ctx);
        loger.info("###############设备"+address+"##################");
        ChannelInit.channelMap.put(address,ctx.channel());

    }

    private  String getRemoteAddress(ChannelHandlerContext ctx){
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {


        int readableLength = msg.readableBytes();

        byte[] msgData = new byte[readableLength];
        msg.readBytes(msgData);
        //收到的原始数据串
        String restDataString = toHexString(msgData).toUpperCase();
        System.out.println(restDataString);
    }

    /**
     * @param bytes
     * @return
     * @description 将指定byte数组以16进制的形式打印到控制台
     */
    public static String toHexString(byte[] bytes) {
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt(b & 0x0f));
            sb.append(" ");
        }
        return sb.toString();
    }

}
