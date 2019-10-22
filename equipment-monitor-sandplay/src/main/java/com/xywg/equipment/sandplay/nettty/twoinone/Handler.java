package com.xywg.equipment.sandplay.nettty.twoinone;

import com.xywg.equipment.sandplay.modular.sandplay.model.ProjectLoadometerDetail;
import com.xywg.equipment.sandplay.modular.sandplay.service.IProjectLoadometerDetailService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.regex.Pattern;

import static com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit.channelMap;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:00 2018/10/15
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class Handler  extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    /**
     * 解析 形如：
     * F=0.89
     * OK
     *
     * */
    private static final String PATTERN_WEIGHT = "02\\+F=((0)|([1-9]\\d?)).\\d{2}(\\n|\\r|\\n\\r|\\r\\n)OK";

    private static final String PATTERN_CONTROL  = "01((2201210000000044)|(220113000000[fF]{2}3[56]))";


    private static final String PATERN_HEAD  = "03FFFFFFFF";

    private static final String PATERN_HEART  = "04FFFFFFFF";


    @Autowired
    private IProjectLoadometerDetailService ipldService;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
           //可读长度
        try{
        int readableLength = msg.readableBytes();
            LOGGER.info("###############数据长度#"+readableLength+"#############");
        byte[] msgData = new byte[readableLength];
        msg.readBytes(msgData);
        String data  = new String (msgData);
        LOGGER.info("###############原始数据##############");
        LOGGER.info("###############"+data+"##############");
        if(Pattern.matches(PATERN_HEAD,data)){
            LOGGER.info("###############沙盘连接##############");
            channelMap.put(AllChannelInit.KEY,ctx.channel());
        }else if(Pattern.matches(PATERN_HEART,data)){
            LOGGER.info("###############沙盘心跳##############");
            channelMap.put(AllChannelInit.KEY,ctx.channel());
            ByteBuf byteBuf = Unpooled.copiedBuffer(data.getBytes());
            ctx.channel().writeAndFlush(byteBuf);


        }
        else if(Pattern.matches(PATTERN_CONTROL,data)){
            // 控制返回
            LOGGER.info("###############接受控制返回##############");
            LOGGER.info("###############"+data+"##############");

        }else if(Pattern.matches(PATTERN_WEIGHT,data)){
            //称重返回
            LOGGER.info("###############薄膜返回##############");
            LOGGER.info("###############"+data+"##############");

            LOGGER.info("#############解析称重数据##########");
            String str = data;
            str = str.trim().replaceAll("\r|\n|[a-zA-Z]|\\+|=","");

            str = str.substring(2);
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



        }else{
            LOGGER.error("###非法数据###");
            ctx.close();
        }
        }catch (Exception ex){
            LOGGER.error("########################");
            LOGGER.error(ex.getMessage());
            LOGGER.error("########################");
        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("#################设备连接#############");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if(ctx.channel() == AllChannelInit.channelMap.get(AllChannelInit.KEY)){
            AllChannelInit.channelMap.remove(AllChannelInit.KEY);
        }
        LOGGER.info("#################设备断开############");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("###35秒未收到客户端消息，主动关闭连接###");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*写超时*/
                // System.out.println("===服务端===(WRITER_IDLE 写超时)");
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
                // System.out.println("===服务端===(ALL_IDLE 总超时)");
            }
        }

    }
}
