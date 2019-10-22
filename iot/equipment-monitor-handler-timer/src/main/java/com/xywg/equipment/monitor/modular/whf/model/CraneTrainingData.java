package com.xywg.equipment.monitor.modular.whf.model;

import lombok.Data;


/**
 * @author hjy
 * @date 2018/12/26
 * 塔基http 请求拉取数据实体
 */
@Data
public class CraneTrainingData {
    private String devKey;
    private String devName;
    private String devType;
    private String devAddr;
    private String devTempName;
    private String devTempValue;
    private String devHumiName;
    private String devHumiValue;
    private String devStatus;
    private String devLng;
    private String devLat;
    private String tempStatus;
    private String humiStatus;
    private String devDataType1;
    private String devDataType2;
    private String devPos;
}
