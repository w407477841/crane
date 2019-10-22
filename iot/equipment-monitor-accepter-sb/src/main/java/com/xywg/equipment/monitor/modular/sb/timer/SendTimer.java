package com.xywg.equipment.monitor.modular.sb.timer;

import com.xywg.equipment.monitor.modular.sb.decoder.SbDecoder;
import com.xywg.equipment.monitor.modular.sb.handler.SbHandler;
import com.xywg.equipment.monitor.modular.sb.init.SbChannelInit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:42 2018/11/2
 * Modified By : wangyifei
 */
@Component
public class SendTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendTimer.class);

    /**
     * 帧标识
     */
    private final String DATA_HEADER = "6810";
    /**
     * 控制码
     */
    private final String DATA_CTRL_CODE = "01";
    /**
     * 结束码
     */
    private final String DATA_END_CODE = "16";

    /**
     * 发送 获取读数指令
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void send(){
        // 迭代 本地所有连接的设备
        Map<String,Channel> channels = SbChannelInit.channels;
        for(String sn:channels.keySet()){
           Channel channel = channels.get(sn);
           if(!channel.isActive()){
               channel.close();
           }else{
               // 发送数据 FF68A28968AA18
               //  1  16进制 String 转 byte[]
               //  2    byte[] 转 ByteBuf s
               //  3   数据域
               String sendStr = "";
               //数据类型 文档page 19 (上1结算日 第二象限无功总电能)
               String dataType = "00000000";
               String addr = sn ;
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

               sendStr = DATA_HEADER + addr +  DATA_CTRL_CODE + "031F9001";
               //偶校验处理
               String cs = getCS(sendStr);

               sendStr += cs + DATA_END_CODE;

               System.out.println("########发送数据："+sendStr+"#########");

               ByteBuf byteBuf = Unpooled.copiedBuffer(ByteBufUtil.decodeHexDump(sendStr.toUpperCase()));
               channel.writeAndFlush(byteBuf);

               //将发送数据插入sendMessageMap中
               SbChannelInit.sendMessage.put(sn,sendStr);



           }


        }




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
        if(addr.length()< SbDecoder.LINK_SN_LENGTH){
            for (int i = 0; i < SbDecoder.LINK_SN_LENGTH-addr.length(); i++) {
                addr = addr+"0";
            }
        }
        return addr;
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
     * 加偏移量 发送数据用到
     * @param msg
     * @return
     */
    private String addChangeNum(String msg){
        msg = Integer.toHexString(Integer.parseInt(msg,16)+ SbHandler.CHANGE_NUM);
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

}
