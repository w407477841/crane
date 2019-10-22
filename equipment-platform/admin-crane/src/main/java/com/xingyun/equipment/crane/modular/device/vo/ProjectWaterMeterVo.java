package com.xingyun.equipment.crane.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeter;

import lombok.Data;

/***
 *@author:jixiaojun
 *DATE:2018/9/27
 *TIME:18:05
 */
@Data
public class ProjectWaterMeterVo extends ProjectWaterMeter {
    /**
     * 工程名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 状态
     */
    @TableField(exist = false)
    private String statusName;
    
    private Double currentDegree;
    
    private String deviceTime;
    
    private Double waterQuantity;
}
