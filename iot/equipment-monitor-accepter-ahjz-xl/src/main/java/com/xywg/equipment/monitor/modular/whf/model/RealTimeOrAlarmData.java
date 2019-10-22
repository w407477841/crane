package com.xywg.equipment.monitor.modular.whf.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RealTimeOrAlarmData {
    //设备号
    private String deviceNo;
    //时间
    private Date time;
    //实时起重量
    private BigDecimal realTimeLift;
    //重量百分比
    private BigDecimal weightPercent;
    //实时倾斜度1
    private BigDecimal realTimeTilt1;
    //倾斜百分比1
    private BigDecimal tiltPervent1;
    //实时倾斜度2
    private BigDecimal realTimeTilt2;
    //倾斜百分比2
    private BigDecimal tiltPervent2;
    //报警原因
    private Integer alarmReason;
    //报警级别
    private Integer alarmClass;

    @Override
    public String toString() {
        return "RealTimeOrAlarmData{" +
                "time='" + time + '\'' +
                ", realTimeLift=" + realTimeLift +
                ", weightPercent=" + weightPercent +
                ", realTimeTilt1=" + realTimeTilt1 +
                ", tiltPervent1=" + tiltPervent1 +
                ", realTimeTilt2=" + realTimeTilt2 +
                ", tiltPervent2=" + tiltPervent2 +
                ", alarmReason=" + alarmReason +
                ", alarmClass=" + alarmClass +
                '}';
    }
}
