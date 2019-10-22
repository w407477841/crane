package com.xywg.iot.common.utils;


import java.util.UUID;

/**
 * 进制转换工具类
 *
 * @author hjy
 */
public class DataUtil {
    /**
     * byte 数组的截取
     *
     * @param data       原始数组
     * @param startIndex 截取的开始位
     * @param subLength  截取多少位
     * @return 返回截取的数组
     */
    public static byte[] subByteArray(byte[] data, int startIndex, int subLength) {
        byte[] code = new byte[subLength];
        System.arraycopy(data, startIndex, code, 0, subLength);
        return code;
    }

    /**
     * 单字节转十六进制字符串
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * byte 数组转16进制字符串
     *
     * @param bytes 数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
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
     * 将16进制字符串转换为byte[]
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString.trim())) {
            return new byte[0];
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            String subStr = hexString.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }


    /**
     * 字符串转换成十六进制字符串
     */
    public static String stringToHexString(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    /**
     * 将byte数组转换成字符串(ascii 码)
     * @param b byte数组
     * @return 字符串
     */
    public static String byteToString(byte[] b) {
        return new String(b);
    }


    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 二进制字符串转16进制字符串
     * @param binaryStr
     * @return
     */
    public static String binaryStrToHexStr(String binaryStr) {
        if (binaryStr == null || "".equals(binaryStr) || binaryStr.length() % 4 != 0) {
            return null;
        }
        StringBuffer sbs = new StringBuffer();
        // 二进制字符串是4的倍数,所以四位二进制转换成一位十六进制
        for (int i = 0; i < binaryStr.length() / 4; i++) {
            String subStr = binaryStr.substring(i * 4, i * 4 + 4);
            String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));
            sbs.append(hexStr);
        }
        return sbs.toString();
    }

    /**
     * 将十六进制的字符串转换成二进制的字符串
     *
     * @param hexString
     * @return
     */
    public static String hexStrToBinaryStr(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        // 将每一个十六进制字符分别转换成一个四位的二进制字符
        for (int i = 0; i < hexString.length(); i++) {
            String indexStr = hexString.substring(i, i + 1);
            StringBuilder binaryStr = new StringBuilder(Integer.toBinaryString(Integer.parseInt(indexStr, 16)));
            while (binaryStr.length() < 4) {
                binaryStr.insert(0, "0");
            }
            sb.append(binaryStr);
        }
        return sb.toString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
