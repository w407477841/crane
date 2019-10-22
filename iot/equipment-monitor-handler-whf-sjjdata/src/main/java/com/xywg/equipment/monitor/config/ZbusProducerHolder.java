package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.modular.whf.handle.LiftDevice;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDetail;
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
    private Broker broker;
    private Consumer ychtConsumer;
    private Consumer sjjhtConsumer;
    private Consumer sjjlxConsumer;
    private Consumer sjjImgConsumer;
    private Producer websocketProducer;

    @Autowired
    private ZbusConfig   zbusConfig;
    @Autowired
    private LiftDevice  liftDevice;
    public Broker getBroker() {
        return broker;
    }
    public void init(String host) {
        try {
            LOGGER.info("######配置消费者 开始#####");
            broker = new Broker(host);
            ConsumerConfig ychtConfig  = new ConsumerConfig();
            ychtConfig.setBroker(broker);
            ychtConfig.setTopic(zbusConfig.getSjjDataTopic());
            // 初始化  扬尘数据生产者
            ychtConsumer = new Consumer(ychtConfig);
            ychtConsumer.declareTopic(zbusConfig.getSjjDataTopic());
            try {
                ychtConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic  = message.getHeader("server");
                        try {
                            ProjectLiftDetail projectEnvironmentMonitorDetail =  liftDevice.insertOriginData(data);
                            liftDevice.insertData(projectEnvironmentMonitorDetail,serverTopic);
                        }catch (Exception ex){
                            ex.printStackTrace();
                            LOGGER.error(ex.getMessage());
                        }
                    }
                });
            }catch (Exception ex){

            }
            // 初始化  扬尘心跳生产者
            ConsumerConfig sjjhtConfig  = new ConsumerConfig();
            sjjhtConfig.setBroker(broker);
            sjjhtConfig.setTopic(zbusConfig.getSjjHtTopic());
            // 初始化  扬尘数据生产者
            sjjhtConsumer = new Consumer(sjjhtConfig);
            sjjhtConsumer.declareTopic(zbusConfig.getSjjHtTopic());
            sjjhtConsumer.start(new MessageHandler() {
                @Override
                public void handle(Message message, MqClient mqClient) throws IOException {
                    LOGGER.info(message.getBodyString());
                    String data = message.getBodyString();
                    try {
                        Map<String,Object> ht =  liftDevice.insertOrigin(data);
                        ht.put("serverTopic",message.getHeader("server"));
                        liftDevice.online(ht);
                    }catch (Exception ex){
                        ex.printStackTrace();
                        LOGGER.error(ex.getMessage());
                    }
                }
            });
            // 初始化  扬尘心跳生产者
            LOGGER.info("######配置消费者 结束#####");

            LOGGER.info("######配置生产者 开始#####");
            websocketProducer = new Producer(broker);
            websocketProducer.declareTopic(zbusConfig.getWebsocketTopic());
            LOGGER.info("######配置生产者 结束#####");

            // 初始化 扬尘下线消费者
            ConsumerConfig sjjlxConfig  = new ConsumerConfig();
            sjjlxConfig.setBroker(broker);
            sjjlxConfig.setTopic(zbusConfig.getSjjLxTopic());
            sjjlxConsumer = new Consumer(sjjlxConfig);
            sjjlxConsumer.declareTopic(zbusConfig.getSjjLxTopic());
            try {
                sjjlxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        liftDevice. offline(message.getBodyString(),message.getHeader("server"));
                    }
                });
            }catch (Exception ex){

            }

            // 初始化 扬尘图像消费者
            ConsumerConfig sjjImgConfig  = new ConsumerConfig();
            sjjImgConfig.setBroker(broker);
            sjjImgConfig.setTopic(zbusConfig.getSjjImgTopic());
            sjjImgConsumer = new Consumer(sjjImgConfig);
            sjjImgConsumer.declareTopic(zbusConfig.getSjjImgTopic());
            try {
                LOGGER.info("######扬尘图像消费 处理#####");
//                liftDevice. insertImg("{\"deviceNo\":\"13002101\",\"url\":\"device_platform/picture/2019/08/05/1564990860529.jpeg\",\"deviceTime\":\"1564990863500\"}");
                sjjImgConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {

                        liftDevice. insertImg(message.getBodyString());

                    }
                });
            }catch (Exception ex){

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