package com.xywg.equipment.sandplay.core.util.rxtx;

import com.xywg.equipment.sandplay.core.vo.SerialVO;
import gnu.io.*;

import javax.swing.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Auther: SJ
 * @Date: 2018/9/26 14:05
 * @Description:
 */
public class RxtxExport {

    private static DateTimeFormatter df= DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");

    /**
     * 获取串口信息
     */
    public void getCom(){
        SerialTool.commListMap.remove("test1");
        List<Map<String,String>> commList = SerialTool.findPort(); //程序初始化时就扫描一次有效串口
        //检查是否有可用串口，有则加入选项中
        if (commList == null || commList.size()<1) {
            System.out.println( "没有搜索到有效串口！");
        }else{
            SerialTool.commListMap.put("test1",commList);
        }

    }

    /**
     * 通过制定波特率打开串口
     */
    public SerialPort openCom(String commName,String bpsStr){
        SerialPort sp = null;
        //检查串口名称是否获取正确
        if (commName == null || "".equals(commName)) {
            System.out.println("没有搜索到有效串口！");
        }else {
            //检查波特率是否获取正确
            if (bpsStr == null || "".equals(bpsStr)) {
                System.out.println("波特率获取错误！");
            }else {
                //串口名、波特率均获取正确时
                int bps = Integer.parseInt(bpsStr);
                try {
                    //关闭所有非称重串口
                    Iterator<Map.Entry<String, SerialVO>> entries = SerialTool.serialMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, SerialVO> entry = entries.next();
                        String keyName = entry.getKey();
                        SerialVO serVO = entry.getValue();
                        if(!serVO.isWeightPort()){
                            System.out.println(1);
                            SerialTool.closePort(serVO.getSerialPort());
                        }
                    }

                    //获取指定端口名及波特率的串口对象
                    sp = SerialTool.openPort(commName, bps);
                    SerialTool.serialMap.put(commName,SerialVO.factoryNotWeight(true,sp));
                    //SerialTool.serialPortMap.put("test1",sp);
                    SerialTool.addListener(SerialTool.serialMap.get(commName).getSerialPort(), new SerialListener(commName));
                    SerialTool.comm = commName;
                    if(SerialTool.serialMap.get(commName).getSerialPort()==null){
                        return null;
                    }
                    //在该串口对象上添加监听器
                    String time=df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Shanghai")));
                    System.out.println(time+" ["+SerialTool.serialMap.get(commName).getSerialPort().getName().split("/")[3]+"] : "+" 连接成功..."+"\r\n");
                } catch (UnsupportedCommOperationException | PortInUseException | NoSuchPortException | TooManyListenersException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sp;
    }

    /**
     * 发送信息
     * @param message
     */
    public void sendMsg(String commName,String message){
        System.out.println(commName);
        SerialVO serialVO =  SerialTool.serialMap.get(commName);
        try {
            if(serialVO.isOpen()){
                SerialTool.sendToPort(serialVO.getSerialPort(), hex2byte(message));
            }

        } catch (IOException e1) {
            serialVO.setOpen(false);
            serialVO =  SerialTool.serialMap.put(commName,serialVO);
        }
    }

    /**
     * 关闭串口
     */
    public void closePort(String commName){
        SerialVO serialVO =  SerialTool.serialMap.get(commName);
        if(serialVO.isOpen()){
            SerialPort serialPort  = SerialTool.serialMap.get(commName).getSerialPort();
            SerialTool.closePort(serialPort);
            serialVO = SerialVO.factoryNotWeight(false,serialPort);
            SerialTool.serialMap.put(commName,serialVO);
        }
        String time=df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),ZoneId.of("Asia/Shanghai")));
        System.out.println(time+" ["+SerialTool.serialPortMap.get("test1").getName().split("/")[3]+"] : "+" 断开连接"+"\r\n");
    }

    /**
     * 以内部类形式创建一个串口监听类
     * @author zhong
     */
    class SerialListener implements SerialPortEventListener {

        private String comm;

        public SerialListener(String comm) {
            this.comm = comm;
        }

        /**
         * 处理监控到的串口事件
         */
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {

            switch (serialPortEvent.getEventType()) {
                // 10 通讯中断
                case SerialPortEvent.BI:
                    JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
                    break;
                // 7 溢位（溢出）错误
                case SerialPortEvent.OE:
                    break;
                // 9 帧错误
                case SerialPortEvent.FE:
                    break;
                // 8 奇偶校验错误
                case SerialPortEvent.PE:
                    break;
                // 6 载波检测
                case SerialPortEvent.CD:
                    break;
                // 3 清除待发送数据
                case SerialPortEvent.CTS:
                    break;
                // 4 待发送数据准备好了
                case SerialPortEvent.DSR:
                    break;
                // 5 振铃指示
                case SerialPortEvent.RI:
                    break;
                // 2 输出缓冲区已清空
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                    break;
                // 1 串口存在可用数据
                case SerialPortEvent.DATA_AVAILABLE:
                    String time=df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Shanghai")));
                    //FE0400030001D5C5
                    byte[] data;
                    try {
                        data = SerialTool.readFromPort(SerialTool.serialMap.get(comm).getSerialPort());
                        System.out.println(time+" ["+SerialTool.serialMap.get(comm).getSerialPort().getName().split("/")[3]+"] : "+ printHexString(data)+"\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**字符串转16进制
     * @param hex
     * @return
     */
    public static  byte[] hex2byte(String hex) {

        String digital = "0123456789ABCDEF";
        String hex1 = hex.replace(" ", "");
        char[] hex2char = hex1.toCharArray();
        byte[] bytes = new byte[hex1.length() / 2];
        byte temp;
        for (int p = 0; p < bytes.length; p++) {
            temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
            temp += digital.indexOf(hex2char[2 * p + 1]);
            bytes[p] = (byte) (temp & 0xff);
        }
        return bytes;
    }

    /**字节数组转16进制
     * @param b
     * @return
     */
    public static String printHexString(byte[] b) {

        StringBuffer sbf=new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sbf.append(hex.toUpperCase()+"  ");
        }
        return sbf.toString().trim();
    }

}
