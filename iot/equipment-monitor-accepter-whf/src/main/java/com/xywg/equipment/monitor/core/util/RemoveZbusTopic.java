package com.xywg.equipment.monitor.core.util;

import io.zbus.mq.Broker;
import io.zbus.mq.BrokerConfig;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:50 2018/12/3
 * Modified By : wangyifei
 */
public class RemoveZbusTopic {

    public static final String TOPICS [] =  {
            "accepter-whf1",
            "topic-db-data",
            "topic-db-lx",
            "topic-db-sx",
            "topic-sb-data",
            "topic-sb-lx",
            "topic-sb-sx",
            "topic-sjj-data",
            "topic-sjj-ht",
            "topic-sjj-lx",
            "topic-tj-data",
            "topic-tj-ht",
            "topic-tj-lx",
            "topic-websocket",
            "topic-yc-data",
            "topic-yc-ht",
            "topic-yc-lx"


    };

    public static void main(String args[]){
        Broker broker =new Broker("192.168.1.186:15555");

        Producer producer =new Producer(broker);
        for(String topic:TOPICS){
            try {
                producer.removeTopic(topic);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




    }
}
