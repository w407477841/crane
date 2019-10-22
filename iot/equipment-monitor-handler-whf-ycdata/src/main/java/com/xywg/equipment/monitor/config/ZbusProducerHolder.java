package com.xywg.equipment.monitor.config;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.ProducerUtil;
import com.xywg.equipment.monitor.modular.whf.dto.ControllerDTO;
import com.xywg.equipment.monitor.modular.whf.handle.EnvironmentMonitorDevice;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDetail;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.xywg.equipment.monitor.core.util.ProducerUtil.serversMap;

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
    private Consumer yclxConsumer;
    private Consumer ychtConsumer;

    @Autowired
    private ZbusConfig   zbusConfig;
    @Autowired
    private EnvironmentMonitorDevice  environmentMonitorDevice;
    @Autowired
    private XywgProerties  xywgProerties;


    public Broker getBroker() {
        return broker;
    }

    public void init(String host) {
        try {
            LOGGER.info("######配置消费者 开始#####");

            broker = new Broker(host);
            // 初始化 扬尘数据消费者
            for(int i=0;i<COMSUMER_SIZE;i++){
                startDataComsumer(i);
            }



            // 初始化  扬尘上线消费者

            ConsumerConfig ychtConfig  = new ConsumerConfig();
            ychtConfig.setBroker(broker);
            ychtConfig.setTopic(zbusConfig.getYcHtTopic());

            ychtConsumer = new Consumer(ychtConfig);
            ychtConsumer.declareTopic(zbusConfig.getYcHtTopic());
            try {

                ychtConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {
                        LOGGER.info(message.getBodyString());
                        String data = message.getBodyString();
                        String serverTopic  = message.getHeader("server");
                        try {
                            Map<String,Object> ht =  environmentMonitorDevice.insertOrigin(data);
                            ht.put("serverTopic",serverTopic);
                            environmentMonitorDevice.online(ht);
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


            // 初始化  扬尘离线消费者
            ConsumerConfig yclxConfig  = new ConsumerConfig();
            yclxConfig.setBroker(broker);
            yclxConfig.setTopic(zbusConfig.getYcLxTopic());

            yclxConsumer = new Consumer(yclxConfig);
            yclxConsumer.declareTopic(zbusConfig.getYcLxTopic());
            try {
                yclxConsumer.start(new MessageHandler() {
                    @Override
                    public void handle(Message message, MqClient mqClient) throws IOException {

                        environmentMonitorDevice. offline(message.getBodyString(),message.getHeader("server"));
                    }
                });
            }catch (Exception ex){

            }
            String [] servers = xywgProerties.getServers();
            if(servers!=null){
                for(String server : servers){
                    Producer     ctrlProducer = new Producer(broker);
                    websocketProducer.declareTopic(server);
                    serversMap.put(server,ctrlProducer);
                }
            }
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


private void startDataComsumer(int i) throws IOException, InterruptedException {

    ConsumerConfig ycdtConfig  = new ConsumerConfig();
    ycdtConfig.setBroker(broker);
    ycdtConfig.setTopic(zbusConfig.getYcDataTopic());
    // 初始化  扬尘数据生产者
    dataConsumers[i] = new Consumer(ycdtConfig);
    dataConsumers[i].declareTopic(zbusConfig.getYcDataTopic());
    try {

        dataConsumers[i].start(new MessageHandler() {
            @Override
            public void handle(Message message, MqClient mqClient) throws IOException {
                String serverTopic  = message.getHeader("server");
                LOGGER.info(serverTopic);
                LOGGER.info(message.getBodyString());
                String data = message.getBodyString();
                try {
                    ProjectEnvironmentMonitorDetail projectEnvironmentMonitorDetail =  environmentMonitorDevice.insertOriginData(data);
                    environmentMonitorDevice.insertData(projectEnvironmentMonitorDetail,serverTopic);

                }catch (Exception ex){
                    ex.printStackTrace();
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









