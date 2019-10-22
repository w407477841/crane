package com.xywg.equipment.monitor.iot.netty.device.handler;

import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hjy
 * @date 2018/10/25
 */
public class CommonStaticMethod {

    /**
     * 协议解析
     * 报文组成格式: 固定头部(12个字节) + 固定数据头(序列号长度<2字节>+序列号id<1字节>+序列号长度<16> = 19字节) +数据体
     */
    public static CompleteDataPojo resolutionProtocol(String dataStr) {
        String[] message = dataStr.split(" ");
        //固定码
        String fixedCode = message[0] + message[1] + message[2];
        //数据包版本，如数据包版本为1，则为0x00 0x01
        String version = message[3];
        //命令码
        String command = message[4] + message[5];
        //表示数据包是请求包、应答包还是主动发送包
        String aQ = message[6];
        Integer dataLength = Integer.parseInt((message[7] + message[8]).replaceAll(" ", ""), 16);
        //表示数据表是否正确，正确为0，错误为大于0的正整数
        String errorStatus = message[9];
        //crc16
        String crc16 = message[10] + message[11];
        // 数据体解析
        Map<String, String> dataVolume = handleMessageData(message);
        //报文
        return new CompleteDataPojo(fixedCode, version, command, aQ, dataLength, errorStatus, crc16, dataVolume);
    }

    /**
     * 报文数据体 从第12字节个开始 将所有数据部分转为Map 形式
     *
     * @param message
     * @return
     */
    private static Map<String, String> handleMessageData(String[] message) {
        String[] newData = Arrays.copyOfRange(message, 12, message.length);
        int j = 1;
        Map<String, String> dataMap = new HashMap<>();
        for (int i = 0; i < newData.length; i += j) {
            //数据长度
            int dataLength = Integer.parseInt(newData[i] + newData[i + 1], 16);
            //数据类型
            String dataType = newData[i + 2];
            StringBuilder dataMessage = new StringBuilder();
            //真正的数据前面有长度占2个,类型占1个
            int placeholder = 3;
            for (int k = (i + placeholder); k < dataLength + i; k++) {
                dataMessage.append(newData[k]).append(" ");
            }
            j = dataLength;
            String dataMessageStr = dataMessage.toString();

            dataMap.put(dataType, dataMessageStr.replaceAll(" ",""));
        }
        return dataMap;
    }

    /**
     * byte数组转16进制字符串
     * @param bytes
     * @return
     * @description
     */
    public static String toHexString(byte[] bytes) {
        final String hex = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(hex.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(hex.charAt(b & 0x0f));
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 字符串转16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch) + " ";
            str.append(s4);
        }
        return str.toString();
    }


    /**
     * 16进制字符串转ASCII码(字符串)
     *
     * @param bytes
     * @return
     */
    public static String decode(String bytes) {
        bytes =bytes.replaceAll(" ","");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        String hexString = "0123456789ABCDEF";
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        }
        return new String(baos.toByteArray());
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param message
     * @return
     */
    public static byte[] toBytes(String message) {
        String str = message.replaceAll(" ", "");
        if ("".equals(str.trim())) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }


}
