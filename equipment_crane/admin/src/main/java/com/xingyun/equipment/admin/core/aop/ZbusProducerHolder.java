package com.xingyun.equipment.admin.core.aop;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectApplicationConfig;
import com.xingyun.equipment.admin.modular.remotesetting.service.IProjectApplicationConfigService;
import io.zbus.mq.Broker;
import io.zbus.mq.Message;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息列表生产
 * @author wangcw
 */
@Component
@ConfigurationProperties(prefix = "zbus")
@SuppressWarnings("all")
public class ZbusProducerHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusProducerHolder.class);
    private Broker broker;
    private Broker brokerIot;

    private Producer ychtProducer;
    private Producer ycdataProducer;
    private Producer yclxProducer;
    private Producer sjjdataProducer;
    private Producer sjjhtProducer;
    private Producer sjjlxProducer;
    private Producer tjdataProducer;
    private Producer tjhtProducer;
    private Producer tjlxProducer;
    private Producer websocketProducer;
    private String ycDataTopic ;
    private String ycHtTopic;
    private String ycLxTopic;
    private String sjjDataTopic;
    private String sjjHtTopic;
    private String sjjLxTopic;
    private String tjHtTopic;
    private String tjDataTopic;
    private String tjLxTopic;
    private String websocketTopic;
    public static Map<String,Producer>  producerMap = new ConcurrentHashMap<>(10);


    private  String address;

    private String addressIot;

    public String getWebsocketTopic() {
        return websocketTopic;
    }

    public void setWebsocketTopic(String websocketTopic) {
        this.websocketTopic = websocketTopic;
    }

    public String getYcHtTopic() {
        return ycHtTopic;
    }

    public void setYcHtTopic(String ycHtTopic) {
        this.ycHtTopic = ycHtTopic;
    }

    public String getYcLxTopic() {
        return ycLxTopic;
    }

    public void setYcLxTopic(String ycLxTopic) {
        this.ycLxTopic = ycLxTopic;
    }

    public String getSjjHtTopic() {
        return sjjHtTopic;
    }

    public void setSjjHtTopic(String sjjHtTopic) {
        this.sjjHtTopic = sjjHtTopic;
    }

    public String getSjjLxTopic() {
        return sjjLxTopic;
    }

    public void setSjjLxTopic(String sjjLxTopic) {
        this.sjjLxTopic = sjjLxTopic;
    }

    public String getTjHtTopic() {
        return tjHtTopic;
    }

    public void setTjHtTopic(String tjHtTopic) {
        this.tjHtTopic = tjHtTopic;
    }

    public String getTjLxTopic() {
        return tjLxTopic;
    }

    public void setTjLxTopic(String tjLxTopic) {
        this.tjLxTopic = tjLxTopic;
    }

    public String getYcDataTopic() {
        return ycDataTopic;
    }

    public void setYcDataTopic(String ycDataTopic) {
        this.ycDataTopic = ycDataTopic;
    }

    public String getSjjDataTopic() {
        return sjjDataTopic;
    }

    public void setSjjDataTopic(String sjjDataTopic) {
        this.sjjDataTopic = sjjDataTopic;
    }

    public String getTjDataTopic() {
        return tjDataTopic;
    }

    public void setTjDataTopic(String tjDataTopic) {
        this.tjDataTopic = tjDataTopic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressIot() {
        return addressIot;
    }

    public void setAddressIot(String addressIot) {
        this.addressIot = addressIot;
    }

    public Broker getBroker() {
        return broker;
    }

    public Broker getBrokerIot() {
        return brokerIot;
    }

    @Autowired
    private IProjectApplicationConfigService  applicationConfigService;
    @Autowired
    private IProjectInfoService  projectInfoService;

    @PostConstruct
    public void init() {
        // 全局
        broker = new Broker(address);
        // 设备平台内部
        brokerIot = new Broker(addressIot);
        Wrapper<ProjectApplicationConfig> wrapper = new EntityWrapper<>();
        List<ProjectApplicationConfig>  list =   applicationConfigService.selectList(wrapper);
        list.forEach(item->{
            Producer producer = new Producer(broker);
            try {
                producer.declareTopic(item.getTopic());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            producerMap.put(item.getTopic(),producer)  ;
        });

        // 扬尘
        ycdataProducer =  new Producer(brokerIot);
        try {
            ycdataProducer.declareTopic(ycDataTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ychtProducer = new Producer(brokerIot);
        try {
            ychtProducer.declareTopic(ycHtTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        yclxProducer = new Producer(brokerIot);
        try {
            yclxProducer.declareTopic(ycLxTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 塔吊
        tjdataProducer = new Producer(brokerIot);
        try {
            tjdataProducer.declareTopic(tjDataTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tjhtProducer = new Producer(brokerIot);
        try {
            tjhtProducer.declareTopic(tjHtTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tjlxProducer = new Producer(brokerIot);
        try {
            tjlxProducer.declareTopic(tjLxTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //升降机
        sjjdataProducer = new Producer(brokerIot);
        try {
            sjjdataProducer.declareTopic(sjjDataTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sjjhtProducer = new Producer(brokerIot);
        try {
            sjjhtProducer.declareTopic(sjjHtTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sjjlxProducer = new Producer(brokerIot);
        try {
            sjjlxProducer.declareTopic(sjjLxTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // websocket

        websocketProducer = new Producer(brokerIot);
        try {
            websocketProducer.declareTopic(websocketTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addTopic(String topic){
        Producer producer = new Producer(broker);
        try {
            producer.declareTopic(topic);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producerMap.put(topic,producer)  ;
    }

    public void removeTopic(String topic){
        Producer producer = producerMap.get(topic);
        try {
            producer.removeTopic(topic);
            producerMap.remove(topic);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param uuid    项目uuid
     * @param json  设备信息
     * @param type   类型 add/edit/delete
     * @param  device 设备  left / monitor/ crane
     * @throws Exception
     */
    public void modifyDevice(String  uuid,String json,String type,String device) throws Exception {
        Wrapper<ProjectInfo> wrapper =new EntityWrapper<>();
        wrapper.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapper);

        String key =  projectInfo.getTopic();

        String topic  = "project-"+uuid+"-"+type+"-"+device+"-";

        Producer producer = producerMap.get(key);
        Message msg = new Message();
        msg.setTopic(key);
        msg.setToken("t-10001");
        msg.setBody(topic+json);
        if(null == producer){
            LOGGER.error("uuid:{} 不存在 topic ",uuid);
        }else {
            producer.publish(msg);
        }


    }


    /**
     * 发送扬尘数据
     */
    public void sendYcDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(ycDataTopic);
        msg.setBody(data);
        ycdataProducer.publish(msg);
    }

    public void sendYcHtMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(ycHtTopic);
        msg.setBody(data);
        ychtProducer.publish(msg);
    }

    public void sendYcLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(ycLxTopic);
        msg.setBody(data);
        yclxProducer.publish(msg);
    }

    public void sendTjDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(tjDataTopic);
        msg.setBody(data);
        tjdataProducer.publish(msg);
    }

    public void sendTjHtMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(tjHtTopic);
        msg.setBody(data);
        tjhtProducer.publish(msg);
    }
    public void sendTjLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(tjLxTopic);
        msg.setBody(data);
        tjlxProducer.publish(msg);
    }
    public void sendSjjDataMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(sjjDataTopic);
        msg.setBody(data);
        sjjdataProducer.publish(msg);
    }
    public void sendSjjHtMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(sjjHtTopic);
        msg.setBody(data);
        sjjhtProducer.publish(msg);
    }
    public void sendSjjLxMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setHeader("server","admin1");
        msg.setTopic(sjjLxTopic);
        msg.setBody(data);
        sjjlxProducer.publish(msg);
    }

    public void sendWebsocketMessage(String data) throws Exception{
        Message msg=new Message();
        msg.setTopic(websocketTopic);
        msg.setBody(data);
        websocketProducer.publish(msg);
    }

}