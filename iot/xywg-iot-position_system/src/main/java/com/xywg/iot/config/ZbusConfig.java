package com.xywg.iot.config;

import com.xywg.iot.config.properties.ZbusProperties;
import com.xywg.iot.netty.zbus.ZbusServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:24 2019/4/17
 * Modified By : wangyifei
 */
@Configuration
public class ZbusConfig {

    @Bean(initMethod = "init")
    ZbusServer zbusServer(ZbusProperties zbusProperties){
        return  new ZbusServer(zbusProperties) ;
    }

}
