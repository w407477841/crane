package com.xingyun.equipment.simpleequipment.receive;

import com.xingyun.equipment.simpleequipment.core.properties.XywgProerties;
import com.xingyun.equipment.simpleequipment.receive.handler.MonitorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:36 2019/7/30
 * Modified By : wangyifei
 */
@SpringBootApplication
@EnableConfigurationProperties(value={XywgProerties.class})
@Import(value=MonitorHandler.class)
public class ReceiveApplication {
    @Bean(initMethod = "init")
    NettyServer nettyServer(XywgProerties proerties,MonitorHandler monitorHandler){
        return new NettyServer(proerties,monitorHandler);
    }

    public static void main(String[] args){
        SpringApplication.run(ReceiveApplication.class,args);
    }

}
