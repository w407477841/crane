package com.xingyun.equipment.crane.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeterDetail;
import lombok.Data;

import java.util.Date;

/***
 *@author:jixiaojun
 *DATE:2018/9/29
 *TIME:9:12
 */
@Data
public class ProjectWaterMeterDetailVo extends ProjectWaterMeterDetail {
    /**
     * 开始时间
     */
    @TableField(exist = false)
    private Date beginDate;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private Date endDate;

    /**
     * 查询时间类别
     */
    @TableField(exist = false)
    private Integer checkTime;
}
