package com.xywg.equipment.sandplay.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:32 2018/9/25
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "cmd.crane")
@Data
public class CmdCrane  extends  BaseCmd{

}
