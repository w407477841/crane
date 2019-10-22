package com.xywg.equipment.monitor.iot.config.rabbitmq.consumer.impl;
import com.rabbitmq.client.Channel;
import com.xywg.equipment.monitor.iot.config.rabbitmq.consumer.BaseConsumer;
import com.xywg.equipment.monitor.iot.netty.device.crane.CraneHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:55 2019/7/15
 * Modified By : wangyifei
 */
@Component
@RabbitListener(queues = {"queue.request"})
public class MqConsumer  extends BaseConsumer {
    private Logger logger = LoggerFactory.getLogger(CraneHandlerService.class);
    @Autowired
    private CraneHandlerService  craneHandlerService;
    @Override
    public void handler0(String hello, Channel channel, Message message) {
        Long start= System.currentTimeMillis();
        //craneHandlerService.switchOperation(JSONUtil.toBean(hello, RequestDTO.class));
        Long end= System.currentTimeMillis();
        logger.info("执行用时："+String.valueOf(end-start));
    }
}
