package com.xywg.equipment.monitor.iot.netty.device.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.config.rabbitmq.RabbitConfig;
import com.xywg.equipment.monitor.iot.core.util.Crc16Util;
import com.xywg.equipment.monitor.iot.core.util.DateUtils;
import com.xywg.equipment.monitor.iot.core.util.FileUtil;
import com.xingyun.crane.cache.RedisUtil;;
import com.xywg.equipment.monitor.iot.modular.base.dao.DeviceCommonInfoMapper;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.common.enums.DeviceTableEnum;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

import static com.xywg.equipment.global.GlobalStaticConstant.*;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.*;

/**
 * @author hjy
 * @date 2018/10/26
 */
@Component
public class CommonMethod {
    @Autowired
    protected XywgProerties xywgProerties;
    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired
    private DeviceCommonInfoMapper deviceCommonInfoMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtil redisUtil;
    private Logger logger = LoggerFactory.getLogger(CommonMethod.class);

    /**
     * 传输升级数据
     *
     * @param monitorPojo 信息对象
     */
    public void upgradeTransfer(Channel channel, CompleteDataPojo monitorPojo) {
        String sn = monitorPojo.getDataMap().get("01").replaceAll(" ", "");

        String deviceCode = decode(sn);
        String path = redisUtil.get(xywgProerties.getRedisHead() + ":upgradeSn_" + deviceCode).toString();
        File file = new File(xywgProerties.getUpgradeFileBasePath() + path);
        byte[] bytes = FileUtil.file2Byte(file);

        //已发送的文件的内容长度
        Integer length = Integer.parseInt(monitorPojo.getDataMap().get("04")
                .replaceAll(" ", ""), 16);
        byte[] newBytes;
        //最大只能发送1024个字节
        Integer everyTimeMaxLength = 1024;
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //本次发送刚好1024字节
        assert bytes != null;
        if (length + everyTimeMaxLength < bytes.length) {
            newBytes = Arrays.copyOfRange(bytes, length, everyTimeMaxLength + length);
            String message16 = toHexString(newBytes);
            map.put("03", message16);
            map.put("05", Integer.toHexString(everyTimeMaxLength));
            resMessageJoint(channel, sn, map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
            //本次发送不足1024字节
        } else if (length < bytes.length && (length + everyTimeMaxLength) > bytes.length) {
            newBytes = Arrays.copyOfRange(bytes, length, bytes.length);
            String message16 = toHexString(newBytes);
            map.put("03", message16);
            map.put("05", Integer.toHexString(bytes.length - length));
            resMessageJoint(channel, sn, map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        } else {
            //升级文件发送完毕
            resMessageJoint(channel, sn, map, "0007", monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        }
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
        String key = xywgProerties.getRedisHead() + ":current:" + uuid + ":" + deviceType + ":" + deviceNo;
        String topic = "/topic/current/" + deviceType + "/" + uuid;
        String tempTopic = "/topic/current/" + deviceType + "/" + projectId + "/" + deviceNo;
        if (!deviceType.endsWith("alarm")) {
            topic = "/topic/current/" + deviceType + "/" + uuid + "/" + deviceNo;
            redisUtil.set(key, JSONUtil.toJsonStr(data));
            if (StringUtils.isNotEmpty(projectId)) {
                String tempKey = xywgProerties.getRedisHead() + ":current:" + projectId + ":" + deviceType + ":" + deviceNo;
                redisUtil.set(tempKey, JSONUtil.toJsonStr(data));
                simpMessageSendingOperations.convertAndSend(tempTopic, data);
            }
        }
        simpMessageSendingOperations.convertAndSend(topic, data);
    }
    private void pushStatus(String uuid,String projectId,String deviceNo,Integer status){
        String data = "{'deviceNo':'"+deviceNo+"','status':"+status+"}";
        String localTopic = "/topic/deviceStatus/"+projectId+"/"+deviceNo;
        simpMessageSendingOperations.convertAndSend(localTopic, data);
        if(StrUtil.isNotBlank(uuid)){
            String publicTopic = "/topic/deviceStatus/"+uuid+"/"+deviceNo;
            simpMessageSendingOperations.convertAndSend(publicTopic, data);
        }
    }
    public void pushError(String json){
        String localTopic = "/topic/errorMsg/admin";
        simpMessageSendingOperations.convertAndSend(localTopic,json);
    }

    public void userMsg(String userName,String type,String content){
        String localTopic = "/topic/userMsg/"+userName;
        String json = "{'userName':'"+userName+"','type':'"+type+"','content':'"+content+"'}";
        simpMessageSendingOperations.convertAndSend(localTopic,json);
    }

    public void pushOnline(String uuid,String projectId,String deviceNo){
        pushStatus(uuid,projectId,deviceNo,1);
    }
    public void pushOffline(String uuid,String projectId,String deviceNo){
        pushStatus(uuid,projectId,deviceNo,0);
    }

    /**
     * 回复消息的拼接(数据体有变动时使用该方法)
     *
     * @param channel
     * @param sn16            设备序列号 16进制串
     * @param map             key 消息的id  value: 一个回复消息的消息内容 16进制字符串
     * @param command         功能码 16进制串
     * @param protocolVersion 协议版本   16进制串
     * @param aQ              00 为应答包,01为请求包
     */
    @SuppressWarnings("all")
    public void resMessageJoint(Channel channel, String sn16, LinkedHashMap<String, String> map, String command, String protocolVersion, String aQ) {
        String message =  messageJoint(channel, sn16, map, command, protocolVersion,  aQ);
        /*System.out.println("回复:" + message);*/
        channel.writeAndFlush(Unpooled.copiedBuffer(toBytes(message)));

        logger.info("System Reply:<<Device:" + decode(sn16) + " Message:" + message + ">>");
    }

    private String messageJoint(Channel channel, String sn16, LinkedHashMap<String, String> map, String command, String protocolVersion, String aQ) {
        //消息错误类型,00 表示正常数据,其他代表有错  在这里只会是正确的数据
        String messageStatus = "00";
        //31个固定长度
        String total;
        if (map != null && map.size() > 0) {
            int length = 0;
            for (String key : map.keySet()) {
                int messageLength = complement2(map.get(key).replaceAll(" ", "")).length() / 2 + 3;
                length += messageLength;
            }
            total = Integer.toHexString(31 + length);
        } else {
            total = Integer.toHexString(31);
        }
        //总长度(总长度不可能比10小,所以不考虑长度是1位的情况)
        String totalLength = complement(total);

        //设备序列号数据体 :  0013 为长度,01 为消息id
        String dev = "001301" + sn16;

        //回复的数据体
        StringBuilder resMessage = new StringBuilder();
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                //回复的当前id消息内容(消息长度是奇数位时高位补0)
                String message = complement2(map.get(key).replaceAll(" ", ""));
                //回复的数据体长度
                String resMessageLength = Integer.toHexString(3 + message.length() / 2);
                //因为长度位一共占2个字节,故需要补0操作
                String messageLength = complement(resMessageLength);
                //回复的数据体(长度+id+内容)
                resMessage.append(messageLength).append(key).append(message);
            }
        }
        //数据体
        String data = dev + resMessage.toString();

        //得到crc16
        String crc = Crc16Util.getCRC16(data);

        //拼接最终16进制字符串
        String message = XYWG_PROTOCOL_HEAD + protocolVersion + command + aQ + totalLength + messageStatus + crc + data;
        return message;
    }


    /**
     * 数据长度不够时高位补位  一共四位
     *
     * @return 返回值
     */
    private static String complement(String message) {
        if (message.length() == 1) {
            message = ("000" + message);
        } else if (message.length() == 2) {
            message = ("00" + message);
        } else if (message.length() == 3) {
            message = ("0" + message);
        }
        return message;
    }

    private static String complement2(String message) {
        if (message.length() % 2 == 0) {
            return message;
        } else {
            return "0" + message;
        }
    }

    /**
     * 转换版本号
     *
     * @return 返回设备版本
     */
    public String getDeviceVersion(String version) {
        StringBuilder sb = new StringBuilder();
        sb.append("v");
        for (int i = 0; i < version.length(); i = i + 2) {
            sb.append(Integer.parseInt(version.substring(i, i + 2),16)).append(".");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 判断设备在线装备
     */
    public void handleDeviceIsOnline(String sn, Integer state) {
        //获取设备类型
        String deviceType = sn.substring(8, 10);
        String tableName = DeviceTableEnum.getDeviceTypeTableName(deviceType);
        //如果是设备上线
        if (state == DEVICE_STATE_ON_LINE) {
            //如果没有保存过连接,说明是新的连接,更新数据数据库设备为在线,如果保存过连接了说明是一直在线的,不更新数据库
            /*if (!MonitorHandler.CONTEXT_MAP.containsKey(sn)) {*/
            deviceCommonInfoMapper.updateDeviceIsOnLineState(tableName, state, sn);
            /* }*/
            //设备下线
        } else {
            deviceCommonInfoMapper.updateDeviceIsOnLineState(tableName, state, sn);
        }

    }

    /**
     * 同步时间
     * @param channel 上下文
     * @param sn  序列号
     * @param command  所属命令块
     * @param version  协议版本
     */
    public void synchronizationTime(Channel channel, String sn ,String command,String version) {
        //获取服务器当前时间
        String dateTimeStr = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        //02 代表功能数据的id  即回复的时间
        linkedHashMap.put("02", dateTimeStr);
        resMessageJoint(channel, sn, linkedHashMap, command, version, DATA_PACKAGE_RESPONSE);
    }


    /**
     * 错误信息  (暂时没有用到,但是以后协议完善后可能会用到,勿删)
     *
     * @param errorStatus 错误信息
     * @return 返回
     */
    private String getErrorMessage(String errorStatus) {
        String errorMessage;
        switch (errorStatus) {
            case "00":
                errorMessage = "数据包正确";
                break;
            case "01":
                errorMessage = "数据包版本错误";
                break;
            case "02":
                errorMessage = "数据包FLAG错误";
                break;
            case "03":
                errorMessage = "命令码错误";
                break;
            case "04":
                errorMessage = "数据包长度错误";
                break;
            case "05":
                errorMessage = "报头最后一个字节错误";
                break;
            case "06":
                errorMessage = "TF卡不存在";
                break;
            case "07":
                errorMessage = "升级文件长度错误";
                break;
            case "08":
                errorMessage = "CRC错误";
                break;
            default:
                errorMessage = "";
                break;
        }
        return errorMessage;
    }

  public void send2Mq(String data){
      rabbitTemplate.convertAndSend(RabbitConfig.CRANE_EXCHANGE,RabbitConfig.CRANE_BINDINGDS[0][1],data);
  }

}
