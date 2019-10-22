package com.xywg.equipment.monitor.config;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.netty.Session;
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
    private Producer ychtProducer;
    private Producer ycdataProducer;
    private Producer yclxProducer;
    private Producer sjjdataProducer;
    private Producer sjjhtProducer;
    private Producer sjjlxProducer;
    private Producer tjdataProducer;
    private Producer tjhtProducer;
    private Producer tjlxProducer;
    private Producer testProducer;
    // 转发
    private Producer dispatchProducer;
    /**
     *  下发 设备数据
     */
    private Consumer consumer;

    @Autowired
    private ZbusConfig   zbusConfig;
    @Autowired
    private ServerProperties  serverProperties;

    public void init(String host) {
        try {
            LOGGER.info("######配置生产者 开始#####");
            broker = new Broker(host);


            // 初始化  扬尘数据生产者
            ycdataProducer = new Producer(broker);
            ycdataProducer.declareTopic(zbusConfig.getYcDataTopic());

            // 初始化  扬尘心跳生产者
            ychtProducer = new Producer(broker);
            ychtProducer.declareTopic(zbusConfig.getYcHtTopic());

            //初始化  扬尘离线生产者
            yclxProducer = new Producer(broker);
            yclxProducer.declareTopic(zbusConfig.getYcHtTopic());


            //初始化 升降机心跳生产者
            sjjhtProducer = new Producer(broker);
            sjjhtProducer.declareTopic(zbusConfig.getSjjHtTopic());

            //初始化 升降机数据生成者
            sjjdataProducer = new Producer(broker);
            sjjdataProducer.declareTopic(zbusConfig.getSjjDataTopic());


            //初始化  升降机离线生产者
            sjjlxProducer = new Producer(broker);
            sjjlxProducer.declareTopic(zbusConfig.getSjjLxTopic());

            // 塔基心跳生产者

            tjhtProducer = new Producer(broker);
            tjhtProducer.declareTopic(zbusConfig.getTjHtTopic());

            //塔基数据
            tjdataProducer = new Producer(broker);
            tjdataProducer.declareTopic(zbusConfig.getTjDataTopic());

            //初始化  塔基离线生产者
            tjlxProducer = new Producer(broker);
            tjlxProducer.declareTopic(zbusConfig.getTjLxTopic());

            // 初始化 测试生产者
            testProducer = new Producer(broker);
            testProducer.declareTopic(zbusConfig.getTestTopic());


            // 初始化 转发生产者
            dispatchProducer = new Producer(broker);
            dispatchProducer.declareTopic(zbusConfig.getDispatchTopic());

            LOGGER.info("######配置生产者 结束#####");

            LOGGER.info("#######配置生成者#########");
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

            LOGGER.info("#######配置生成者 结束#########");


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
     * 发送扬尘数据
     */
    public void sendYcDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getYcDataTopic());
        msg.setBody(data);
        ycdataProducer.publish(msg);
    }

    public void sendYcHtMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getYcHtTopic());
        msg.setBody(data);
        ychtProducer.publish(msg);
    }

    public void sendYcLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getYcLxTopic());
        msg.setBody(data);
        yclxProducer.publish(msg);
    }


    public void sendSjjDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setBody(data);
        msg.setTopic(zbusConfig.getSjjDataTopic());
        sjjdataProducer.publish(msg);
    }

    public void sendSjjHtMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setBody(data);
        msg.setTopic(zbusConfig.getSjjHtTopic());
        sjjhtProducer.publish(msg);
    }

    public void sendSjjLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setBody(data);
        msg.setTopic(zbusConfig.getSjjLxTopic());
        sjjlxProducer.publish(msg);
    }


    public void sendTjDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getTjDataTopic());
        msg.setBody(data);
        tjdataProducer.publish(msg);
    }

    public void sendTjHtMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getTjHtTopic());
        msg.setBody(data);
        tjhtProducer.publish(msg);
    }
    public void sendTjLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getTjLxTopic());
        msg.setBody(data);
        tjlxProducer.publish(msg);
}
    public void sendMonitorTestMessage(String sn ,String data) throws Exception {
        sendTestMessage("monitor",sn,data);
    }
    public void sendCraneTestMessage(String sn ,String data) throws Exception {
        sendTestMessage("crane",sn,data);
    }
    public void sendLiftTestMessage(String sn ,String data) throws Exception {
        sendTestMessage("lift",sn,data);
    }
    private void sendTestMessage(String type,String sn ,String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("Device-Type",type);
        msg.setHeader("Vender","daliantengyi");
        msg.setHeader("SN",sn);
        msg.setTopic(zbusConfig.getTestTopic());
        msg.setBody(data);
        testProducer.publish(msg);
    }




    public void sendDispatchMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",serverProperties.getName());
        msg.setTopic(zbusConfig.getDispatchTopic());
        msg.setBody(data);
        dispatchProducer.publish(msg);
    }




}