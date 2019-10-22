package com.xingyun.equipment.simpleequipment.receive.handler;

import cn.hutool.core.date.DateUtil;
import com.xingyun.equipment.simpleequipment.core.pojo.CompleteDataPojo;
import com.xingyun.equipment.simpleequipment.core.utils.Crc16Util;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;


;import static com.xingyun.equipment.simpleequipment.core.CommonStaticMethod.decode;
import static com.xingyun.equipment.simpleequipment.core.CommonStaticMethod.toBytes;
import static com.xingyun.equipment.simpleequipment.core.Const.DATA_PACKAGE_RESPONSE;
import static com.xingyun.equipment.simpleequipment.core.Const.XYWG_PROTOCOL_HEAD;

/**
 * @author hjy
 * @date 2018/10/26
 */
@Component
public class CommonMethod {
    private Logger logger = LoggerFactory.getLogger(CommonMethod.class);

    /**
     * 传输升级数据
     *
     * @param monitorPojo 信息对象
     */
    public void upgradeTransfer(Channel channel, CompleteDataPojo monitorPojo) {
        String sn = monitorPojo.getDataMap().get("01").replaceAll(" ", "");


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
     * 同步时间
     * @param channel 上下文
     * @param sn  序列号
     * @param command  所属命令块
     * @param version  协议版本
     */
    public void synchronizationTime(Channel channel, String sn ,String command,String version) {
        //获取服务器当前时间
        String dateTimeStr = DateUtil.format(new Date(), "yyyyMMddHHmmss");
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



}
