package com.xywg.equipment.sandplay.core.util;

import com.xywg.equipment.sandplay.core.enums.DeviceType;
import com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author : wangyifei
 * Description  称重指令发送
 * Date: Created in 11:13 2018/10/15
 * Modified By : wangyifei
 */
public class WeightThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightThread.class);
    /**
     *
     */
    private static final int COUNT   =  14;

    @Override
    public void run() {
        //发送指令
        sleep(1500);
        for(  int  i  =COUNT  ;i>0;i-- ){

            //发送指令
            send();
            //睡眠1秒
            sleep(1000);
        }
    }

    private  void send (){
            Channel channel = AllChannelInit.channelMap.get(AllChannelInit.KEY);
        if(channel == null){
            LOGGER.info("################################");
            LOGGER.info(" 沙盘未连接");
            LOGGER.info("################################");
            return ;
        }
            try {
                if (!channel.isActive()) {
                    LOGGER.info("#################################");
                    LOGGER.info(" 沙盘已离线");
                    LOGGER.info("#################################");
                   channel.close();
                } else {
                    ByteBuf byteBuf = Unpooled.copiedBuffer(("02AT+F\r\n").getBytes());
                    channel.writeAndFlush(byteBuf);

                }
            } catch (Exception ex) {
                LOGGER.error("###########################");
                LOGGER.error("#########发送失败#########");
                LOGGER.error("###########################");
            }
    }
   private void sleep(long t){
       try {
           Thread.sleep(t);
       } catch (InterruptedException e) {
           LOGGER.error("###########################");
           LOGGER.error("#########睡眠失败#########");
           LOGGER.error("###########################");
       }
   }



}
