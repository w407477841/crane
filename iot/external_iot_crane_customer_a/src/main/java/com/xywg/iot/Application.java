package com.xywg.iot;

import com.xywg.iot.common.utils.ApplicationContextProvider;
import com.xywg.iot.core.config.ZbusConsumerHolder;
import com.xywg.iot.modules.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


        /*ZbusConsumerHolder zbusConsumerHolder=ApplicationContextProvider.getBean(ZbusConsumerHolder.class);
        zbusConsumerHolder.init();
*/
        NettyServer netty =  ApplicationContextProvider.getBean(NettyServer.class);
        netty.startNetty();


    }
}
