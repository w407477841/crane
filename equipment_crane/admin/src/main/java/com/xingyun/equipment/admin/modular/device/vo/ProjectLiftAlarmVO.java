package com.xingyun.equipment.admin.modular.device.vo;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftAlarm;

import lombok.Data;

/***
 *@author:changmengyu
 *DATE:2018/8/22
 *TIME:10:39
 */@Data
public class ProjectLiftAlarmVO extends ProjectLiftAlarm {

	/**
	 * 警告类型
	 */
	  
    @TableField(exist = false)
	private Integer alarmType;

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
    private Integer status;
    /**
     * 状态数量
     */
    @TableField(exist = false)
    private Integer countStatus;

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
     *速度提醒数
     */
    @TableField(exist = false)
    private Integer alarmSpeed;

    /**
     * 前门提醒数
     */
    @TableField(exist = false)
    private Integer alarmFrontDoor;

    /**
     * 后门提醒数
     */
    @TableField(exist = false)
    private Integer alarmBackDoor;

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
     * 倾角提醒数
     */
    @TableField(exist = false)
    private Integer alarmTiltAngle;

    /**
     * 工程名称
     */
    @TableField(exist = false)
    private String projectName;
}
