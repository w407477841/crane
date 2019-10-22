package com.xywg.equipment.monitor.config;

import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 消息列表生产
 *
 * @author wangcw
 */
@Component
public class ZbusProducerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusProducerHolder.class);
    private Broker broker;
    private Consumer ychtConsumer;
    private Producer websocketProducer;

    private Producer tjDataProducer;

    @Autowired
    private ZbusConfig zbusConfig;

    public void init(String host) {
        try {
            broker = new Broker(host);
            LOGGER.info("######配置生产者 开始#####");
            websocketProducer = new Producer(broker);
            websocketProducer.declareTopic(zbusConfig.getWebsocketTopic());

            tjDataProducer = new Producer(broker);
            tjDataProducer.declareTopic(zbusConfig.getTjTrainingTopic());
            LOGGER.info("######配置生产者 结束#####");


        } catch (Exception e) {
            e.printStackTrace();
            try {
                broker.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


    public void sendWebsocketMessage(String data) {

        Message message = new Message();
        message.setTopic(zbusConfig.getWebsocketTopic());
        message.setBody(data);
        try {
            websocketProducer.publish(message);
        } catch (Exception e) {
            LOGGER.error("#########发送失败##########");
            LOGGER.error("#########data  " + data + "##########");
        }
    }


    public void sendTjDataProducerMessage(String data) {
        Message message = new Message();
        message.setTopic(zbusConfig.getTjTrainingTopic());
        message.setBody(data);
        try {
            tjDataProducer.publish(message);
        } catch (Exception e) {
            LOGGER.error("#########发送失败##########");
            LOGGER.error("#########data  " + data + "##########");
        }
    }

}