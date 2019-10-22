package com.xywg.equipment.monitor.modular.whf.handler;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.YcHtDTO;
import com.xywg.equipment.monitor.modular.whf.init.WhfChannelInit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:44 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class YcHtHandler extends SimpleChannelInboundHandler<YcHtDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(YcHtHandler.class);
    /** redis 扬尘信息 key*/
    public final static String YC_INFO_SUFF="device_platform:str:deviceinfo:monitor";


    @Autowired
    ZbusProducerHolder zbusProducerHolder  ;
    @Autowired
    ServerProperties  serverProperties;
    @Autowired
    RedisUtil  redisUtil ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, YcHtDTO msg) throws Exception {
        LOGGER.info("############扬尘心跳处理器接收到数据############");
        String sn =  msg.getData().split(":")[1];
        zbusProducerHolder.sendMonitorTestMessage(sn,msg.getData());
        if(NettyChannelManage.isOn(sn,YcDataHandler.type_monitor,redisUtil,ctx,serverProperties.getName())){
            //上线
            String jsondata  = "{\"deviceNo\":\""+sn+"\",\"deviceTime\":"+msg.getNow().getTime()+"}";
            zbusProducerHolder.sendYcHtMessage(jsondata);
        }
        NettyChannelManage.sendHt(ctx);
    }

}
