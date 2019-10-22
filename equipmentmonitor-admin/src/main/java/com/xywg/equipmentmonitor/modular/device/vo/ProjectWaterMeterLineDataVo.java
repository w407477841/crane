package com.xywg.equipmentmonitor.modular.device.vo;

import java.math.BigDecimal;
import java.util.List;

import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;

import lombok.Data;

/***
 * 曲线图数据  
 *@author:changmengyu
 *DATE:2018/9/30
 *TIME:10:05
 */
@Data
public class ProjectWaterMeterLineDataVo extends ProjectWaterMeter {
    /**
     * 名称
     */
    private String name  ;
    /**
     * 数量
     */
    private BigDecimal amount  ;
}
