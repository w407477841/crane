package com.xywg.equipment.monitor.config.properties;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:52 2018/10/29
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "server")
@Data
public class ServerProperties {

        private String name;


}
