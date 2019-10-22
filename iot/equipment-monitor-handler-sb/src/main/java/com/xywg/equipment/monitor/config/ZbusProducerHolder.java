package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.modular.sb.handler.AmmeterHandler;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息列表生产
 * @author wangcw
 */
@Component
public class ZbusProducerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusProducerHolder.class);
    private Broker broker;
    private Consumer sbdataConsumer;
    private Consumer sblxConsumer;
    private Consumer sbsxConsumer;
    private Producer websocketProducer;

    @Autowired
    private ZbusConfig   zbusConfig;
    @Autowired
    private AmmeterHandler ammeterDevice;

    public Broker getBroker() {
        return broker;
    }

    public void init(String host) {
        try {
            LOGGER.info("######配置消费者 开始#####");

            broker = new Broker(host);

            ConsumerConfig sbdataConfig  = new ConsumerConfig();
            sbdataConfig.setBroker(broker);
            sbdataConfig.setTopic(zbusConfig.getSbDataTopic());

            // 初始化 电表数据消费者
            sbdataConsumer = new Consumer(sbdataConfig);
            sbdataConsumer.declareTopic(zbusConfig.getSbDataTopic());
            try {

                sbdataConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getHeader("server"));
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic = message.getHeader("server");
                        try {
                            ammeterDevice.insertOri(data);
                            ammeterDevice.insertdata(data,serverTopic);
                        }catch (Exception ex){
                            LOGGER.error(ex.getMessage());
                        }
                    }
                });
            }catch (Exception ex){

            }

            // 初始化  电表离线消费者

            ConsumerConfig sblxConfig  = new ConsumerConfig();
            sblxConfig.setBroker(broker);
            sblxConfig.setTopic(zbusConfig.getSbLxTopic());
            // 初始化  扬尘数据生产者
            sblxConsumer = new Consumer(sblxConfig);
            sblxConsumer.declareTopic(zbusConfig.getSbLxTopic());
            try {

                sblxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getHeader("server"));
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic = message.getHeader("server");
                        try {
                            ammeterDevice.offline(data,serverTopic);
                        }catch (Exception ex){
                            ex.printStackTrace();
                            LOGGER.error(ex.getMessage());
                        }
                    }
                });
            }catch (Exception ex){

            }

            ConsumerConfig dbsxConfig  = new ConsumerConfig();
            dbsxConfig.setBroker(broker);
            dbsxConfig.setTopic(zbusConfig.getSbSxTopic());
            // 初始化  扬尘数据生产者
            sbsxConsumer = new Consumer(dbsxConfig);
            sbsxConsumer.declareTopic(zbusConfig.getSbSxTopic());
            try {

                sbsxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getHeader("server"));
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic = message.getHeader("server");
                        try {
                            ammeterDevice.online(data,serverTopic);
                        }catch (Exception ex){
                            ex.printStackTrace();
                            LOGGER.error(ex.getMessage());
                        }
                    }
                });
            }catch (Exception ex){

            }



            LOGGER.info("######配置消费者 结束#####");

            LOGGER.info("######配置生产者 开始#####");
            websocketProducer = new Producer(broker);
            websocketProducer.declareTopic(zbusConfig.getWebsocketTopic());
            LOGGER.info("######配置生产者 结束#####");






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