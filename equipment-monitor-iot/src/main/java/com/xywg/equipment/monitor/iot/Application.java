package com.xywg.equipment.monitor.iot;

import com.xywg.equipment.monitor.iot.config.ZbusConfig;
import com.xywg.equipment.monitor.iot.config.ZbusConsumerHolder;
import com.xywg.equipment.monitor.iot.netty.NettyServer;
import io.zbus.kit.logging.Logger;
import io.zbus.kit.logging.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * SpringBoot方式启动类
 *
 * @author wangcw
 * @Date 2017/5/21 12:06
 */
@SpringBootApplication
public class Application  {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

     /*   @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }*/
    //配置 跨域相关
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.addAllowedOrigin("http://192.168.0.166:8080");//带token不能用*
        corsConfiguration.addAllowedOrigin("*");
        //POST GET PUT等
        corsConfiguration.addAllowedMethod("*");
        //头
        corsConfiguration.addAllowedHeader("*");

        //websocket需要设置
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
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
    @Bean(initMethod = "startTcpNetty")
    public NettyServer nettyServer(){
        System.out.println("netty 启动");
        return new NettyServer();
    }
//    @Bean(initMethod = "init")
//    public ZbusConsumerHolder zbusConsumerHolder(){
//        System.out.println("zbus 启动");
//        return new ZbusConsumerHolder();
//    }
   @Bean(initMethod = "start")
    public ZbusConfig zbusConfig(){
        System.out.println("zbus 生产启动");
        return new ZbusConfig();
    }

    public static void main(String[] args) {

    	 SpringApplication.run(Application.class,args);
         logger.info("\n----------------------------------------------------------\n\t" +
                "星云网格-基础框架\n\t" +
                "本地地址: \t\thttp://localhost:8090\n\t" +
                "接口地址: \thttp://localhost:8090/doc.html\n"+
                "----------------------------------------------------------");

    }
    
    
}
