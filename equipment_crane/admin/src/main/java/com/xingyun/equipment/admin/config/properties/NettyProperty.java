package com.xingyun.equipment.admin.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = NettyProperty.NETTY_PREFIX)
public class NettyProperty {
 
	public static final String NETTY_PREFIX = "netty";


	private int port;


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}



}
