package com.xywg.equipment.monitor.iot.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis 配置管理类
 * @author  hh cao
 * @date 2018/4/24
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
    /**
     * 管理缓存
     * @param redisTemplate
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        return rcm;
    }

    /**
     * RedisTemplate配置
     * @param factory
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        return template;
    }
    
    /**
     * string RedisTemplate
     * @param factory
     * @return
     */
    @Bean
    public StringRedisTemplate strRedisTemplate(RedisConnectionFactory factory) {
    	StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }
}