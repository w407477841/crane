package com.xywg.equipment.monitor;

import com.xywg.equipment.monitor.config.RedisInitConfig;
import com.xywg.equipment.monitor.config.ZbusConsumerHolder;
import com.xywg.equipment.monitor.netty.NettyClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DispatchApplication {


	@Bean(initMethod = "init")
	public NettyClientService nettyClientService () {
		System.out.println("netty 客户端启动");
		return new NettyClientService();
	}

	@Bean(initMethod = "init")
	public ZbusConsumerHolder zbusConsumerHolder(){
		System.out.println("zbus 启动");
		return new ZbusConsumerHolder();
	}

	@Bean(initMethod = "start")
	public RedisInitConfig redisInitConfig(){
		System.out.println("缓存初始化 启动");
		return new RedisInitConfig();
	}
	public static void main(String[] args) {

		SpringApplication.run(DispatchApplication.class, args);

	}
}
