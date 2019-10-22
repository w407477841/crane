package com.xingyun.crane.cache;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:44 2019/6/26
 * Modified By : wangyifei
 */
@Slf4j
public class CacheTemplateService {

    private final RedisUtil redisUtil;

    public CacheTemplateService(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public <T> T findData(String key , long second, Class<T> clazz, CacheLoader<T> cacheLoader){

        String json = (String) redisUtil.get(key);
        if(StrUtil.isBlankOrUndefined(json)){
            synchronized (this){
                json = (String) redisUtil.get(key);
                if(StrUtil.isBlankOrUndefined(json)){
                    //还是没有，就从数据库加载，并缓存
                    T t = cacheLoader.load();
                    if(t==null){
                        redisUtil.set(key,null,second);
                    }else{
                        redisUtil.set(key,JSONUtil.toJsonStr(t),second);
                    }

                    log.info("缓存<{}>不存在",key);
                    return   t;
                }
            }
        }
        log.info("缓存<{}>存在",key);
        return JSONUtil.toBean(json,clazz);
    }

}
