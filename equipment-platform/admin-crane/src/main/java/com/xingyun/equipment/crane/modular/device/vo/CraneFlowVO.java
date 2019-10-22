package com.xingyun.equipment.crane.modular.device.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CraneFlowVO {
    //设备编号
    private String craneNo;
    //黑匣子编号
    private String deviceNo;
    //SIM卡号
    private String gprs;
    //工程id
    private Integer projectId;
    //工程名称
    private String projectName;
    //卡状态
    private String status;
    //累计用量
    private BigDecimal cumulativeFlow;
    //流量上限
    private String toplimitFlow;


    @Override
    public String toString() {
        return "CraneFlowVO{" +
                "craneNo='" + craneNo + '\'' +
                ", deviceNo='" + deviceNo + '\'' +
                ", gprs='" + gprs + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", status='" + status + '\'' +
                ", cumulativeFlow='" + cumulativeFlow + '\'' +
                ", toplimitFlow='" + toplimitFlow + '\'' +
                '}';
    }
}
