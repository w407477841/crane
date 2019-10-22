package com.xingyun.equipment.admin.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt相关配置
 *
 * @author wangyifei
 * @date 2017-08-23 9:23
 */
@Configuration
@ConfigurationProperties(prefix = JwtProperties.JWT_PREFIX)
@Data
public class JwtProperties {
 
    public static final String JWT_PREFIX = "jwt";

    private String header ;

    private String secret ;

     private Long expiration ;
    private String authPath ;

    private String md5Key ;



}
