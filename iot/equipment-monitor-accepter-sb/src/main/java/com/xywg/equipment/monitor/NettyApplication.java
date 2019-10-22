package com.xywg.equipment.monitor;

import com.xywg.equipment.monitor.config.NettyConfig;
import com.xywg.equipment.monitor.config.ZbusConfig;
import com.xywg.equipment.monitor.core.util.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyApplication {

	public static void main(String[] args) {

		SpringApplication.run(NettyApplication.class, args);
		ApplicationContextProvider.getBean(ZbusConfig.class).start();
		ApplicationContextProvider.getBean(NettyConfig.class).start();

			}
}
