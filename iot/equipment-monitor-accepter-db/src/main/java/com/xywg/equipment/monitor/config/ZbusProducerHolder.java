package com.xywg.equipment.monitor.config;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.modular.whf.dto.ControllerDTO;
import io.netty.channel.Channel;
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
    private Producer dbdataProducer;
    private Producer dblxProducer;
    private Producer dbsxProducer;
    private Consumer consumer;
    @Autowired
    private ZbusConfig   zbusConfig;
    @Autowired
    private ServerProperties  serverProperties;

    public void init(String host) {
        try {
            LOGGER.info("######配置生产者 开始#####");
            broker = new Broker(host);


            // 初始化 电表数据生产者
            dbdataProducer = new Producer(broker);
            dbdataProducer.declareTopic(zbusConfig.getDbDataTopic());

            //初始化 离线生产者
           dblxProducer = new Producer(broker);
           dblxProducer.declareTopic(zbusConfig.getDbLxTopic());

           //初始化 上线
            dbsxProducer = new Producer(broker);
            dbsxProducer.declareTopic(zbusConfig.getDbSxTopic());
            ConsumerConfig controllerConfig  = new ConsumerConfig();
            controllerConfig.setBroker(broker);
            controllerConfig.setTopic(serverProperties.getName());
            consumer = new Consumer(controllerConfig);
            consumer.declareTopic(serverProperties.getName());
            consumer.start(new MessageHandler() {
                @Override
                public void handle(Message message, MqClient mqClient) throws IOException {
                    String json =  message.getBodyString();
                    ControllerDTO controllerDTO = JSONUtil.toBean(json,ControllerDTO.class);
                    if("01".equals(controllerDTO.getCmd())){
                        String localKey= controllerDTO.getType()+":"+controllerDTO.getSn();
                        Channel channel =  NettyChannelManage.CHANNEL_MAP.get(localKey);
                        if(channel!=null){
                            channel.close();
                        }

                    }
                }
            });

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

    /**
     * 发送电表数据
     */
    public void sendDbDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getDbDataTopic());
        msg.setBody(data);
        dbdataProducer.publish(msg);
    }

    /**
     * 发送离线
     */
    public void sendDbLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getDbLxTopic());
        msg.setBody(data);
        dblxProducer.publish(msg);
    }

    /**
     * 发送上线
     */
    public void sendDbSxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getDbSxTopic());
        msg.setBody(data);
        dbsxProducer.publish(msg);
    }













}