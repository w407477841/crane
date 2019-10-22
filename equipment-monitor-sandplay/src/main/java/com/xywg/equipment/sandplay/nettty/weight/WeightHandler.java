package com.xywg.equipment.sandplay.nettty.weight;

import com.xywg.equipment.sandplay.modular.sandplay.model.ProjectLoadometerDetail;
import com.xywg.equipment.sandplay.modular.sandplay.service.IProjectLoadometerDetailService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:49 2018/10/11
 * Modified By : wangyifei
 */
@Component
public class WeightHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightHandler.class);

    @Autowired
    private IProjectLoadometerDetailService ipldService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

           LOGGER.info("#############实际处理类##########");
          String str = msg;
        str = str.trim().replaceAll("\r|\n|[a-zA-Z]|\\+|=","");
        BigDecimal zeroStart =   new BigDecimal("11.79");
        BigDecimal zeroEnd =   new BigDecimal("11.82");

        BigDecimal curr=    new BigDecimal(str);
        if(curr.compareTo(zeroStart)==-1||curr.compareTo(zeroEnd)==1){
            LOGGER.info("##################压力传感器数值###############");
            LOGGER.info("##################"+str+"###############");
            String convert  = "19."+str.split("\\.")[1];
            Date date = new Date();
            ProjectLoadometerDetail pld = new ProjectLoadometerDetail();
            pld.setTimeBegin(date);
            pld.setTimeEnd(date);
            pld.setTimeEnd(date);
            pld.setWeightGross(convert);
            pld.setPid(2);
            pld.setIsDel(0);
            ipldService.insert(pld);
        }




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
        LOGGER.info("###############设备"+address+"##################");
        WeightChannelInit.channelMap.put(address,ctx.channel());

    }

    private  String getRemoteAddress(ChannelHandlerContext ctx){
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }

}
