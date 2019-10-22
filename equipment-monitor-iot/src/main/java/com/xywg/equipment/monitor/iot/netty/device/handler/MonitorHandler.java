package com.xywg.equipment.monitor.iot.netty.device.handler;

import com.xywg.equipment.monitor.iot.core.dto.RemoteDTO;
import com.xywg.equipment.monitor.iot.core.util.Crc16Util;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayService;
import com.xywg.equipment.monitor.iot.netty.device.crane.CraneHandlerService;
import com.xywg.equipment.monitor.iot.netty.device.dust.DustHandlerService;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import com.xywg.equipment.monitor.iot.netty.device.spray.SprayHandleService;
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
import java.util.UUID;

import static com.xywg.equipment.global.GlobalStaticConstant.DEVICE_STATE_OFF_LINE;
import static com.xywg.equipment.global.GlobalStaticConstant.XYWG_PROTOCOL_HEAD;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.*;

/**
 * @author hjy
 * @date 2018/9/18
 * 数据处理服务
 */
@Component
@ChannelHandler.Sharable
public class MonitorHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Logger logger = LoggerFactory.getLogger(MonitorHandler.class.getName());
    @Autowired
    private DustHandlerService dustHandlerService;
    @Autowired
    private CraneHandlerService attendanceHandlerService;
    @Autowired
    private SprayHandleService sprayHandleService;
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
//        if (!dataValidity(ctx, restDataString)) {
//            ctx.close();
//            return;
//        }
        //设备序列号
        String sn = decode(restDataString.replaceAll(" ", "").substring(30, 62));
        // 发送调试数据
        simpMessageSendingOperations.convertAndSend("/topic/xywg/monitor/"+sn, restDataString);

        switchAnalysisProtocol(ctx, restDataString, sn);
    }


    /**
     * 根据设备类型选择解析方式
     */
    private void switchAnalysisProtocol(ChannelHandlerContext ctx, String restDataString, String sn) {
        //收到的原始字符串
        logger.info("Receive:<<Device:" + sn + " Message:" + restDataString + ">>");
        //获取设备类型
        String deviceType = sn.substring(8, 10);
        switch (deviceType) {
            //二维码门禁控制板
            case "01":
                // System.out.println("01二维码门禁控制板");
                break;

            //扬尘
            case "02":
                dustHandlerService.dataProtocol(ctx, restDataString, sn);
                break;

            //塔机
            case "03":
                attendanceHandlerService.dataProtocol(ctx, restDataString, sn);
                break;
            //智能安全帽
            case "04":
                break;
            //二维码扫描仪
            case "05":
                break;
            //地磅
            case "06":
                break;
            //沙盘传感器采集控制板
            case "07":
                break;
            //喷淋设备
            case "0F":
                sprayHandleService.dataProtocol(ctx, restDataString, sn);
                break;
            default:
                break;
        }
    }


    /**
     * channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
     *
     * @param ctx
     * @throws Exception
     * 链接断开后执行
     */

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel Inactive---> Active Disconnection--->ip:" + getRemoteAddress(ctx));
        NettyChannelManage.removeChannel(ctx.channel());
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());
        if (deviceConnectInfo != null) {
            String sn = deviceConnectInfo.getSn();
            if (StringUtils.isNotBlank(sn)) {
                //修改设备状态为离线
                commonMethod.handleDeviceIsOnline(sn, DEVICE_STATE_OFF_LINE);
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
                ctx.close();
                logger.info("Heartbeat Detection Timeout--->ip:" + getRemoteAddress(ctx));
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
        logger.info("NettyException--->Active Disconnection--->ip:"
                + getRemoteAddress(ctx) + " ExceptionMessage:" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 新客户端接入
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client Connection--->ip:" + getRemoteAddress(ctx));
        System.out.println("----------当前连接数:" + (NettyChannelManage.CHANNEL_MAP.size() + 1) + "--------------");
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
