package com.xywg.equipment.monitor.iot.netty.aop;

import com.xywg.equipment.monitor.iot.config.ZbusConfig;
import io.zbus.mq.Broker;
import io.zbus.mq.Message;
import io.zbus.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息列表生产
 * @author wangcw
 */
@Component
public class ZbusProducerHolder {

    private Broker broker;

    @Autowired
    private ZbusConfig zbusConfig;

    // 转发
    private Producer dispatchProducer;

    public void init(String host) {
        try {
            broker = new Broker(host);
            dispatchProducer = new Producer(broker);
            dispatchProducer.declareTopic(zbusConfig.getDispatchTopic());
        }catch (Exception e){
            try {
                broker.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


    public void sendDispatchMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server",zbusConfig.getServerName());
        msg.setTopic(zbusConfig.getDispatchTopic());
        msg.setBody(data);
        dispatchProducer.publish(msg);
    }



}