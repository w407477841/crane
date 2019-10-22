package com.xywg.equipment.sandplay.timer;


import com.xywg.equipment.sandplay.config.properties.Cmd;
import com.xywg.equipment.sandplay.core.util.rxtx.RxtxExport;
import com.xywg.equipment.sandplay.core.util.rxtx.SerialTool;
import com.xywg.equipment.sandplay.core.vo.SerialVO;
import com.xywg.equipment.sandplay.modular.sandplay.model.ProjectLoadometerDetail;
import com.xywg.equipment.sandplay.modular.sandplay.service.IProjectLoadometerDetailService;
import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;




import javax.swing.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

/**
 * @author : wangyifei
 * Description  串口定时任务
 *  1.读取所有串口
 *
 * Date: Created in 11:26 2018/9/27
 * Modified By : wangyifei
 */
@Component
@ConditionalOnProperty(prefix = "cmd",name = "weightTimer",havingValue = "true")
public class SerialTimer {
@Autowired
    private Cmd cmd;
@Autowired
    private IProjectLoadometerDetailService  projectLoadometerDetailService;

    private static Logger log = LoggerFactory.getLogger(SerialTimer.class);

    @Autowired
    private IProjectLoadometerDetailService ipldService;

    /**
     * 打开 称重
     */
 @Scheduled(cron = "0/10 * * * * ?")
    public void openWeight(){
    if(cmd.isWeightTimer()) {
        if (!SerialTool.isOpen) {
            String comm = cmd.getWeightSerialPort();
            SerialPort serialPort = null;
            try {

                SerialVO serialVO = SerialTool.serialMap.get(comm);
                if (serialVO == null) {
                    log.info("##################开启称重串口" + comm + "###############");
                    //第一次
                    serialVO = SerialVO.factoryWeight(true, SerialTool.openPort(comm, 9600));
                    SerialTool.addListener(serialVO.getSerialPort(), new SerialListener(comm));

                } else {
                    if (!serialVO.isOpen()) {
                        //关闭的
                        log.info("##################开启" + comm + "###############");
                        serialVO = SerialVO.factoryWeight(true, SerialTool.openPort(comm, 9600));
                        SerialTool.addListener(serialVO.getSerialPort(), new SerialListener(comm));

                    } else {
                        //已开启  ，说明被别的地方开了，不需要再开启
                        log.info("##################串口" + comm + " 已被启用，不能再次开启###############");

                    }


                }


                log.info("##################存储" + comm + "串口上下文###############");
                SerialTool.serialMap.put(comm, serialVO);


            } catch (UnsupportedCommOperationException e) {
                log.info("##################" + comm + "不支持的串口操作###############");
            } catch (PortInUseException e) {
                log.info("##################" + comm + "开启失败###############");
            } catch (NoSuchPortException e) {
                log.info("##################" + comm + "端口不存在###############");
            } catch (TooManyListenersException e) {
                log.info("##################" + comm + "监听太多###############");
            }

        } else {
            log.info("##################已发现适配串口###############");

        }
    }else{
        log.info("##################未开启###############");
    }

    }


    /**
     * 自动扫串口
     *
     * 使用场景： 串口打开后连续忘上位机发送数据
     *
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void readAllSerials(){
    if(cmd.isTimer()) {
        if (!SerialTool.isOpen) {
            log.info("##################开始查询可用串口###############");
            RxtxExport rxtxExport = new RxtxExport();
            rxtxExport.getCom();
            List<Map<String,String>> comms = SerialTool.commListMap.get("test1");
            for (Map<String,String> commMap : comms) {
                SerialPort serialPort = null;
                try {

                    SerialVO serialVO = SerialTool.serialMap.get(commMap.get("name"));
                    if (serialVO == null) {
                        log.info("##################开启" + commMap.get("name") + "###############");
                        //第一次
                        serialVO = SerialVO.factoryNotWeight(true, SerialTool.openPort(commMap.get("name"), 9600));
                        SerialTool.addListener(serialVO.getSerialPort(), new SerialListener(commMap.get("name")));
                        log.info("##################开启线程等待关闭" + commMap.get("name") + "###############");
                        new Thread(new ClosePort(commMap.get("name"))).start();

                    } else {
                        if (!serialVO.isOpen()) {
                            //关闭的
                            log.info("##################开启" + commMap.get("name") + "###############");
                            serialVO = SerialVO.factoryNotWeight(true, SerialTool.openPort(commMap.get("name"), 9600));
                            SerialTool.addListener(serialVO.getSerialPort(), new SerialListener(commMap.get("name")));
                            log.info("##################开启线程等待关闭" + commMap.get("name") + "###############");
                            new Thread(new ClosePort(commMap.get("name"))).start();

                        } else {
                            //已开启  ，说明被别的地方开了，不需要再开启
                            log.info("##################串口" + commMap.get("name") + " 已被启用，不能再次开启###############");

                        }


                    }


                    log.info("##################存储" + commMap.get("name") + "串口上下文###############");
                    SerialTool.serialMap.put(commMap.get("name"), serialVO);


                } catch (UnsupportedCommOperationException e) {
                    log.info("##################" + commMap.get("name") + "不支持的串口操作###############");
                } catch (PortInUseException e) {
                    log.info("##################" + commMap.get("name") + "开启失败###############");
                } catch (NoSuchPortException e) {
                    log.info("##################" + commMap.get("name") + "端口不存在###############");
                } catch (TooManyListenersException e) {
                    log.info("##################" + commMap.get("name") + "监听太多###############");
                }
            }

        } else {
            log.info("##################已发现适配串口###############");

        }
    }




    }

    /**
     *  关闭 串口
     */
    class ClosePort implements Runnable{

