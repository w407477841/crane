package com.xywg.iot.config.properties;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:31 2019/4/17
 * Modified By : wangyifei
 */
@ConfigurationProperties("zbus")
@Component
@Data
public class ZbusProperties {

    private String host;
    private String websocketTopic;


}
