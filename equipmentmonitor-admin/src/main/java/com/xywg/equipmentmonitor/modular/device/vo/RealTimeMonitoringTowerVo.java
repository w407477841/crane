package com.xywg.equipmentmonitor.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/8/22
 *TIME:10:39
 */@Data
public class RealTimeMonitoringTowerVo extends BaseEntity<RealTimeMonitoringTowerVo> {
    /**
     * 主键id
     */
    @TableField(exist = false)
    private Integer id;

    /**
     * 设备编号
     */
    @TableField(exist = false)
    private String deviceNo;

    /**
     * 项目id
     */
    @TableField(exist = false)
    private Integer projectId;

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
     * 持续时间
     */
    @TableField(exist = false)
    private String duration;

    /**
     * 监控状态
     */
    @TableField(exist = false)
    private String status;

    /**
     * 运行时间
     */
    @TableField(exist = false)
    private Date deviceTime;

    /**
     * 幅度
     */
    @TableField(exist = false)
    private Double range;

    /**
     * 高度
     */
    @TableField(exist = false)
    private Double height;

    /**
     * 重量
     */
    @TableField(exist = false)
    private Double weight;

    /**
     * 力矩百分比
     */
    @TableField(exist = false)
    private Double momentPercentage;

    /**
     * 司机
     */
    @TableField(exist = false)
    private Integer driver;

    /**
     * 当前范围实际运行时间
     */
    @TableField(exist = false)
    private String durationActual;

    /**
     * 总提醒数
     */
    @TableField(exist = false)
    private Integer alarmAll;

    /**
     * 重量提醒数
     */
    @TableField(exist = false)
    private Integer alarmWeight;
    /**
     * 高度提醒数
     */
    @TableField(exist = false)
    private Integer alarmHeight;


    /**
     * 幅度提醒数
     */
    @TableField(exist = false)
    private Integer alarmRange;

    /**
     * 力矩提醒数
     */
    @TableField(exist = false)
    private Integer alarmMoment;

    /**
     * 碰撞提醒数
     */
    @TableField(exist = false)
    private Integer alarmCollision;

    /**
     * 提醒时间
     */
    @TableField(exist = false)
    private Date alarmTime;

    /**
     * 提醒内容
     */
    @TableField(exist = false)
    private String alarmInfo;

    /**
     * 短信模板
     */
    @TableField(exist = false)
    private Integer model;

    /**
     * 标题
     */
    @TableField(exist = false)
    private String title;

    /**
     * 内容
     */
    @TableField(exist = false)
    private String content;

    /**
     * 指定人
     */
    @TableField(exist = false)
    private String relatedUser;

    /**
     * 发收时间
     */
    @TableField(exist = false)
    private Date sendTime;

    /**
     * 风俗提醒数
     */
    @TableField(exist = false)
    private Integer alarmSpeed;

    /**
     * 倾角提醒数
     */
    @TableField(exist = false)
    private Integer alarmTiltAngle;

    /**
     * 工程名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 部门id
     */
    @TableField(exist = false)
    private Integer orgId;

    /**
     * 类型
     */
    @TableField(exist = false)
    private String type;

    /**
     * 产权编号
     */
    @TableField(exist = false)
    private String identifier;

    /**
     * 生产厂家
     */
    @TableField(exist = false)
    private String manufactor;

    /**
     * 产权单位
     */
    @TableField(exist = false)
    private String owner;

    /**
     * 回转角度
     */
    @TableField(exist = false)
    private Double rotaryAngle;

    /**
     * 力矩
     */
    @TableField(exist = false)
    private Double moment;

    /**
     * 数量
     */
    @TableField(exist = false)
    private Integer amount;

    /**
     * 设备平台项目跟智慧工地项目关联的uuid
     */
    @TableField(exist = false)
    private String uuid;

    /**
     * 信息详情列表
     */
    @TableField(exist = false)
    private List<RealTimeMonitoringTowerVo> infoList;

    /**
     * 报警状态id
     */
    @TableField(exist = false)
    private Integer alarmId;

    /**
     * 报警信息
     */
    @TableField(exist = false)
    private String info;

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

    /**
     * 力矩百分比查询类别
     */
    @TableField(exist = false)
    private Integer percentage;

    /**
     * 处理人姓名
     */
    private String userName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
