package com.xywg.iot.netty.handler;

import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import static com.xywg.iot.common.global.GlobalStaticConstant.XYWG_PROTOCOL_HEAD;
import static com.xywg.iot.netty.handler.CommonStaticMethod.toBytes;
import static com.xywg.iot.netty.handler.CommonStaticMethod.toHexString;


/**
 * @author hjy
 * @date 2018/11/20
 */
@Component
@ChannelHandler.Sharable
public class PositionHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(PositionHandler.class);
    private Logger craneLogger = LoggerFactory.getLogger("crane");
    public static AtomicInteger count = new AtomicInteger();
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    List<BaseAction> actions;

/*    @Autowired
    private BusinessProcessService businessProcessService;*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        ByteBuf msg = (ByteBuf) message;
        //可读长度
        int readableLength = msg.readableBytes();
        byte[] msgData = new byte[readableLength];
        msg.readBytes(msgData);
        //收到的原始数据串
        String restDataString = toHexString(msgData).toUpperCase();
        //初步判断数据的有效性
        if (!dataValidity(ctx, restDataString)) {
            ctx.close();
            return;
        }
        //分解数据
        CompleteDataPojo dataDomain =  CommonStaticMethod.resolutionProtocol(restDataString);
        //执行对应handle
        String commandCode= dataDomain.getCommand();
        BaseAction.actions.forEach(item->{
            if(item.supports(commandCode)){
                item.handle(ctx,dataDomain);
            }
        });
        msg.release();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("#####################################################");
        logger.warn("#####################################################");
        logger.warn("#####################上线了##########################");
        logger.warn("####################连接数{}#########################",count.incrementAndGet());
        logger.warn("#####################################################");
        logger.warn("#####################################################");
        logger.warn("#####################################################");
        logger.info("New Connection ... " + "SocketChannel Info: " + getRemoteAddress(ctx));
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("#####################################################");
        logger.warn("#####################################################");
        logger.warn("#####################掉线了##########################");
        logger.warn("####################连接数{}#########################",count.decrementAndGet());
        logger.warn("#####################################################");
        logger.warn("#####################################################");
        logger.warn("#####################################################");
        logger.warn("Channel Inactive---> Active Disconnection--->ip:" + getRemoteAddress(ctx));
        NettyChannelManage.removeChannel(ctx.channel());
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());
        if (deviceConnectInfo != null) {
            String sn = deviceConnectInfo.getSn();
            if (StringUtils.isNotBlank(sn)) {
                //修改设备状态为离线
              //  commonMethod.handleDeviceIsOnline(sn, DEVICE_STATE_OFF_LINE);
            }
        }
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



    /**
     * 数据合法性初步校验
     *
     * @param restDataString 原始数据
     */
    private boolean dataValidity(ChannelHandlerContext ctx, String restDataString) {
        //将收到的16进制数据 按字节转化为数组
        String[] messageArray = restDataString.split(" ");
        //固定头部12个字节 序列号长度表示位2个字节 序列号编号key  1字节   序列号16字节
        int minLength = 12 + 2 + 1 + 16;
        if (messageArray.length < minLength) {
            logger.error("Illegal Data(Data Length Error):<<" + restDataString + ">>");
            //收到消息时回复给客户端  04数据包长度错误
            responseErrorMessageHandle(ctx, restDataString, "00", "04");
            return false;
        }
        //校验数据开头位置
        if (!XYWG_PROTOCOL_HEAD.equalsIgnoreCase(messageArray[0] + messageArray[1] + messageArray[2])) {
            logger.error("Illegal Data(Head String Error):<<" + restDataString + ">>");
            //收到消息时回复给客户端   02数据包FLAG错误
            responseErrorMessageHandle(ctx, restDataString, "00", "02");
            return false;
        }
        //crc 校验   08 CRC错误
        if (!crc16Validity(restDataString)) {
            responseErrorMessageHandle(ctx, restDataString, "00", "08");
            return false;
        }
        return true;
    }



    /**
     * 错误数据回复的处理
     *
     * @param ctx
     * @param message
     * @param messageType 0：应答包；1：请求包
     */
    public static void responseErrorMessageHandle(ChannelHandlerContext ctx, String message, String messageType, String errorCode) {
        String messageNew = message.replaceAll(" ", "");
        StringBuffer sb = new StringBuffer(messageNew);
        //改变协议头中的AQ    0：应答包；1：请求包
        sb.replace(12, 14, messageType);
        //错误类型位置
        sb.replace(18, 20, errorCode);
        logger.info("回复:{}",sb.toString());
        responseMessage(ctx, sb.toString());
    }


    /**
     * 回复消息给客户端
     *
     * @param ctx     上下文
     * @param message responseMessage
     */
    public static void responseMessage(ChannelHandlerContext ctx, String message) {
        ctx.writeAndFlush(Unpooled.copiedBuffer(toBytes(message)));
        logger.info("回复:{}",message);
    }

    /**
     * 回复消息给客户端
     * @param ctx
     * @param message
     */
    public static void responseMessage(Channel ctx, String message) {
        ctx.writeAndFlush(Unpooled.copiedBuffer(toBytes(message)));
        logger.info("回复:{}",message);
    }

    /**
     * crc 校验
     *
     * @param restDataString
     * @return
     */
    private boolean crc16Validity(String restDataString) {
        //数据CRC16校验(前面算空格在内的个36个字符为报文头)
        byte[] dd = Crc16Util.getData(restDataString.substring(36, restDataString.length()).split(" "));
        //字节数组转16进制字符串
        String crcString = Crc16Util.byteTo16String(dd).toUpperCase();
        //计算出来的校验位(去除多余的原始数据位,只保留CRC位)
        String crcCode = crcString.substring(crcString.length() - 6).replaceAll(" ", "");

        //原始校验字符串
        String crcCheckCode = restDataString.replaceAll(" ", "");
        //将低位在前高位在后的位置调换
        String checkCode = crcCheckCode.substring(22, 24) + crcCheckCode.substring(20, 22);
        if (!crcCode.equalsIgnoreCase(checkCode)) {
            logger.info("Illegal Data(CRC Check Error):<<" + restDataString + ">>");
            return false;
        }
        return true;
    }
}
