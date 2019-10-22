package com.xingyun.equipment.websocket.rabbitmq.consumer.impl;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.xingyun.equipment.system.dto.WebsocketDTO;
import com.xingyun.equipment.websocket.rabbitmq.consumer.BaseConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:55 2019/7/15
 * Modified By : wangyifei
 */
@Component
@RabbitListener(queues = {"queue.websocket"})
@Slf4j
public class MqConsumer  extends BaseConsumer {

    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public void handler0(String hello, Channel channel, Message message) {
        Long start= System.currentTimeMillis();
        WebsocketDTO websocketDTO = JSONUtil.toBean(hello,WebsocketDTO.class);
        log.info("推送websocket，[{}]",websocketDTO.toString());
        simpMessageSendingOperations.convertAndSend(websocketDTO.getTopic(),websocketDTO.getData());
        Long end= System.currentTimeMillis();
        log.info("执行用时："+String.valueOf(end-start));
    }
}
