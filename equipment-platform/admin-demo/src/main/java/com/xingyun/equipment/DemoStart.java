package com.xingyun.equipment;

import com.xingyun.equipment.cache.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:40 2019/7/23
 * Modified By : wangyifei
 */
@SpringBootApplication()
public class DemoStart {

    public static void main(String args[]){
        SpringApplication.run(DemoStart.class,args);
    }
}

