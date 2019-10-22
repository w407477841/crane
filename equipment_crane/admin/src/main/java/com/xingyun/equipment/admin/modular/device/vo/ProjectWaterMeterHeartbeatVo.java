package com.xingyun.equipment.admin.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.admin.modular.device.model.ProjectWaterMeterHeartbeat;
import lombok.Data;

import java.util.Date;

/***
 *@author:jixiaojun
 *DATE:2018/9/28
 *TIME:16:00
 */
@Data
public class ProjectWaterMeterHeartbeatVo extends ProjectWaterMeterHeartbeat {
    /**
     * 开始时间
     */
    @TableField(exist = false)
    private Date deviceTimeBegin;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private Date deviceTimeEnd;

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
