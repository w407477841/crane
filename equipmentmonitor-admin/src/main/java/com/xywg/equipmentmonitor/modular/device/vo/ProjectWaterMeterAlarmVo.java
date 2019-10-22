package com.xywg.equipmentmonitor.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeterAlarm;
import lombok.Data;

import java.util.Date;

/***
 *@author:jixiaojun
 *DATE:2018/9/28
 *TIME:16:05
 */
@Data
public class ProjectWaterMeterAlarmVo extends ProjectWaterMeterAlarm {
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
     * 警报数量
     */
    @TableField(exist = false)
    private Integer alarmDissipation;

    /**
     * 查询时间类别
     */
    @TableField(exist = false)
    private Integer checkTime;
}
