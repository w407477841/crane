package com.xywg.equipment.monitor.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "xywg")
@Data
public class XywgProerties {

    private String redisYcDispatchPrefix;//扬尘
    private String redisSjjDispatchPrefix;//升降机
    private String redisTdDispatchPrefix;//塔吊
}
