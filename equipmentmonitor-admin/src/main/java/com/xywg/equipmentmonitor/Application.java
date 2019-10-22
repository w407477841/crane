package com.xywg.equipmentmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.xywg.equipmentmonitor.config.properties.JwtProperties;
import com.xywg.equipmentmonitor.config.properties.NettyProperty;
import com.xywg.equipmentmonitor.config.properties.XywgProperties;

/**
 * SpringBoot方式启动类
 *
 * @author wangcw
 * @Date 2017/5/21 12:06
 */
@SuppressWarnings("all")
@SpringBootApplication
@EnableConfigurationProperties(value={NettyProperty.class,JwtProperties.class,XywgProperties.class})
//public class Application extends SpringBootServletInitializer {
    public class Application{
    /**
     * 配置 跨域相关
     * @return
     */
	  private CorsConfiguration buildConfig() {  
        CorsConfiguration corsConfiguration = new CorsConfiguration();  
        //coresConfiguration.addAllowedOrigin("http://192.168.0.166:8080");//带token不能用*
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");  
        corsConfiguration.addAllowedMethod("*");
        //webocket需要设置
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }  
      
    /** 
     * 跨域过滤器 
     * @return
     */  
    @Bean  
    public CorsFilter corsFilter() {  
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);  
    }
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Application.class);
//    }
	
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
    	 SpringApplication app=    new SpringApplication(Application.class);
    	 app.run(args);
        logger.info("\n----------------------------------------------------------\n\t" +
                "星云网格-基础框架\n\t" +
                "本地地址: \t\thttp://localhost:9092\n\t" +
                "接口地址: \thttp://localhost:9092/doc.html\n"+
                "----------------------------------------------------------");
        

    }
    
    
}
