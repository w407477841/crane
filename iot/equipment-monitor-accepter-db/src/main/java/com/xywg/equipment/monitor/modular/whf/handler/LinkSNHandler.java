package com.xywg.equipment.monitor.modular.whf.handler;

import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.decoder.DbDecoder;
import com.xywg.equipment.monitor.modular.whf.dto.LinkSNDTO;
import com.xywg.equipment.monitor.modular.whf.init.DbChannelInit;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    private final String type_meter = "meter";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ServerProperties serverProperties;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LinkSNDTO msg) throws Exception {
        LOGGER.info("##############设备号处理器数据###################");
        String data = msg.getData();

        LOGGER.info("##############原始数据："+data+"###################");

        int snAmount = data.length()/DbDecoder.LINK_SN_LENGTH;
        LOGGER.info("##############解析到 ["+snAmount+"] 个设备号###################");
        String sn [] = new String[snAmount];
        for(int i = 0;i<snAmount;i++){
            sn[i] = StrUtil.sub(data,i*DbDecoder.LINK_SN_LENGTH,(i+1)*DbDecoder.LINK_SN_LENGTH);
            if(NettyChannelManage.isOn(sn[i],type_meter,redisUtil,ctx,serverProperties.getName())) {

                LOGGER.info("##############设备号 : " + sn[i] + "###################");
                DbChannelInit.channels.put(sn[i], ctx.channel());
                //上线
                zbusProducerHolder.sendDbSxMessage(sn[i]);
            }
        }
        LOGGER.info("##############设备号处理器数据 完毕###################");



    }


}
