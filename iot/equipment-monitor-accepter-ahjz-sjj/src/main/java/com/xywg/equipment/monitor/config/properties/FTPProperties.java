package com.xywg.equipment.monitor.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author : zhouyujie
 * Description
 * Date: Created in 10:52 2019/08/05
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "ftp")
public class FTPProperties {

        private String host;
        private int port;
        private String username;
        private String password;
        private String rootpath;
        private String filePath;



}
