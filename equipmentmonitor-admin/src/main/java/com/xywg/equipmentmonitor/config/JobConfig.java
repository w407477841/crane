package com.xywg.equipmentmonitor.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
//@Configuration
@ComponentScan(basePackages = "com.xywg.equipmentmonitor.task")
public class JobConfig {
    private Logger logger = LoggerFactory.getLogger(JobConfig.class);

    @Value("${xywg.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xywg.job.executor.appname}")
    private String appName;

    @Value("${xywg.job.executor.ip}")
    private String ip;

    @Value("${xywg.job.executor.port}")
    private int port;

    @Value("${xywg.job.accessToken}")
    private String accessToken;

    @Value("${xywg.job.executor.logpath}")
    private String logPath;

    @Value("${xywg.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> job-config init.");
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(adminAddresses);
        xxlJobExecutor.setAppName(appName);
        xxlJobExecutor.setIp(ip);
        xxlJobExecutor.setPort(port);
        xxlJobExecutor.setAccessToken(accessToken);
        xxlJobExecutor.setLogPath(logPath);
        xxlJobExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobExecutor;
    }

}