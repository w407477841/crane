package com.xywg.iot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xywg.iot.config.properties.JedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:52 2018/12/14
 * Modified By : wangyifei
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;


    @Override
    public CacheErrorHandler errorHandler() {
        LOGGER.error("缓存异常:[{}]","redis");
        return super.errorHandler();
    }

    /**
     * 生成key的策略
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }



    /**
     * 管理缓存
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder  = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory);
        return builder.build();
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(jedisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    public JedisConnectionFactory jedisConnectionFactory(@Autowired JedisProperties jedisProperties) {
        System.out.println("jedisConnectionFactory");
        LOGGER.info("{}  {}  {}  {}",jedisProperties.getHost(),jedisProperties.getDatabase(),jedisProperties.getPassword(),jedisProperties.getPort());
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(jedisProperties.getHost());
        redisStandaloneConfiguration.setDatabase(jedisProperties.getDatabase());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(jedisProperties.getPassword()));
        redisStandaloneConfiguration.setPort(jedisProperties.getPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration); }


}
