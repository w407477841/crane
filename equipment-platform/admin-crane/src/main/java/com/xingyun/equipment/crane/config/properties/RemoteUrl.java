package com.xingyun.equipment.crane.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:25 2018/9/8
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "remote.url")
@Data
public class RemoteUrl {

  private String  project  ;

}
