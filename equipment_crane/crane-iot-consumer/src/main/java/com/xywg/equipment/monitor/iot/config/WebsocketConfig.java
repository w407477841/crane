package com.xywg.equipment.monitor.iot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
/**
 * websocket 主要实现   服务端主动推送
 *
 */

@Configuration
@EnableWebSocketMessageBroker//注解使用STOMP协议传输基于代理消息
@ConfigurationProperties(prefix="xywg.websocket")
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	private String []allowedOrigins;
	
	 public String[] getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(String[] allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	/** 
     * 配置了一个简单的消息代理，如果不重载，默认情况下回自动配置一个简单的内存消息代理，用来处理以"/topic"为前缀的消息。这里重载configureMessageBroker()方法， 
     * 消息代理将会处理前缀为"/topic"和"/user"的消息。 
     * @param config
     */  
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
		//表示在topic和user这两个域上可以向客户端发消息, stompClient.subscribe
    	config.enableSimpleBroker("/topic", "/user");
		//这句话表示给指定用户发送一对一的主题前缀是"/user"
    	config.setUserDestinationPrefix("/user");
		//这句话表示客户单向服务器端发送时的主题上面需要加"/app"作为前缀。 stompClient.send
    	config.setApplicationDestinationPrefixes("/app");
    }
 
    /** 
     * 将"/hello"路径注册为STOMP端点，这个路径与发送和接收消息的目的路径有所不同，这是一个端点，客户端在订阅或发布消息到目的地址前，要连接该端点， 
     * 即用户发送请求url="/applicationName/hello"与STOMP server进行连接。之后再转发到订阅url； 
     * PS：端点的作用——客户端在订阅或发布消息到目的地址前，要连接该端点。 
     * @param registry
     */  
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 建立连接时使用 如 var socket = new SockJS("http://192.168.0.166:8090/hello");
        registry.addEndpoint("/webSocketServer")
				//支持跨域
        .setAllowedOrigins("*")
				//当客户机不支持websocket时，采用轮训方式
        .withSockJS();
    }
}
