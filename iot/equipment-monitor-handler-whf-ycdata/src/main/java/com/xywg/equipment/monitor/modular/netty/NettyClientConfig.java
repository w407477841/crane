package com.xywg.equipment.monitor.modular.netty;

import com.xywg.equipment.monitor.config.properties.XywgProerties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 严鹏
 * @date 2019/7/29
 */
//@Configuration
public class NettyClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientConfig.class);

    @Autowired
    private XywgProerties xywgProerties;

    public static NettyClient client;

//    @Bean
    public Integer run(){
        client = new NettyClient(xywgProerties.getNettyHost(), xywgProerties.getNettyPort());
        try {
            new Thread(() -> {
                try {
                    client.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            LOGGER.info("netty客户端启动成功");
            return 1;
        } catch (Exception e) {
            LOGGER.error("netty客户端启动失败");
            e.printStackTrace();
            return 0;
        }
    }

}
