package com.xywg.equipment.monitor.core.util;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import io.zbus.mq.Message;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:57 2018/11/21
 * Modified By : wangyifei
 */
@Component
public class ProducerUtil {
    public  static Map<String,Producer> serversMap = new ConcurrentHashMap<>(10);
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerUtil.class);
    @Autowired
    private ZbusProducerHolder zbusProducerHolder ;
    public void sendCtrlMessage(String server,String data)  {
         Producer producer =  serversMap.get(server);
         if(producer ==null){
             producer = new Producer(zbusProducerHolder.getBroker());
             try {
                 producer.declareTopic(server);
             } catch (IOException e1) {
                 e1.printStackTrace();
             } catch (InterruptedException e1) {
                 e1.printStackTrace();
             }
             serversMap.put(server,producer);
         }

        Message message = new Message();
        message.setTopic(server);
        message.setBody(data);
        try {
            producer.publish(message);
        } catch (Exception e) {
            LOGGER.error("#########发送失败##########");
            LOGGER.error("#########data  "+data+"##########");
        }


    }


}
