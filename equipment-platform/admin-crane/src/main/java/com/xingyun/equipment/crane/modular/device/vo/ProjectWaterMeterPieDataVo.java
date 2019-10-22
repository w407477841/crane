package com.xingyun.equipment.crane.modular.device.vo;

import java.math.BigDecimal;

import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeter;

import lombok.Data;

/***
 *@author:changmengyu
 *DATE:2018/9/30
 *TIME:10:05
 */
@Data
public class ProjectWaterMeterPieDataVo extends ProjectWaterMeter {
    /**
     * 名称
     */
    private String name  ;
    /**
     * 数量
     */
    private BigDecimal amount  ;
}
