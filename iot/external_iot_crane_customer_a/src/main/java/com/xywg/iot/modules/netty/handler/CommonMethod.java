package com.xywg.iot.modules.netty.handler;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.modules.crane.model.DataDomain;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * @author hjy
 * @date 2018/12/28
 * 业务逻辑处理中的公共方法
 */
@Service
public class CommonMethod {
    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 消息回复
     *
     * @param ctx
     * @param dataDomain
     * @param messageLength 信息段所占字节数
     */
    public void sendMessage(ChannelHandlerContext ctx, DataDomain dataDomain, Integer messageLength) {
        //计算总长度: 固定字节数+信息段所占字节数
        Integer leg = 2 + 1 + 1 + 1 + 1 + 4 + messageLength + 1 + 4;
        String length = Integer.toHexString(leg);
        //拼接回复的消息
        StringBuilder sb = new StringBuilder();
        sb.append(dataDomain.getHead()).append(length)
                .append(dataDomain.getVendorNumber())
                .append(dataDomain.getProtocolVersion())
                .append(dataDomain.getCommandCode())
                .append(dataDomain.getDeviceCode())
                .append(dataDomain.getDataMessage());

        String incompleteMessage = sb.toString();
        //计算校验码
        String check = getCheckCode(incompleteMessage);

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(incompleteMessage);
        sBuilder.append(check);
        sBuilder.append(GlobalStaticConstant.PROTOCOL_FIXED_TAIL);
        //发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer(DataUtil.hexStringToBytes(sBuilder.toString())));
    }

    /**
     * 计算校验码
     *
     * @param message 不包含校验码  和结尾标识的信息
     * @return
     */
    private String getCheckCode(String message) {
        return getCheckCode(DataUtil.hexStringToBytes(message));
    }

    /**
     * 字节累加和
     *
     * @param b
     * @return
     */
    public static String getCheckCode(byte[] b) {
        short s = 0;
        // 累加求和
        for (byte aB : b) {
            s += aB;
        }
        return DataUtil.byteToHex((byte) s);
    }


    /**
     * 实时数据 推入redis
     *
     * @param redisUtil  工具
     * @param deviceNo   设备编号
     * @param deviceType 设备类型
     * @param uuid       项目编号
     * @param data       数据
     */
    public void push(RedisUtil redisUtil, String deviceNo, String deviceType, String uuid, Object data, String projectId) {
        String key = "device_platform:current:" + uuid + ":" + deviceType + ":" + deviceNo;
        String topic = "/topic/current/" + deviceType + "/" + uuid;
        String tempTopic = "/topic/current/" + deviceType + "/" + projectId + "/" + deviceNo;
        if (!deviceType.endsWith("alarm")) {
            topic = "/topic/current/" + deviceType + "/" + uuid + "/" + deviceNo;
            redisUtil.set(key, JSONUtil.toJsonStr(data));
            if (StringUtils.isNotEmpty(projectId)) {
                String tempKey ="device_platform:current:" + projectId + ":" + deviceType + ":" + deviceNo;
                redisUtil.set(tempKey, JSONUtil.toJsonStr(data));
                simpMessageSendingOperations.convertAndSend(tempTopic, data);
            }
        }
        //System.out.println("订阅路径：" + topic);
        // 推送至 websocket
        simpMessageSendingOperations.convertAndSend(topic, data);
    }

}






