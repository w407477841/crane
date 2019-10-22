package com.xywg.equipment.monitor.iot.config.rabbitmq.consumer;


import com.rabbitmq.client.Channel;
import com.xywg.equipment.monitor.iot.config.rabbitmq.model.MqError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import java.io.IOException;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:50 2019/7/15
 * Modified By : wangyifei
 */
@Slf4j
public abstract class BaseConsumer {

    public abstract void handler0(String hello, Channel channel, Message message);

    @RabbitHandler
    public void handler(String json, Channel channel, Message message) throws IOException {
        try {
            handler0(json, channel ,message);
            log.info("消息成功消费 | 数据 {}  : 元信息 {}",json,message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered())
            {

                MqError mqError  =new MqError();
                mqError.setError(e.getMessage());
                mqError.setMessage(json);
                mqError.setDeliveryTag(message.getMessageProperties().getDeliveryTag());
                mqError.setTime(new Date());
                mqError.setExchange(message.getMessageProperties().getReceivedExchange());
                mqError.setQueue(message.getMessageProperties().getConsumerQueue());
                mqError.setRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
                log.error("消息消费失败 , error[{}] ",mqError.toString());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
            else
            {
                log.info("消息失败重发 | 数据 {}  : 元信息 {} | 异常 {}",json,message,e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }

}
