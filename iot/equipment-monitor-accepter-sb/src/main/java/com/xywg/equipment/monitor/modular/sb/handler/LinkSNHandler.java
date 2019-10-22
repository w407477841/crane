package com.xywg.equipment.monitor.modular.sb.handler;

import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.sb.decoder.SbDecoder;
import com.xywg.equipment.monitor.modular.sb.dto.LinkSNDTO;
import com.xywg.equipment.monitor.modular.sb.init.SbChannelInit;
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
 * Date: Created in 14:09 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class LinkSNHandler extends SimpleChannelInboundHandler<LinkSNDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkSNHandler.class);
    @Autowired
    ZbusProducerHolder  zbusProducerHolder;



    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ServerProperties serverProperties;

    private final String type_water = "water";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LinkSNDTO msg) throws Exception {
        LOGGER.info("##############设备号处理器数据###################");
        String data = msg.getData();

        LOGGER.info("##############原始数据："+data+"###################");

        int snAmount = data.length()/SbDecoder.LINK_SN_LENGTH;
        LOGGER.info("##############解析到 ["+snAmount+"] 个设备号###################");
        String sn [] = new String[snAmount];
        for(int i = 0;i<snAmount;i++){
            sn[i] = StrUtil.sub(data,i*SbDecoder.LINK_SN_LENGTH,(i+1)*SbDecoder.LINK_SN_LENGTH);
            if(NettyChannelManage.isOn(sn[i],type_water,redisUtil,ctx,serverProperties.getName())) {
                LOGGER.info("##############设备号 : "+sn[i]+"###################");
                SbChannelInit.channels.put(sn[i],ctx.channel());
                //上线
                zbusProducerHolder.sendDbSxMessage(sn[i]);
            }
        }
        LOGGER.info("##############设备号处理器数据 完毕###################");



    }


}
