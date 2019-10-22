package com.xywg.attendance.modular.netty.handler;

import com.xywg.attendance.common.model.KeyModel;
import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import com.xywg.attendance.common.utils.DataUtil;
import com.xywg.attendance.common.utils.RedisUtil;
import com.xywg.attendance.common.utils.UrlUtil;
import com.xywg.attendance.core.mq.producer.RabbitMqService;
import com.zkteco.ZKCryptoClientService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

import static com.xywg.attendance.common.global.GlobalStaticConstant.DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY;

/**
 * @author hjy
 * @date 2019/2/21
 * 公共方法  提供给其他类使用
 */
@Service
public class CommonMethod {
    @Autowired
    private RabbitMqService rabbitMqService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MethodService methodService;

    private Logger logger = LoggerFactory.getLogger("");

    /**
     * 给设备正常回复信息
     *
     * @param ctx     返回
     * @param context 消息
     */
    public void sendMessage(ChannelHandlerContext ctx, String context) {
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(context.getBytes("GB2312"))
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.headers().set("Date", new Date());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 正常回复信息
     *
     * @param ctx     返回
     * @param context 消息
     */
    public void sendMessageUTF8(ChannelHandlerContext ctx, String context) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(context.getBytes(CharsetUtil.UTF_8))
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set("Date", new Date());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * 加密信息的回复
     *
     * @param ctx     返回
     * @param message 消息
     */
    public void sendMessageEncrypt(ChannelHandlerContext ctx, String message, String sn) {
        byte[] msgData = methodService.encryptData(message, sn);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(msgData)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set("Date", new Date());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * 给设备回复错误信息
     *
     * @param ctx     返回
     * @param context 消息
     */
    public void sendErrorMessage(ChannelHandlerContext ctx, String context) {
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.BAD_REQUEST,
                    Unpooled.copiedBuffer(context.getBytes("GB2312"))
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.headers().set("Date", new Date());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取body参数
     *
     * @param msgData
     * @return
     */
    public String getBodyGB2312(byte[] msgData) {
        String res = "";
        try {
            res = new String(msgData, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 获取body参数
     *
     * @param msgData
     * @return
     */
    public String getBodyGB2312Photo(byte[] msgData) {
        Integer index0 = DataUtil.index(msgData, 0x00);
        byte[] bodyMessage = DataUtil.subByteArray(msgData, 0, index0);
        byte[] bodyPhto = DataUtil.subByteArray(msgData, index0 + 1, msgData.length - index0 - 1);
        String resStr = "";
        try {
            String res1 = new String(bodyMessage, "GB2312");
            String phoBase64 = new String(bodyPhto, "GB2312");
            resStr = res1 + "\npicture=" + phoBase64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resStr;
    }

    /**
     * 将考勤记录中的二进制流 转为 base64
     *
     * @param msgData
     * @return
     */
    public String getBodyIncludePhoto(byte[] msgData) {
        try {
            Integer index = DataUtil.index(msgData, 0x00);
            //body 可用字符
            byte[] bodyMessage = DataUtil.subByteArray(msgData, 0, index);
            String bodyMessageStr = new String(bodyMessage, "GB2312");
            Map<String, String> map = UrlUtil.handleSeparate(bodyMessageStr, "\n", "=");
            Integer size = Integer.parseInt(map.get("size"));
            //上传考勤图片的二进制流
            byte[] photo = DataUtil.subByteArray(msgData, msgData.length - size, size);
            //将二进制流转为 base 64
            String base64Str = Base64.encodeBase64String(photo);
            return bodyMessageStr + "\npicture=" + base64Str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取远程客户端ip
     *
     * @return
     */
    public String getRemoteAddress(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return inSocket.getAddress().getHostAddress();
    }


    /**
     * 将需要走队列的数据保存一份到NoSQL 发送到队列中
     */
    public void backupsAndToMq(String topic, TransmissionMessageTemplate template) {
        //保存到NoSQL
        //mongoTemplate.save(template);
        //发送到队列
        rabbitMqService.amqpSendMessage(topic, template);
    }

    /**
     * 设备发来公钥时
     *
     * @param serverPrivateKey 服务器之间的私钥
     * @param sn               设备编号
     * @param devicePublicKey  设备发来的公钥
     */
    public void handleDevicePublicKey(String serverPrivateKey, String sn, String devicePublicKey) {
        //初始化内存地址
        long hClient = ZKCryptoClientService.init();
        //根据设备公钥和指定的私钥(serverPrivateKey) 生成服务器端公钥
        int ret = ZKCryptoClientService.setParameter(hClient, 4, serverPrivateKey.getBytes(), 4);
        if (0 != ret) {
            logger.info("server set public-key failed: " + ret);
            return;
        }
        byte[] publicKeyClient = new byte[2048];
        int[] retPubKeyClientSize = new int[1];
        retPubKeyClientSize[0] = 2048;
        ret = ZKCryptoClientService.getParameter(hClient, 2, publicKeyClient, retPubKeyClientSize);
        if (0 != ret) {
            logger.info("server get public-key failed: " + ret);
            return;
        }
        String serverPublicKey = new String(publicKeyClient, 0, retPubKeyClientSize[0]);

        KeyModel keyModel = new KeyModel();
        keyModel.setSn(sn);
        keyModel.setDevicePublicKey(devicePublicKey);
        keyModel.setServerPublicKey(serverPublicKey);
        keyModel.setInitKey(hClient);
        keyModel.setServerPrivateKey(serverPrivateKey);
        redisUtil.set(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn, keyModel);
    }

    /**
     * 设备发来因子时处理
     *
     * @param sn
     * @param deviceFactors
     */
    public void handleServerFactors(String sn, String deviceFactors) {
        KeyModel keyModel = (KeyModel) redisUtil.get(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn);
        keyModel.setDeviceFactors(deviceFactors);

        int ret;
        int[] retPubKeyServer = new int[1];
        retPubKeyServer[0] = keyModel.getDevicePublicKey().length();
        ret = ZKCryptoClientService.setParameter(keyModel.getInitKey(), 3, keyModel.getDevicePublicKey().getBytes(), retPubKeyServer[0]);
        if (0 != ret) {
            logger.info("set server public-key failed: " + ret);
            return;
        }
        int[] nR1EncryptedLen = new int[1];
        nR1EncryptedLen[0] = deviceFactors.length();
        ret = ZKCryptoClientService.setParameter(keyModel.getInitKey(), 1, deviceFactors.getBytes(), nR1EncryptedLen[0]);
        if (0 != ret) {
            logger.info("set device r1 failed: " + ret);
            return;
        }

        byte[] r2Encrypted = new byte[1024];
        int[] nR2EncryptedLen = new int[1];
        nR2EncryptedLen[0] = 2048;
        ret = ZKCryptoClientService.getParameter(keyModel.getInitKey(), 4, r2Encrypted, nR2EncryptedLen);
        if (0 != ret) {
            logger.info("get device r2 failed: " + ret);
            return;
        }
        //最终生成的因子
        String serverFactors = new String(r2Encrypted, 0, nR2EncryptedLen[0]);
        keyModel.setServerFactors(serverFactors);
        redisUtil.set(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn, keyModel);
    }

}
