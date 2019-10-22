package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.core.util.ApplicationContextProvider;
import com.xywg.equipment.monitor.modular.onlinetest.enums.DeviceType;
import com.xywg.equipment.monitor.modular.onlinetest.service.OnlineTestService;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.xywg.equipment.monitor.modular.onlinetest.enums.DeviceType.getClazz;

/**
 * 消息列表生产
 * @author wangcw
 */
@Component
public class ZbusProducerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusProducerHolder.class);
    private Broker broker;
    @Autowired
    private ZbusConfig   zbusConfig;
    private Producer websocketProducer;
    public Broker getBroker() {
        return broker;
    }
    public void init(String host) {
        try {
            LOGGER.info("######配置消费者 开始#####");

            broker = new Broker(host);

            ConsumerConfig testConfig = new ConsumerConfig();
            testConfig.setBroker(broker);
            testConfig.setTopic(zbusConfig.getTestTopic());
            // 初始化  扬尘数据生产者
            Consumer testConsumer = new Consumer(testConfig);
            testConsumer.declareTopic(zbusConfig.getTestTopic());
            try {

                testConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        String type = message.getHeader("device-type") ;
                        String vender = message.getHeader("vender");
                        String sn = message.getHeader("sn");
                        String data =  message.getBodyString();
                        Class<? extends OnlineTestService> clazz =  DeviceType.getClazz(vender,type) ;
                        if(clazz!=null){
                            ApplicationContextProvider.getBean(clazz).test(vender,type,sn,data);
                            LOGGER.info(message.getBodyString());
                        }


                    }
                });
            } catch (Exception ex) {

            }

            LOGGER.info("######配置生产者 开始#####");
            websocketProducer = new Producer(broker);
            websocketProducer.declareTopic(zbusConfig.getWebsocketTopic());
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

    public void sendWebsocketMessage(String data)  {

        Message   message  =new Message();
        message.setTopic(zbusConfig.getWebsocketTopic());
        message.setBody(data);
        try {
            websocketProducer.publish(message);
        } catch (Exception e) {
            LOGGER.error("#########发送失败##########");
            LOGGER.error("#########data  "+data+"##########");
        }

    }

}