package com.xywg.equipment.monitor.iot.config;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.core.dto.RemoteDTO;
import com.xywg.equipment.monitor.iot.modular.helmet.handle.HelmetDataHandleService;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息列表生产
 *
 * @author wangyifei
 */
//@Component
public class ZbusConsumerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusConsumerHolder.class);
    private Broker broker;
    private Consumer consumer;
    @Autowired
    private ZbusConfig zbusConfig;

    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired
    private HelmetDataHandleService helmetDataHandleService;


    public void init() {
        try {
            LOGGER.info("######配置消费者 开始#####");
            broker = new Broker(zbusConfig.getHost());
            ConsumerConfig config = new ConsumerConfig();
            config.setBroker(broker);
            config.setTopic(zbusConfig.getWebsocketTopic());
            consumer = new Consumer(config);
            consumer.declareTopic(zbusConfig.getWebsocketTopic());
            //  消费者
            consumer.start(new MessageHandler() {
                @Override
                public void handle(Message message, MqClient mqClient) throws IOException {
                    String json = message.getBodyString();
                    RemoteDTO remoteDTO = JSONUtil.toBean(json, RemoteDTO.class);
                    try {
                        simpMessageSendingOperations.convertAndSend(remoteDTO.getTopic(), remoteDTO.getData());
//                        LOGGER.info("###########websocket转发成功###############");
//                        LOGGER.info("###########topic  " + remoteDTO.getTopic() + "###############");
//                        LOGGER.info("###########data   " + remoteDTO.getData() + "###############");
                    } catch (Exception ex) {
                        LOGGER.error("###########websocket转发异常###############");
                        LOGGER.error("###########topic  " + remoteDTO.getTopic() + "###############");
                        LOGGER.error("###########data   " + remoteDTO.getData() + "###############");
                    }

                }
            });
            LOGGER.info("######配置消费者 结束#####");

//---------------------------------------------------------------------------------------------------------------------
          /*  ConsumerConfig configHelmet = new ConsumerConfig();
            configHelmet.setBroker(broker);
            configHelmet.setTopic(zbusConfig.getHelmetTopic());
            Consumer helmetConsumer = new Consumer(configHelmet);
            //  消费者
            helmetConsumer.start((message, mqClient) -> helmetDataHandleService.handleData(message.getBodyString()));*/
//-----------------------------------------------------------------------------------------------------------------------


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