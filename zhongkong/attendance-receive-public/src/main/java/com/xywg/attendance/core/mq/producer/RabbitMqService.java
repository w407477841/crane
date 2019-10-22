package com.xywg.attendance.core.mq.producer;

import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jingyun_hu
 * @date 2018/5/10
 */
@Component
public class RabbitMqService<T> implements RabbitTemplate.ConfirmCallback {
    private Logger logger = LoggerFactory.getLogger(RabbitMqService.class.getName());

    @Autowired
    private AmqpTemplate template;

    public void amqpSendMessage(String topic, TransmissionMessageTemplate values){
        this.template.convertAndSend(topic, values);
    }

    public void amqpSendMessage( String values){
        this.template.convertAndSend(values);
    }

    /**
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("confirm: "+correlationData.getId()+" "+ack +" "+cause);
    }
}
