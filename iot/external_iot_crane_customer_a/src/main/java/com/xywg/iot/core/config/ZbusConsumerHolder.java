package com.xywg.iot.core.config;


import io.zbus.mq.Broker;
import io.zbus.mq.Message;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息列表生产
 */
@Component
public class ZbusConsumerHolder {
    @Value("${zbus.host}")
    private String zbusHost;
    @Value("${zbus.helmet-topic}")
    private String helmetTopic;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusConsumerHolder.class);
    private Broker broker;
    private Producer producer;


    public void init() {
        try {
            LOGGER.info("######配置生产者开始#####");
            broker = new Broker(zbusHost);
            producer = new  Producer(broker);
            producer.declareTopic(helmetTopic);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                broker.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 发送数据
     * @param message
     */
    public void sendMessage(String message){
        Message mess=new Message();
        mess.setTopic(helmetTopic);
        mess.setBody(message);
        try {
            producer.publish(mess);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}