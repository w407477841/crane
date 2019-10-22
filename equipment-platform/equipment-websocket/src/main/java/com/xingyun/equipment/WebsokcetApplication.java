package com.xingyun.equipment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:45 2019/7/29
 * Modified By : wangyifei
 */
@SpringBootApplication
public class WebsokcetApplication {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

public static void main(String args[]){

    SpringApplication.run(WebsokcetApplication.class,args);

}

}
