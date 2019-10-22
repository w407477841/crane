package com.xywg.equipment.monitor.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangyifei
 */
@Component
@ConfigurationProperties(prefix = "xywg")
@Data
public class XywgProerties {


    private String redisHead;
    private String [] servers;
    private String nettyHost;
    private Integer nettyPort;





}
