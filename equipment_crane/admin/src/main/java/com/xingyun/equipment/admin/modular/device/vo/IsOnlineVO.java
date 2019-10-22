package com.xingyun.equipment.admin.modular.device.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IsOnlineVO {
    private String workDate;
    private Integer onlineNumber;
    private Integer count;
    private BigDecimal onlineRate;
    private String projectName;
    private Integer projectId;
    private Integer type;
}
