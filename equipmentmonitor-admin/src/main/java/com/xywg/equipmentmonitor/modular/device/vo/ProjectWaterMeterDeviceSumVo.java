package com.xywg.equipmentmonitor.modular.device.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;

import lombok.Data;

/***
 * 安全数据 
 *@author:changmengyu
 *DATE:2018/9/30
 *TIME:10:05
 */
@Data
public class ProjectWaterMeterDeviceSumVo extends ProjectWaterMeter {
    /**
     * 总数
     */
    private Integer all;
    /**
     * 正常
     */
    private Integer normal; 
    /**
     * 停用 
     */
    private Integer discontinuation; 
    /**
     * 异常
     */
    private Integer abnormal;
}
