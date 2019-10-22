package com.xingyun.equipment.system.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:14 2019/7/29
 * Modified By : wangyifei
 */
@Data
public class WebsocketDTO {
    private String topic;
    private String data;

}
