package com.xywg.equipment.monitor.modular.whf.handler;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.TjHtDTO;
import com.xywg.equipment.monitor.modular.whf.init.WhfChannelInit;
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
 * Date: Created in 14:24 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class TjHtHandler extends SimpleChannelInboundHandler<TjHtDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TjHtHandler.class);

    @Autowired
    ZbusProducerHolder zbusProducerHolder  ;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    RedisUtil redisUtil;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TjHtDTO msg) throws Exception {
        LOGGER.info("###################塔基心跳处理器接收到数据################");
        String sn = msg.getData().split(":")[1];
        zbusProducerHolder.sendLiftTestMessage(sn,msg.getData());
        if(NettyChannelManage.isOn(sn,YcDataHandler.type_crane,redisUtil,ctx,serverProperties.getName())){
            //上线
            String jsondata  = "{\"deviceNo\":\""+sn+"\",\"deviceTime\":"+msg.getNow().getTime()+"}";
            zbusProducerHolder.sendTjHtMessage(jsondata);
        }
        NettyChannelManage.sendHt(ctx);
    }
}
