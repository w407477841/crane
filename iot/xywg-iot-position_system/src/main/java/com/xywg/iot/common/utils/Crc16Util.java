package com.xywg.iot.common.utils;

/**
 * @author hjy
 * @date 2018/9/18
 * 基于Modbus CRC16的校验算法工具类
 */
public class Crc16Util {
    /**
     * 获取源数据和验证码的组合byte数组
     *
     * @param strings 可变长度的十六进制字符串
     * @return
     */
    public static byte[] getData(String... strings) {
        byte[] data = new byte[]{};
        for (int i = 0; i < strings.length; i++) {
            int x = Integer.parseInt(strings[i], 16);
            byte n = (byte) x;
            byte[] buffer = new byte[data.length + 1];
            byte[] aa = {n};
            System.arraycopy(data, 0, buffer, 0, data.length);
            System.arraycopy(aa, 0, buffer, data.length, aa.length);
            data = buffer;
        }
        return getData(data);
    }

    /**
     * 获取源数据和验证码的组合byte数组
     *
     * @param aa 字节数组
     * @return
     */
    private static byte[] getData(byte[] aa) {
        byte[] bb = getCrc16(aa);
        byte[] cc = new byte[aa.length + bb.length];
        System.arraycopy(aa, 0, cc, 0, aa.length);
        System.arraycopy(bb, 0, cc, aa.length, bb.length);
        return cc;
    }

    /**
     * 获取验证码byte数组，基于Modbus CRC16的校验算法
     */
    private static byte[] getCrc16(byte[] arr_buff) {
        int len = arr_buff.length;

        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                // 如果移出位为 0,再次右移一位
                {
                    crc = crc >> 1;
                }
            }
        }
        return intToBytes(crc);
    }

    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将字节数组转换成十六进制字符串
     */
    public static String byteTo16String(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : data) {
            buffer.append(byteTo16String(b));
        }
        return buffer.toString();
    }

    /**
     * 将字节转换成十六进制字符串
     * int转byte对照表
     * [128,255],0,[1,128)
     * [-128,-1],0,[1,128)
     */
    public static String byteTo16String(byte b) {
        StringBuffer buffer = new StringBuffer();
        int aa = (int) b;
        if (aa < 0) {
            buffer.append(Integer.toString(aa + 256, 16) + " ");
        } else if (aa == 0) {
            buffer.append("00 ");
        } else if (aa > 0 && aa <= 15) {
            buffer.append("0" + Integer.toString(aa, 16) + " ");
        } else if (aa > 15) {
            buffer.append(Integer.toString(aa, 16) + " ");
        }
        return buffer.toString();
    }


    /**
     * 根据16进账字符串计算出CRC 校验码  高位在前  低位在后
     * @param crcOriginalString
     * @return
     */
    public static String getCRC16(String crcOriginalString){
        String newStr=crcOriginalString.replaceAll(" ","");
        String regex = "(.{2})";
        newStr = newStr.replaceAll (regex, "$1 ");

        //计算出新的CRC16
        byte[] dd = Crc16Util.getData(newStr.split(" "));
        String crcString = Crc16Util.byteTo16String(dd).toUpperCase();
        //计算出来的校验位(去除多余的原始数据位,只保留CRC位)
        String crcCode = crcString.substring(crcString.length() - 6).replaceAll(" ", "");
        //将低位在前转为高位在前
        return  crcCode.substring(2, 4) + crcCode.substring(0, 2);
    }
    
    
    public static void main(String[] args) {

        byte[] dd = Crc16Util.getData("00 13 03 58 59 32 30 31 38 30 35 30 31 30 31 30 30 30 31 00 06 0B 33 3E 31 00 04 0C 31 00 04 0D 01 00 06 0E 31 30 30 00 06 0F 31 30 30 00 07 10 32 35 2E 31 00 07 11 36 30 2E 33 00 07 12 37 31 2E 34".split(" "));
        String str = Crc16Util.byteTo16String(dd).toUpperCase();
        System.out.println(str);


        getCRC16("0013035859 32 30 31 38 30 35 30 31 30 31 30 30 30 31 00 06 0B 33 3E 31 00 04 0C 31 00 04 0D 01 00 06 0E 31 30 30 00 06 0F 31 30 30 00 07 10 32 35 2E 31 00 07 11 36 30 2E 33 00 07 12 37 31 2E 34");
    }
}
