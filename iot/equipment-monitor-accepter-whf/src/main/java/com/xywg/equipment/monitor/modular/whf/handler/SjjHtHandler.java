package com.xywg.equipment.monitor.modular.whf.handler;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.SjjHtDTO;
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
 * Date: Created in 14:19 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class SjjHtHandler extends SimpleChannelInboundHandler<SjjHtDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SjjHtHandler.class);

    @Autowired
    ZbusProducerHolder zbusProducerHolder  ;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    RedisUtil  redisUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SjjHtDTO msg) throws Exception {
        LOGGER.info("###############升降机心跳处理器收到数据########################");
        String sn = msg.getData().split(":")[1];
        zbusProducerHolder.sendLiftTestMessage(sn,msg.getData());
        if(NettyChannelManage.isOn(sn,YcDataHandler.type_lift,redisUtil,ctx,serverProperties.getName())){
            //上线
            String jsondata  = "{\"deviceNo\":\""+sn+"\",\"deviceTime\":"+msg.getNow().getTime()+"}";
            zbusProducerHolder.sendSjjHtMessage(jsondata);
        }
        NettyChannelManage.sendHt(ctx);
    }
}
