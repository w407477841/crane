package com.xingyun.equipment.crane.modular.device.vo;

import lombok.Data;
@Data
public class TowerIndexVO {

private String identifier;
private String projectName;
private String gprs;
private Integer timeSum;
private Integer isOnline;
private String param;
private String craneNo;
 private String deviceNo;
private String assembleDate;
private String assembledDate;
/**
 * 使用频率
 */
 private float frequencyMonth;
/**
 * 违章次数
 */
private Integer weightAlarm;
}
