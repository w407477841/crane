package com.xywg.equipment.sandplay.timer;

import com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:01 2018/10/24
 * Modified By : wangyifei
 */
@Component
public class StatusTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusTimer.class);

//    @Scheduled(cron = "0/2 * * * * ?")
    public void chechStatus(){

//          long last =  AllChannelInit.last;
//
//          long curr =  System.currentTimeMillis();
//          if(AllChannelInit.remoteHost!= null){
//              if((curr - last)/1000>35){
//                      AllChannelInit.channelMap.get(AllChannelInit.remoteHost).closeFuture();
//                      AllChannelInit.channelMap.get(AllChannelInit.remoteHost).close();
//                      AllChannelInit.channelMap.remove(AllChannelInit.remoteHost);
//                      AllChannelInit.remoteHost = null;
//                      LOGGER.info("###########沙盘被强制断开############");
//              }
//          }

    }

}
