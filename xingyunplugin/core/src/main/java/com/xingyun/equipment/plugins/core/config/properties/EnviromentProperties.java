package com.xingyun.equipment.plugins.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:34 2019/7/8
 * Modified By : wangyifei
 */
@Data
@ConfigurationProperties(prefix = "xingyun.enviroment")
public class EnviromentProperties {

    /**  */
    private int bossThread;
    /**  */
    private int workThread;
    /** 心跳*/
    private int heart;
    /** 端口 */
    private int  port;
    /** debug */
    private boolean debug = false;
}
