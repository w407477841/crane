package com.xywg.equipment.monitor.iot.netty.device.dto;

import lombok.Data;

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


    public ResponseDTO(String sn, String cmd, Integer code, String hexMessage) {
        this.sn = sn;
        this.cmd = cmd;
        this.code = code;
        this.hexMessage = hexMessage;
    }
}
