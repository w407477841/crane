package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.modular.dlt.handler.AmmeterHandler;
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
    private Consumer dbdataConsumer;
    private Consumer dblxConsumer;
    private Consumer dbsxConsumer;
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

            ConsumerConfig dbdataConfig  = new ConsumerConfig();
            dbdataConfig.setBroker(broker);
            dbdataConfig.setTopic(zbusConfig.getDbDataTopic());

            // 初始化 电表数据消费者
            dbdataConsumer = new Consumer(dbdataConfig);
            dbdataConsumer.declareTopic(zbusConfig.getDbDataTopic());
            try {

                dbdataConsumer.start(new MessageHandler() {
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

            ConsumerConfig dblxConfig  = new ConsumerConfig();
            dblxConfig.setBroker(broker);
            dblxConfig.setTopic(zbusConfig.getDbLxTopic());
            // 初始化  扬尘数据生产者
            dblxConsumer = new Consumer(dblxConfig);
            dblxConsumer.declareTopic(zbusConfig.getDbLxTopic());
            try {

                dblxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getHeader("server"));
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic = message.getHeader("server");
                        try {
                            ammeterDevice.offline(data,serverTopic);
                        }catch (Exception ex){
                            LOGGER.error(ex.getMessage());
                        }
                    }
                });
            }catch (Exception ex){

            }

            ConsumerConfig dbsxConfig  = new ConsumerConfig();
            dbsxConfig.setBroker(broker);
            dbsxConfig.setTopic(zbusConfig.getDbSxTopic());
            // 初始化  扬尘数据生产者
            dbsxConsumer = new Consumer(dbsxConfig);
            dbsxConsumer.declareTopic(zbusConfig.getDbSxTopic());
            try {

                dbsxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getHeader("server"));
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic = message.getHeader("server");
                        try {
                            ammeterDevice.online(data,serverTopic);
                        }catch (Exception ex){
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