package com.xingyun.equipment.admin.modular.device.vo;

import lombok.Data;

@Data
public class RequestCraneFlowVO {

    //SIM卡号
    private String gprs;
    //工程id
    private Integer projectId;

    public String getGprs() {
        return gprs;
    }

    public void setGprs(String gprs) {
        this.gprs = gprs;
    }

    public Integer getProjectId() {
        return projectId;
    }

    private String keyWord;
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