     private String comm;


        public ClosePort(String comm) {
            this.comm = comm;
        }

        @Override
        public void run() {
            try {
                log.info("##################获取串口上下文###############");
                SerialVO serialVO  =   SerialTool.serialMap.get(comm);
                if(serialVO == null){
                    log.info("##################SerialVO 为空,结束线程###############");
                    return ;
                }
                SerialPort serialPort  = serialVO.getSerialPort();
                if(serialPort == null){
                    log.info("##################serialPort 为空,结束线程###############");
                    return ;
                }
                log.info("##################暂停2秒###############");
                Thread.sleep(1000);
                log.info("##################重新获取串口上下文###############");
                serialVO  =   SerialTool.serialMap.get(comm);
                serialPort  = serialVO.getSerialPort();
                if(!serialVO.isWeightPort()){
                    log.info("##################该串口不是电子秤串口###############");
                    serialPort.close();
                    serialVO = SerialVO.factoryNotWeight(false,serialPort) ;
                    SerialTool.serialMap.put(comm,serialVO);
                }else{
                    log.info("##################该串口是电子秤串口###############");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 以内部类形式创建一个串口监听类
     * @author zhong
     */
    class SerialListener implements SerialPortEventListener {

        private String   comm;

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
                    //FE0400030001D5C5
                    byte[] data;
                    try {
                        SerialVO  serialVO  = SerialTool.serialMap.get(comm);
                        SerialPort serialPort = serialVO .getSerialPort();
                        if(!serialVO.isWeightPort()){
                            log.info("####################设置为电子秤串口############################");
                            SerialTool.serialMap.put(comm,SerialVO.factoryWeight(true,serialPort)) ;
                        }
                        log.info("##################开始接受数据###############");
                        data = SerialTool.readFromPort(serialPort);
                        String str  = new String(data);

                        str = str.trim().replaceAll("\r|\n|[a-zA-Z]|\\+|=","");
                        BigDecimal zeroStart =   new BigDecimal("12.06");
                        BigDecimal zeroEnd =   new BigDecimal("12.12");

                        BigDecimal curr=    new BigDecimal(str);
                        if(curr.compareTo(zeroStart)==-1||curr.compareTo(zeroEnd)==1){
                            log.info("##################压力传感器数值###############");
                            log.info("##################"+str+"###############");
                            String convert  = "19."+str.split("\\.")[1];
                            Date date = new Date();
                            ProjectLoadometerDetail pld = new ProjectLoadometerDetail();
                            pld.setTimeBegin(date);
                            pld.setTimeEnd(date);
                            pld.setTimeEnd(date);
                            pld.setWeightGross(convert);
                            pld.setPid(2);
                            pld.setIsDel(0);
                            ipldService.insert(pld);
                        }


                        SerialTool.isOpen =true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }






    public static void main(String [] args){
        //开启 串口 COMM3



    }

    /**
     * 一秒向秤砣请求一次数据
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void doRequestWeight(){
        if(cmd.isWeightTimer()) {
            SerialVO serialVO = SerialTool.serialMap.get(cmd.getWeightSerialPort());
            if(null!=serialVO){
                if(serialVO.isOpen()){
                    SerialPort serialPort =  serialVO.getSerialPort();
                    try {
                        SerialTool.sendToPort(serialPort,"AT+F\r\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
