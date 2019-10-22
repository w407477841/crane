package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.modular.whf.handle.CraneDevice;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneDetail;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 消息列表生产
 * @author wangcw
 */
@Component
public class ZbusProducerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusProducerHolder.class);
    private static final int COMSUMER_SIZE =10;
    private Broker broker;
    private Consumer [] dataConsumers = new Consumer[COMSUMER_SIZE];
    private Producer websocketProducer;
    private Consumer tjhtConsumer;
    private Consumer tjlxConsumer;

    @Autowired
    private ZbusConfig   zbusConfig;
    @Autowired
    private CraneDevice craneDevice;
    public Broker getBroker() {
        return broker;
    }
    public void init(String host) {
        try {
            LOGGER.info("######配置消费者 开始#####");

            broker = new Broker(host);

            for(int i=0;i<COMSUMER_SIZE;i++){
                startDataComsumer(i);
            }



            ConsumerConfig tjhtConfig  = new ConsumerConfig();
            tjhtConfig.setBroker(broker);
            tjhtConfig.setTopic(zbusConfig.getTjHtTopic());
            // 初始化  扬尘数据生产者
            tjhtConsumer = new Consumer(tjhtConfig);
            tjhtConsumer.declareTopic(zbusConfig.getTjHtTopic());
            try {

                tjhtConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        try {
                            Map<String,Object> ht =  craneDevice.insertOrigin(data);
                            ht.put("serverTopic", message.getHeader("server"));
                            craneDevice.online(ht);
                        }catch (Exception ex){
                            LOGGER.error(ex.getMessage());
                        }
                    }
                });
            }catch (Exception ex){

            }
            // 初始化  扬尘心跳生产者



            LOGGER.info("######配置消费者 结束#####");

            LOGGER.info("######配置生产者 开始#####");
            websocketProducer = new Producer(broker);
            websocketProducer.declareTopic(zbusConfig.getWebsocketTopic());
            LOGGER.info("######配置生产者 结束#####");



            ConsumerConfig tjlxConfig  = new ConsumerConfig();
            tjlxConfig.setBroker(broker);
            tjlxConfig.setTopic(zbusConfig.getTjLxTopic());

            tjlxConsumer = new Consumer(tjlxConfig);
            tjlxConsumer.declareTopic(zbusConfig.getTjLxTopic());
            try {
                tjlxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        craneDevice.offline(message.getBodyString(), message.getHeader("server"));
                    }
                });
            }catch (Exception ex){
                ex.getMessage();
            }


        }catch (Exception e){
            e.printStackTrace();
            try {
                broker.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


        private void startDataComsumer(int i) throws IOException, InterruptedException {
            ConsumerConfig ychtConfig  = new ConsumerConfig();
            ychtConfig.setBroker(broker);
            ychtConfig.setTopic(zbusConfig.getTjDataTopic());
            // 初始化  扬尘数据生产者
            dataConsumers[i] = new Consumer(ychtConfig);
            dataConsumers[i].declareTopic(zbusConfig.getTjDataTopic());
    try {
        dataConsumers[i].start(new MessageHandler() {
            @Override
            public void handle(Message message, MqClient mqClient) throws IOException {
                LOGGER.info(message.getBodyString());
                String data = message.getBodyString();
                String serverTopic  = message.getHeader("server");
                try {
                    ProjectCraneDetail projectEnvironmentMonitorDetail =  craneDevice.insertOriginData(data);
                    craneDevice.insertData(projectEnvironmentMonitorDetail,serverTopic);
                }catch (Exception ex){
                    LOGGER.error(ex.getMessage());
                }
            }
        });
    }catch (Exception ex){

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