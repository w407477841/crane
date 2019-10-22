package com.xywg.equipmentmonitor.modular.device.vo;

import lombok.Data;

@Data
public class SpraysStatusVO {
    private String deviceNo;
    private Integer status;
    private Integer isOnline;
}
