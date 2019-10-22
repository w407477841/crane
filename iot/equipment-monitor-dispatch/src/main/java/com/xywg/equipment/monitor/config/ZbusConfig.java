package com.xywg.equipment.monitor.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:30 2018/10/24
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "zbus")
@Data
public class ZbusConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZbusConfig.class);
    private String host;
    private String dispatchTopic;

}
