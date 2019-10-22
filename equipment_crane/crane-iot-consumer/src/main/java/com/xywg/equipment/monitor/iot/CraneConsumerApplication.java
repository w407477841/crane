package com.xywg.equipment.monitor.iot;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * SpringBoot方式启动类
 *
 * @author wangcw
 * @Date 2017/5/21 12:06
 */
@SpringBootApplication
@Slf4j
public class CraneConsumerApplication  {

    private final static Logger logger = LoggerFactory.getLogger(CraneConsumerApplication.class);



    public static void main(String[] args) {

    	 SpringApplication.run(CraneConsumerApplication.class,args);
         logger.info("\n----------------------------------------------------------\n\t" +
                "塔吊平台平台\n\t" +
                "----------------------------------------------------------");

    }
    
    
}
