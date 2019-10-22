package com.xywg.iot.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hjy
 * @date 2018/11/14
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                //.allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
