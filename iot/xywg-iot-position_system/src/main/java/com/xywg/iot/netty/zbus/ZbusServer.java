package com.xywg.iot.netty.zbus;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.config.properties.ZbusProperties;
import io.zbus.mq.Broker;
import io.zbus.mq.Message;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:25 2019/4/17
 * Modified By : wangyifei
 */
public class ZbusServer{
    private static Logger logger = LoggerFactory.getLogger(ZbusServer.class);

    private final ZbusProperties properties;

    private Broker broker;

    private  Producer producer;

    public ZbusServer(ZbusProperties properties) {
        this.properties = properties;
    }

    public void init(){
        logger.info("ZBUS 初始化");
        broker = new Broker(properties.getHost());

        producer =  new Producer(broker);
        try {
            producer.declareTopic(properties.getWebsocketTopic());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public  void send(WebsocketDTO websocketDTO){
        Message msg =  new Message();
        msg.setTopic(properties.getWebsocketTopic());
        msg.setBody(JSONUtil.toJsonStr(websocketDTO));
        try {
            producer.publish(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
