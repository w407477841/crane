package com.xingyun.equipment.websocket.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:23 2019/4/29
 * Modified By : wangyifei
 */

@Slf4j
public class ConfirmCallbackHandler implements RabbitTemplate.ConfirmCallback {


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消费成功");
        } else {
            System.out.println("消费失败:" + cause+"\n重新发送");
        }
    }
}
