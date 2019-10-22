package com.xywg.equipment.monitor.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:40 2019/6/22
 * Modified By : wangyifei
 */
@Configuration
public class WebConfig {

    /**
     *
     * @return
     */
    @SuppressWarnings("all")
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
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
