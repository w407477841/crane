package com.xingyun.equipment.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:36 2019/6/19
 * Modified By : wangyifei
 */
@SpringBootApplication
@EnableScheduling
public class CraneTimerApplication /*extends SpringBootServletInitializer */{

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(CraneTimerApplication.class);
//    }

    public static void  main(String args[]){
        SpringApplication.run(CraneTimerApplication.class,args);

    }

}
