package com.xywg.attendance.core.listener;

import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import com.xywg.attendance.modular.handler.MethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.xywg.attendance.common.global.GlobalStaticConstant.RABBITMQ_TOPIC_NAME_MQ;

/**
 * @author jingyun_hu
 * @date 2018/5/10
 * Rabbit 消费者监听,消息是均匀分发,不会造成重复
 */
@Component
public class RabbitListenerHandle {
    @Autowired
    private MethodService methodService;

    private Logger logger = LoggerFactory.getLogger(RabbitListenerHandle.class.getName());

    /**
     * RabbitMQ  监听 dataMessage  队列信息
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue( RABBITMQ_TOPIC_NAME_MQ))
    public void getMessage1(TransmissionMessageTemplate message) {
        //字节码转化为对象
        try {
            methodService.runMethod(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * RabbitMQ  监听 dataMessage  队列信息
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue( RABBITMQ_TOPIC_NAME_MQ))
    public void getMessage2(TransmissionMessageTemplate message) {
        try {
            methodService.runMethod(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * RabbitMQ  监听 dataMessage  队列信息
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue( RABBITMQ_TOPIC_NAME_MQ))
    public void getMessage3(TransmissionMessageTemplate message) {
        try {
            methodService.runMethod(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * RabbitMQ  监听 dataMessage  队列信息
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue( RABBITMQ_TOPIC_NAME_MQ))
    public void getMessage4(TransmissionMessageTemplate message) {
        try {
            methodService.runMethod(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * RabbitMQ  监听 dataMessage  队列信息
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue( RABBITMQ_TOPIC_NAME_MQ))
    public void getMessage5(TransmissionMessageTemplate message) {
        try {
            methodService.runMethod(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
