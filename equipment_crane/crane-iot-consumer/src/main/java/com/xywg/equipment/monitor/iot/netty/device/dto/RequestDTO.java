package com.xywg.equipment.monitor.iot.netty.device.dto;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:28 2019/7/15
 * Modified By : wangyifei
 */
@Data
public class RequestDTO {
    /** 设备号 */
    private String sn;

    /** 指令 */
    private String cmd;
    /** 数据 */
    private String data;


    public RequestDTO() {
    }

    public RequestDTO(String sn, String cmd, String data) {
        this.sn = sn;
        this.cmd = cmd;
        this.data = data;
    }
}
