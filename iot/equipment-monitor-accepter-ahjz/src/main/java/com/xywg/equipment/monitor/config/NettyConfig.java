package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.core.netty.TimeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description  netty配置类
 * Date: Created in 16:08 2018/10/15
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
@Autowired
TimeServer timeServer ;

    private int port ;

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyConfig.class);

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public void start(){


        try {
            timeServer.bind(port);
        } catch (Exception e) {
            LOGGER.error("#######################");
            LOGGER.error("###["+port+"]绑定失败########");
            LOGGER.error("#######################");
        }

    }


}
