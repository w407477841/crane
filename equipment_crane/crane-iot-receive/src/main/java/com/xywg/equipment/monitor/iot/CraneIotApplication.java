package com.xywg.equipment.monitor.iot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.zbus.kit.logging.Logger;
import io.zbus.kit.logging.LoggerFactory;
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
public class CraneIotApplication extends SpringBootServletInitializer {
    private final static Logger logger = LoggerFactory.getLogger(CraneIotApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CraneIotApplication.class);
    }


    public static void main(String[] args) {

        SpringApplication.run(CraneIotApplication.class, args);
        logger.info("\n----------------------------------------------------------\n\t" +
                "塔吊平台平台\n\t" +
                "----------------------------------------------------------");

    }


}
