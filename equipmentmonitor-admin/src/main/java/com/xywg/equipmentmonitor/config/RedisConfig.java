package com.xywg.equipmentmonitor.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.xywg.equipmentmonitor.config.properties.CacheProperties;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis
 * 
 * @author wyf 添加注解@EnableCaching 启用注解缓存，cacheManager方法中对不同的cacheName设置不同的过期时间。
 * 
 *         CachePut：这个注释可以确保方法被执行，同时方法的返回值也被记录到缓存中。
 *         Cacheable：当重复使用相同参数调用方法的时候，方法本身不会被调用执行，即方法本身被略过了，取而代之的是方法的结果直接从缓存中找到并返回了。
 * 
 * 
 */

@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig extends CachingConfigurerSupport {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static  final String  DEVICE_INFO_PREFIX = "device_platform:str:deviceinfo:";

	public static  final String  DEVICE_PLATFORM = "device_platform:devices:";

	private String host;

	private int port;

	private int timeout;

	private String password;
	//数据库
	private int database;

	private int maxIdle;

	private int minIdle;

	private List<String> cacheNames;

	/**
	 * 注解@Cache key生成规则
	 */
	@Bean
@Override
	public KeyGenerator keyGenerator() {
		return (Object target, Method method, Object... params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}

	/**
	 * 注解@Cache的管理器，设置过期时间的单位是秒
	 * 
	 * @Description:
	 * @param redisTemplate
	 * @return
	 */
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		Map<String, Long> expires = new HashMap<String, Long>();// 单位秒
        expires.put(CacheProperties.CACHE_VALUE_YEAR_2, 2*365*24*60*60L);
		expires.put(CacheProperties.CACHE_VALUE_DAY_2, 2*24*60*60L);
		expires.put(CacheProperties.CACHE_VALUE_HOUR_2, 2*60*60L);
		expires.put(CacheProperties.CACHE_VALUE_MIN_30, 30*60L);
		expires.put(CacheProperties.CACHE_VALUE_MIN_2, 2*60L);
		// equipment datakey
		cacheManager.setExpires(expires);
		cacheManager.setCacheNames(cacheNames);
		cacheManager.setDefaultExpiration(600); // 设置key-value超时时间 10分钟
		return cacheManager;
	}


	/**
	 * redis模板，存储关键字是字符串，值是Jdk序列化
	 * 
	 * @Description:
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		// key序列化方式;但是如果方法上有Long等非String类型的话，会报类型转换错误；
		// Long类型不可以会出现异常信息;
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);

		// JdkSerializationRedisSerializer序列化方式;
		JdkSerializationRedisSerializer jdkRedisSerializer = new JdkSerializationRedisSerializer();
		redisTemplate.setValueSerializer(jdkRedisSerializer);
		redisTemplate.setHashValueSerializer(jdkRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * redis连接的基础设置
	 * 
	 * @Description:
	 * @return
	 */
	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(host);
		factory.setPort(port);
		factory.setPassword(password);
		// 存储的库
		factory.setDatabase(database);
		// 设置连接超时时间
		factory.setTimeout(timeout);
		factory.setUsePool(true);
		factory.setPoolConfig(jedisPoolConfig());
		return factory;
	}

	/**
	 * 连接池配置
	 * 
	 * @Description:
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		// jedisPoolConfig.set ...
		return jedisPoolConfig;
	}

	/**
	 * redis数据操作异常处理 这里的处理：在日志中打印出错误信息，但是放行
	 * 保证redis服务器出现连接等问题的时候不影响程序的正常运行，使得能够出问题时不用缓存
	 * 
	 * @return
	 */
	@Bean
	@Override
	public CacheErrorHandler errorHandler() {
		CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
				logger.error("redis异常：key=[{}]", key, e);
			}

			@Override
			public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
				logger.error("redis异常：key=[{}]", key, e);
			}

			@Override
			public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
				logger.error("redis异常：key=[{}]", key, e);
			}

			@Override
			public void handleCacheClearError(RuntimeException e, Cache cache) {
				logger.error("redis异常：", e);
			}
		};
		return cacheErrorHandler;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public List<String> getCacheNames() {
		return cacheNames;
	}

	public void setCacheNames(List<String> cacheNames) {
		this.cacheNames = cacheNames;
	}

}
