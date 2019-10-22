package com.xywg.equipment.sandplay.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:50 2018/9/25
 * Modified By : wangyifei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cmd")
public class Cmd {

    /**称重串口号*/
    private String weightSerialPort;
    /**开启称重定时任务*/
    private boolean weightTimer;

    /**  报头 */
    private String head;
    /**开启定时任务*/
    private boolean timer ;
    /**全开指令*/
    private String allOpen;
    /**全关指令*/
    private String allClose;
}
