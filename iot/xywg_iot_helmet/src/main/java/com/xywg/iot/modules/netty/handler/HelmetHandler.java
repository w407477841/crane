package com.xywg.iot.modules.netty.handler;

import com.xywg.iot.common.utils.DataAnalyUtil;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.core.annotion.SocketCommand;
import com.xywg.iot.modules.helmet.model.DataDomain;
import com.xywg.iot.modules.helmet.model.DeviceWorker;
import com.xywg.iot.modules.helmet.model.ProjectHelmet;
import io.netty.buffer.ByteBuf;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import static com.xywg.iot.common.global.GlobalStaticConstant.XYWG_IOT_HELMET;

/**
 * @author hjy
 * @date 2018/11/20
 */
@Component
@ChannelHandler.Sharable
public class HelmetHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(HelmetHandler.class);
    @Autowired
    private HelmetMessageDeal helmetMessageDeal;
    @Autowired
    private RedisUtil redisUtil;
  /*  @Autowired
    private ZbusConsumerHolder zbusConsumerHolder;*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        //可读长度
        int readableLength = buf.readableBytes();
        byte[] msgData = new byte[readableLength];
        buf.readBytes(msgData);
        this.dealMessage(ctx, msgData);
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
        NettyChannelManage.removeChannel(ctx.channel());
        DeviceWorker deviceWorker = NettyChannelManage.getDeviceWorkerByChannel(ctx.channel());
        if (StringUtils.isNotBlank(deviceWorker.getImei())) {
            //修改设备状态为离线
            //commonMethod.handleDeviceIsOnline(sn, DEVICE_STATE_OFF_LINE);
            helmetMessageDeal.terminalDown(deviceWorker);
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
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*写超时*/
                logger.warn("Write Time Out From Server--->ip:" + getRemoteAddress(ctx));
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
                logger.warn("All Time Out From Server--->ip:" + getRemoteAddress(ctx));
            }
        }
    }

    /**
     * 处理消息
     */
    private void dealMessage(ChannelHandlerContext ctx, byte[] data) {
        logger.info("Receive Original Data:" + DataUtil.toHexString(data));
        int command = DataAnalyUtil.getCommand(data);
        DataDomain dataDomain = DataAnalyUtil.getDataDomain(data);
        logger.info("Receive Analysis Data:" + dataDomain.toString());

        //如果包含imei 号 即登录报文，不包含的是业务报文
        if (dataDomain.getImei() != null) {
            reflectMethod(command, dataDomain, ctx);
        } else {
            DeviceWorker deviceWorker= NettyChannelManage.getDeviceWorkerByChannel(ctx.channel());
            if(deviceWorker == null ){
                logger.info("No Login Data:" + dataDomain.toString());
                ctx.close();
                return;
            }
            //查询缓存中这个设备是否存在
            ProjectHelmet projectHelmet = (ProjectHelmet) redisUtil.get(XYWG_IOT_HELMET +deviceWorker.getImei());

            if (projectHelmet == null) {
                helmetMessageDeal.terminalDown(deviceWorker);
                ctx.close();
                return;
            }

            dataDomain.setImei(deviceWorker.getImei());
            reflectMethod(command, dataDomain, ctx);
        }

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
     * 反射调用
     */
    private void reflectMethod(int command, Object... objects) {
        Class<HelmetMessageDeal> clazz = HelmetMessageDeal.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            boolean methodHasAnnotation = method.isAnnotationPresent(SocketCommand.class);
            if (methodHasAnnotation) {
                SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
                int desc = socketCommand.command();
                if (desc == command) {
                    try {
                        method.invoke(helmetMessageDeal, objects);
                        break;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}
