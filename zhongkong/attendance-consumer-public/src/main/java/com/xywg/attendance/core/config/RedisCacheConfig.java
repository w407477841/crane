package com.xywg.attendance.core.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
    /**
     * 管理缓存
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        return RedisCacheManager.create(factory);
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * string RedisTemplate
     *
     * @param factory
     * @return
     */
    @Bean
    public StringRedisTemplate StrRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }


}