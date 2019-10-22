package com.xywg.attendance.core.config;

import com.xywg.attendance.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xywg.attendance.common.global.GlobalStaticConstant.DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY;

/**
 * @author hjy
 * @date 2019/3/21
 */
@Configuration
public class InitEncryptionParameter {
    @Autowired
    private  RedisUtil redisUtil;


    /**
     * 项目重启时 清空 redis中已经存储的 公钥私钥相关信息
     */
    @Bean
    public void initParameter(){
        redisUtil.removePattern(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY+"*");
    }

}
