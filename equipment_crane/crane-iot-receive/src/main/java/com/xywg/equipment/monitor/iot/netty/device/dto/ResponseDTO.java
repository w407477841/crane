package com.xywg.equipment.monitor.iot.netty.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:56 2019/7/15
 * Modified By : wangyifei
 */
@Data
public class ResponseDTO {

    private String sn;
    private String cmd;
    private Integer code;
    private String hexMessage;

}
