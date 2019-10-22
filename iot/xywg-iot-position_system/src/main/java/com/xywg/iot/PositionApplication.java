package com.xywg.iot;

import com.xywg.iot.common.utils.ApplicationContextProvider;
import com.xywg.iot.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hjy
 */
@SpringBootApplication
public class PositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PositionApplication.class, args);


        NettyServer netty =  ApplicationContextProvider.getBean(NettyServer.class);
        netty.startNetty();
       NettyServer.threadPoolExecutor.shutdown();
    }
}
