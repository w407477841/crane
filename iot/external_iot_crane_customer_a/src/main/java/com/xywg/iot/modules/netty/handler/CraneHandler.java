package com.xywg.iot.modules.netty.handler;

import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.RedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * @author hjy
 * @date 2018/11/20
 */
@Component
@ChannelHandler.Sharable
public class CraneHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(CraneHandler.class);
    private Logger craneLogger = LoggerFactory.getLogger("crane");

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BusinessProcessService businessProcessService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        //可读长度
        int readableLength = buf.readableBytes();
        byte[] msgData = new byte[readableLength];
        buf.readBytes(msgData);
        String originalData = DataUtil.bytesToHexString(msgData);
        craneLogger.info("Receive Original Data:" + originalData);
        businessProcessService.handleMessage(ctx, msgData);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("New Connection ... " + "SocketChannel Info: " + getRemoteAddress(ctx));
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("Channel Inactive---> Active Disconnection--->ip:" + getRemoteAddress(ctx));
        //移除连接
        businessProcessService.deviceDisconnect(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.close();
                logger.warn("Heartbeat Detection Timeout--->ip:" + getRemoteAddress(ctx));
            } /*else if (event.state() == IdleState.WRITER_IDLE) {
                logger.warn("Write Time Out From Server--->ip:" + getRemoteAddress(ctx));
            } else if (event.state() == IdleState.ALL_IDLE) {
                logger.warn("All Time Out From Server--->ip:" + getRemoteAddress(ctx));
            }*/
        }
    }


    /**
     * 获取远程客户端ip
     * @return
     */
    public String getRemoteAddress(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return inSocket.getAddress().getHostAddress();
    }

}
