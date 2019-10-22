package com.xywg.iot.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 */
@SuppressWarnings("all")
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
	
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setSec(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

    public Set<String> keys(final String pattern){
        Set<String> keys = redisTemplate.keys(pattern);
        return keys;
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value, Long expireTime){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
        redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 列表添加
     * @param key
     * @param value
     * @param  expireTime
     */
    public void lPush(String key,Object value, Long expireTime){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(key,value);
        redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 列表获取
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lRange(String key, long start, long end){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(key,start,end);
    }

    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void add(String key,Object value, Long expireTime){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
        redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure, Long expireTime){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
        redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }


    public   boolean  existsAndSet(String key,String stationId,double length,String sn){
        if(this.keys(key+":*").size()<=0){
         //   System.out.println("不存在"+key+":*");
            this.setSec(key+":"+stationId,length+","+sn,30L);
           // System.out.println("新增缓存"+key+":"+stationId);
            return false ;
        }
        return  true ;
    }


}
