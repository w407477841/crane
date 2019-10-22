package com.xywg.equipment.monitor.iot.modular.timer;

import com.xywg.equipment.monitor.iot.netty.aop.HexPowerHandler;
import io.netty.buffer.*;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:33 2018/9/12
 * Modified By : wangyifei
 */
@Component
public class AmmeterTimer {

    private static final Logger log=LoggerFactory.getLogger(AmmeterTimer.class);

    /**
     * 帧标识
     */
    private final String DATA_HEADER = "68";
    /**
     * 控制码
     */
    private final String DATA_CTRL_CODE = "11";
    /**
     * 结束码
     */
    private final String DATA_END_CODE = "16";
    /**
     * 地址域长度
     */
    private final int DATA_AREA_LEN = 12;
    /**
     * 偏移量 0x33
     */
    private final int CHANGE_NUM = 51;

    @Scheduled(cron = "0/15 * * * * ?")
    public void sendCommand(){

        for (Iterator<Map.Entry<String, Channel>> it = HexPowerHandler.ameterChannel.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Channel> item = it.next();
            String addr  = item.getKey();
            Channel channel = item.getValue();
            if(!channel.isActive()){
                //已离线
                log.info(addr+"已离线");
                it.remove();
            }else{
                // 发送数据 FF68A28968AA18
                //  1  16进制 String 转 byte[]
                //  2    byte[] 转 ByteBuf s
                //  3   数据域
                String sendStr = "";
                //数据类型 文档page 19 (上1结算日 第二象限无功总电能)
                String dataType = "00000000";

                addr = getRealAddr(addr);

                //负荷记录块
                String logN = "";
                //时间 mmhhDDMMYY 形式
                String dateStr = "";
                //数据域数据部分
                String dataArea = dataType + logN + dateStr;
                //偏移处理
                dataArea = beforeSend(dataArea);

                //数据长度
                String dataLenStr = Integer.toHexString(dataArea.length()/2);
                if(dataLenStr.length()<2){
                    dataLenStr = "0"+dataLenStr;
                }

                //地址取反
                addr = doPathSort(addr);

                sendStr = DATA_HEADER + addr + DATA_HEADER + DATA_CTRL_CODE + dataLenStr + dataArea;
                //偶校验处理
                String cs = getCS(sendStr);

                sendStr += cs + DATA_END_CODE;

                System.out.println("#######################################");
                System.out.println("发送数据："+sendStr);
                System.out.println("#######################################");

                ByteBuf byteBuf = Unpooled.copiedBuffer(ByteBufUtil.decodeHexDump(sendStr.toUpperCase()));
                channel.writeAndFlush(byteBuf);

                //将发送数据插入sendMessageMap中
                addr = doPathSort(addr);
                HexPowerHandler.sendAmeterMessage.put(addr,sendStr);

                log.info("数据发送完毕");
            }

        }

    }


    /**
     * 对发送数据进行偏移处理
     * @param dataStr
     * @return
     */
    private String beforeSend(String dataStr){
        String newStr = "";
        for (int i = 0; i < dataStr.length()/2; i++) {
            newStr += addChangeNum(dataStr.substring(i*2,(i+1)*2));
        }
        return newStr;
    }

    /**
     * 获取偶校验CS的值
     * @return
     */
    private String getCS(String str){
        int sum = 0;
        for (int i = 0; i < str.length()/2; i++) {
            sum += Integer.parseInt(str.substring(i*2,(i+1)*2),16)%256;
        }
        String kou = Integer.toHexString(sum);
        return kou.substring(kou.length()-2,kou.length());
    }

    /**
     * 对地址域数据校验
     * @param addr
     * @return
     */
    private String getRealAddr(String addr){

        //高位补0
        if(addr.length()<DATA_AREA_LEN){
            for (int i = 0; i < DATA_AREA_LEN-addr.length(); i++) {
                addr = addr+"0";
            }
        }

        return addr;
    }

    /**
     * 加偏移量 发送数据用到
     * @param msg
     * @return
     */
    private String addChangeNum(String msg){
        msg = Integer.toHexString(Integer.parseInt(msg,16)+CHANGE_NUM);
        return msg;
    }

    /**
     * 地址顺序去反
     * @param str
     * @return
     */
    private String doPathSort(String str){
        String result = "";
        for (int i = 0; i < str.length()/2; i++) {
            result = str.substring(i*2,(i+1)*2) + result;
        }
        return result;

    }

    public static void main(String[]  args){
        AmmeterTimer at = new AmmeterTimer();
        String s = "6830000206182068110433333333";
        System.out.println(at.getCS(s));

        System.out.println(at.doPathSort("201806020003"));

    }

}
