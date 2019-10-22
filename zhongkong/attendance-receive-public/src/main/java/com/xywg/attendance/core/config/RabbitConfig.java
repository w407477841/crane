package com.xywg.attendance.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import static com.xywg.attendance.common.global.GlobalStaticConstant.RABBITMQ_TOPIC_NAME_MQ;

/**
 * @author hujingyun
 * RabbitMq  初始化队列主题  不同的topic 必须在此类初始化一个实例对象
 */
@Configuration
public class RabbitConfig {

    /**
     * 自动创建队列
     */
    @Bean
    public Queue queue() {
        return new Queue(RABBITMQ_TOPIC_NAME_MQ);
    }


}