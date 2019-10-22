package com.xywg.equipmentmonitor.config;

import com.google.common.collect.ImmutableMap;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import java.util.Map;

/**
 * Created by wangcw on 2018/7/7.
 */
@Configuration
public class XywgProxyConfig {
    @Bean
    public Servlet baiduProxyServlet(){
        return new ProxyServlet();
    }

    @Bean
    public ServletRegistrationBean proxyServletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(baiduProxyServlet(), "/labor/*");
        Map<String, String> params = ImmutableMap.of(
                "targetUri", "http://192.168.1.124:8080/labor/",
                "log", "true");
        registrationBean.setInitParameters(params);
        return registrationBean;
    }


}
