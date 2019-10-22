package com.xywg.attendance.modular.netty.handler;

import com.xywg.attendance.common.global.RequestUrlEnum;
import com.xywg.attendance.common.model.KeyModel;
import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import com.xywg.attendance.common.utils.RedisUtil;
import com.xywg.attendance.common.utils.UrlUtil;
import com.zkteco.ZKCryptoClientService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.xywg.attendance.common.global.GlobalStaticConstant.DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY;
import static com.xywg.attendance.common.global.GlobalStaticConstant.RABBITMQ_TOPIC_NAME_MQ;
import static com.xywg.attendance.common.global.RequestUrlEnum.ATTENDANCE_RECORD_PICTURE;

/**
 * @author hjy
 * @date 2019/2/21
 * 所有的设备上报处理
 */
@Service
public class MethodService {
    @Autowired
    private MethodServiceHandle methodServiceHandle;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger("");

    /**
     * @return
     */
    public void runMethod(ChannelHandlerContext ctx, TransmissionMessageTemplate template, byte[] msgData) {
        RequestUrlEnum methodNameEnum = RequestUrlEnum.getMethodName(template.getUrl(), template.getMethod());
        if (methodNameEnum == null) {
            commonMethod.sendMessageUTF8(ctx, "Unsupported Request !");
            //没有匹配到相关url 正则表达式
            logger.info("There is no match to the associated URL regular expression: " + template.getUrl());
            return;
        }
        String sn = getSn(template.getUrl());
        logger.info("Device Number: " + sn + "\tFunction Description: " + methodNameEnum.getName()
                + "\tRequest Method: " + template.getMethod() + " \tReceipt Time:"+template.getDate()
                + " \tRequest Path: " + template.getUrl());

        switch (methodNameEnum) {
            //初始化
            case DEVICE_INIT:
                methodServiceHandle.handleDeviceInit(ctx, template);
                break;
            //交换公钥（支持通信加密的场合）
            case HANDLE_EXCHANGE_PUBLIC_KEY:
                template.setBody(commonMethod.getBodyGB2312(msgData));
                methodServiceHandle.handleExchangePublicKey(ctx, template);
                break;
            //交换因子（支持通信加密的场合）
            case HANDLE_EXCHANGE_FACTOR:
                template.setBody(commonMethod.getBodyGB2312(msgData));
                methodServiceHandle.handleExchangeFactor(ctx, template);
                break;
            //获取命令
            case HANDLE_GET_COMMAND:
                //说明如果解密加密失败了 那么就不要给设备回复OK 信息 设备会重新初始化完成后再次发送该消息
                if (!judgeInitInternalValue(sn, template)) {
                    return;
                }
                commonMethod.backupsAndToMq(RABBITMQ_TOPIC_NAME_MQ, template);
                methodServiceHandle.handleGetCommand(ctx, template);
                break;
            //普通考勤机需要走队列的数据
            default:
                handleReply(ctx, template, methodNameEnum, msgData);
                break;
        }
    }

    /**
     * 普通考勤机数据 回复处理  以及发往队列中
     *
     * @param ctx
     * @param template
     * @param method
     */
    private void handleReply(ChannelHandlerContext ctx, TransmissionMessageTemplate template, RequestUrlEnum method, byte[] msgData) {
        String sn = getSn(template.getUrl());

        //说明如果解密加密失败了 那么就不要给设备回复OK 信息 设备会重新初始化完成后再次发送该消息
        if (!judgeInitInternalValue(sn, template)) {
            return;
        }

        //将加密后的消息回复给设备
        // commonMethod.sendMessage(ctx, "OK");
        commonMethod.sendMessageEncrypt(ctx, "OK", sn);
        if (msgData.length == 0) {
            return;
        }
        //先解密
        byte[] bodys = decryptData(msgData, sn);
        if (method.equals(ATTENDANCE_RECORD_PICTURE)) {
            String getBody = commonMethod.getBodyGB2312Photo(bodys);
            template.setBody(getBody);
        } else {
            template.setBody(commonMethod.getBodyGB2312(bodys));
        }
        commonMethod.backupsAndToMq(RABBITMQ_TOPIC_NAME_MQ, template);
    }

    /**
     * 解密
     *
     * @param body 需要解密的数据
     * @param sn   设备编号
     * @return
     */
    public byte[] decryptData(byte[] body, String sn) {
        KeyModel keyModel = (KeyModel) redisUtil.get(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn);
        byte[] decryptData = new byte[body.length];
        int[] retDecryptDataSize = new int[1];
        retDecryptDataSize[0] = body.length;
        int ret = ZKCryptoClientService.decryptDataBase64(keyModel.getInitKey(), body, body.length, decryptData, retDecryptDataSize);
        if (0 != ret) {
            logger.info("server decrypt failed");
        }
        return decryptData;
    }

    /**
     * 加密
     *
     * @param resMessage 需要加密的字符串
     * @param sn         字符串
     * @return
     */
    public byte[] encryptData(String resMessage, String sn) {
        KeyModel keyModel = (KeyModel) redisUtil.get(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn);
        byte[] messByte = new byte[0];
        try {
            messByte = resMessage.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //10 协议头固定长度  （x+15)/16*16, 数据长度16的倍数，不足的补齐
        // 直接用encrypDataBase64, base64数据会扩大1/3,   长度=（encryptLen+2）/3 * 4
        int length = ((messByte.length + 10 + 15) / 16 * 16 + 2) / 3 * 4;
        byte[] encryptData = new byte[length];
        int[] retEncryptDataSize = new int[1];
        retEncryptDataSize[0] = length;
        int ret = ZKCryptoClientService.encryptDataBase64(keyModel.getInitKey(), messByte, messByte.length, encryptData, retEncryptDataSize);
        if (0 != ret) {
            logger.info("server encrypt failed, ret:" + ret);
        }
        return encryptData;
    }


    /**
     * 判断加密解密的的机制是否有在内存中初始化内存地址
     */
    public Boolean judgeInitInternalValue(String sn, TransmissionMessageTemplate template) {
        KeyModel keyModel = (KeyModel) redisUtil.get(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn);
        if (keyModel == null) {
            logger.info("Server Encrypt Or Decrypt Failed --> No Init Internal Value -->" + template.toString());
            return false;
        }
        return true;
    }


    public Map<String, String> handleUrl(String url) {
        String[] path = url.split("\\?");
        if (!url.contains("&") && !url.contains("=")) {
            return null;
        }

        Map<String, String> map = new HashMap<>(1);
        if (path[1].contains("&")) {
            map = UrlUtil.handleSeparate(path[1], "&", "=");
        } else {
            String[] split = path[1].split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }


    public String getSn(String url) {
        Map<String, String> map = handleUrl(url);
        if (map == null) {
            return "";
        }
        if (!map.containsKey("SN")) {
            return "";
        }
        return map.get("SN");
    }

}
