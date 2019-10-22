package com.xywg.equipment.monitor.iot.netty.aop;

//import com.xywg.equipmentmonitor.core.netty.HeartService;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author hjy
 * @date 2018/7/30
 */
@Component
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
   // @Autowired
   // private HeartService heartService;
    private Logger logger = LoggerFactory.getLogger(UdpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String messageStr = msg.content().toString(CharsetUtil.UTF_8);
        logger.info("<<heartData:" + messageStr+">>");

    //    String resMessage = heartService.heartDataHandle(messageStr);
        String resMessage = "";
        if(StrUtil.isNotBlank(resMessage)){
            //当有需要下发指令时,给设备回复PostRequest   让设备走TCP去请求指令
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(resMessage, CharsetUtil.UTF_8), msg.sender()));
        }
    }
}
