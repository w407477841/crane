package com.xywg.attendance.modular.netty.handler;

import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import com.xywg.attendance.common.utils.DateUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author hjy
 * @date 2019/2/21
 */
@Component
@ChannelHandler.Sharable
public class AttendanceInboundHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private MethodService methodService;
    @Autowired
    private CommonMethod commonMethod;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {

        if (!(message instanceof FullHttpRequest)) {
            return;
        }
        FullHttpRequest msg = (FullHttpRequest) message;
        String addressIp = commonMethod.getRemoteAddress(ctx);
        String method = msg.method().toString();
        String url = msg.uri();

        ByteBuf byteBuf = msg.content();
        int len = byteBuf.readableBytes();
        byte[] msgData = new byte[len];
        byteBuf.readBytes(msgData);

        ((FullHttpRequest) message).release();
        TransmissionMessageTemplate template = new TransmissionMessageTemplate(addressIp, method, url, null, DateUtils.getDateTime());

        methodService.runMethod(ctx, template, msgData);

    }


    /**
     * 建立连接时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


}
