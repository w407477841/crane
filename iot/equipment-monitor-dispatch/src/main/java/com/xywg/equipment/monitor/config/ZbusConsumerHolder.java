package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.modular.handle.DispatchService;
import io.zbus.mq.Broker;
import io.zbus.mq.Consumer;
import io.zbus.mq.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 消息列表生产
 *
 * @author wangyifei
 */
public class ZbusConsumerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusConsumerHolder.class);
    private Broker broker;
    @Autowired
    private ZbusConfig zbusConfig;

    @Autowired
    private DispatchService service;

    public void init() {
        try {
            LOGGER.info("######配置消费者 开始#####");
            broker = new Broker(zbusConfig.getHost());
            ConsumerConfig config = new ConsumerConfig();
            config.setBroker(broker);
            config.setTopic(zbusConfig.getDispatchTopic());
            Consumer consumer = new Consumer(config);
            consumer.declareTopic(zbusConfig.getDispatchTopic());
            //  消费者
            consumer.start((message, mqClient) -> {
                // 转发
                service.dispatch(message.getBodyString());
            });
            LOGGER.info("######配置消费者 结束#####");



        } catch (Exception e) {
            e.printStackTrace();
            try {
                broker.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


}