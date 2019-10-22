package com.xingyun.equipment.admin.modular.device.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceOfflineVO {
    //工程编号
    private Integer projectId;
    //工程名称
    private String projectName;
    //塔吊编号
    private String craneNo;
    //黑匣子编号
    private String deviceNo;
    //离线时间
    private String timeOff;
    //离线时长
    private String durationOff;
    //塔吊编号/黑匣子编号
    private String keyWord;


}
