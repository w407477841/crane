package com.xywg.equipment.monitor.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String host ;
    private String ycDataTopic ;
    private String ycHtTopic;
    private String sjjDataTopic;
    private String sjjHtTopic;
    private String tjHtTopic;
    private String tjDataTopic;
    private String sjjLxTopic;
    private String sjjImgTopic;
    private String websocketTopic;




    @Autowired
    private ZbusProducerHolder zbusProducerHolder;


    public void start (){
        LOGGER.info("######zbus初始化开始#######");
        zbusProducerHolder.init(host);
        LOGGER.info("######zbus初始化完毕#######");
    }

}
