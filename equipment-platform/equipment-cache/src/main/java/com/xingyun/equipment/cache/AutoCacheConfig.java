package com.xingyun.equipment.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:34 2019/7/24
 * Modified By : wangyifei
 */
@Configuration
@EnableCaching
public class AutoCacheConfig {

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
     * 缓存工具类
     * @param redisTemplate
     * @return
     */
    @Bean
    public RedisUtil redisUtil(RedisTemplate redisTemplate){
        RedisUtil redisUtil =new RedisUtil(redisTemplate);
        return redisUtil;
    }

    /**
     * 缓存模板
     * @param redisUtil
     * @return
     */
    @Bean
    public CacheTemplateService cacheTemplateService(RedisUtil redisUtil){
        CacheTemplateService cacheTemplateService = new CacheTemplateService(redisUtil);
        return cacheTemplateService;
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
