package com.xingyun.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:39 2019/6/22
 * Modified By : wangyifei
 */
//@SpringBootApplication
@Slf4j
public class TomcatAdminApploication  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TomcatAdminApploication.class);
    }

    public static void main(String[] args) {
        SpringApplication app=    new SpringApplication(TomcatAdminApploication.class);
        app.run(args);
        log.info("\n----------------------------------------------------------\n\t" +
                "星云网格-基础框架\n\t" +
                "本地地址: \t\thttp://localhost:9092\n\t" +
                "接口地址: \thttp://localhost:9092/doc.html\n"+
                "----------------------------------------------------------");
    }

}
