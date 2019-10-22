package com.xingyun.equipment.crane.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:03 2019/3/4
 * Modified By : wangyifei
 */
@ConfigurationProperties(prefix = "ftp")
@Configuration
@Data
public class FtpPropteries {
    /**主机地址*/
    private String host = "127.0.0.1";
    /**端口 默认21*/
    private Integer port = 21 ;

    private String username = "admin";

    private String password = "123456";
    private String rootPath = "industrial_iot";

}
