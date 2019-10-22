package com.xywg.equipment.sandplay;

import com.xywg.equipment.sandplay.config.NettyConfig;
import com.xywg.equipment.sandplay.core.util.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
* @author: wangyifei
* Description:
* Date: 8:45 2018/9/26
*/
@SpringBootApplication
public class SandplayApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SandplayApplication.class);
	}

	public static void main(String[] args) {

		SpringApplication.run(SandplayApplication.class, args);
		
//		NettyConfig netty =  ApplicationContextProvider.getBean(NettyConfig.class);
//		netty.server();

	}
}
