package com.xywg.equipment.monitor.iot.netty.device.handler;

import com.xywg.equipment.monitor.iot.core.util.Crc16Util;
import com.xywg.equipment.monitor.iot.netty.device.crane.CraneHandlerService;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

import static com.xywg.equipment.global.GlobalStaticConstant.XYWG_PROTOCOL_HEAD;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.*;

/**
 * @author hjy
 * @date 2018/9/18
 * 数据处理服务
 */
@Component
@ChannelHandler.Sharable
@SuppressWarnings("all")
public class MonitorHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Logger logger = LoggerFactory.getLogger(MonitorHandler.class.getName());
    @Autowired
    private CraneHandlerService attendanceHandlerService;
    @Autowired
    private CommonMethod commonMethod;

    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;

//收到数据后执行
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
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
        //设备序列号
        String sn = decode(restDataString.replaceAll(" ", "").substring(30, 62));
        attendanceHandlerService.dataProtocol(ctx, restDataString, sn);
    }





    /**
     * channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
     * 前面的业务逻辑里做了未登录踢掉的处理，这边对离线的设备进行数据库修改
     * 当设备异常离线(未经过四次挥手)，且服务器重启后，设备会显示在线，但实际为离线
     * @param ctx
     * @throws Exception
     * 链接断开后执行
     */

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接断开了:" + getRemoteAddress(ctx));
        NettyChannelManage.removeChannel(ctx.channel());
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());
        if (deviceConnectInfo != null) {
            String sn = deviceConnectInfo.getSn();
            logger.info("塔吊--设备{} 断开连接",sn);
            if (StringUtils.isNotBlank(sn)) {
                //修改设备状态为离线
                attendanceHandlerService.offline(ctx.channel(),sn);
            }
        }
        super.channelInactive(ctx);
    }

    /**
     * 事件触发后执行
     * 配合 IdleStateHandler使用
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.info("塔吊-- 心跳超时 {} ", getRemoteAddress(ctx));
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("塔吊--异常 {} 异常信息 {} ", getRemoteAddress(ctx),cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 新客户端接入
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端连接--->ip:{} , 当前连接数 {}" , getRemoteAddress(ctx),(NettyChannelManage.CHANNEL_MAP.size() + 1) );
        super.channelActive(ctx);
    }



    /**
     * 获取远程客户端ip地址
     *
     * @return
     */
    public String getRemoteAddress(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return inSocket.getAddress().getHostAddress();
    }


    /**
     * 回复消息给客户端
     *
     * @param ctx     上下文
     * @param message responseMessage
     */
    public void responseMessage(ChannelHandlerContext ctx, String message) {
        ctx.writeAndFlush(Unpooled.copiedBuffer(toBytes(message)));
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
            logger.error("塔吊--异常--数据长度,<<{}>>", restDataString );
            //收到消息时回复给客户端  04数据包长度错误
            responseErrorMessageHandle(ctx, restDataString, "00", "04");
            return false;
        }
        //校验数据开头位置
        if (!XYWG_PROTOCOL_HEAD.equalsIgnoreCase(messageArray[0] + messageArray[1] + messageArray[2])) {
            logger.error("塔吊--异常--头部错误:<<" + restDataString + ">>");
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
            logger.info("塔吊--异常--CRC校验错误:<<{}>>", restDataString );
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
    public void responseErrorMessageHandle(ChannelHandlerContext ctx, String message, String messageType, String errorCode) {
        String messageNew = message.replaceAll(" ", "");
        StringBuffer sb = new StringBuffer(messageNew);
        //改变协议头中的AQ    0：应答包；1：请求包
        sb.replace(12, 14, messageType);
        //错误类型位置
        sb.replace(18, 20, errorCode);
        responseMessage(ctx, sb.toString());
    }


}
