package com.xywg.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author hjy
 */
@SpringBootApplication
public class AttendanceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AttendanceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

}
