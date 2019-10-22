package com.xingyun.equipment.crane.modular.device.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 22:33 2019/6/23
 * Modified By : wangyifei
 */
@Data
public class ProjectCraneStaticsticsDailyVO {

    /**
     * ID
     */
    private int id;
    /**
     * 工程id
     */
    private Integer projectId;
    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 施工单位
     */
    private Integer builder;
    /**
     * 产权单位
     */
    private String owner;
    /**
     * 备案编号
     */
    private String identifier;
    /**
     * 设备编号
     */
    private String craneNo;
    /**
     * 黑匣子编号
     */
    private String deviceNo;
    /**
     * 吊重次数
     */
    private Integer liftFrequency;
    /**
     * 40以下
     */
    private Integer percentage0;
    /**
     * 40-60
     */
    private Integer percentage40;
    /**
     * 60-80
     */
    private Integer percentage60;
    /**
     * 80-90
     */
    private Integer percentage80;
    /**
     * 90-110
     */
    private Integer percentage90;
    /**
     * 110-120
     */
    private Integer percentage110;
    /**
     * 120以上
     */
    private Integer percentage120;
    /**
     * 重量预警
     */
    private Integer weightWarn;
    /**
     * 幅度预警
     */
    private Integer rangeWarn;
    /**
     * 高度预警
     */
    private Integer limitWarn;
    /**
     * 力矩预警
     */
    private Integer momentWarn;
    /**
     * 碰撞预警
     */
    private Integer collisionWarn;
    /**
     * 重量报警
     */
    private Integer weightAlarm;
    /**
     * 幅度报警
     */
    private Integer rangeAlarm;
    /**
     * 高度报警
     */
    private Integer limitAlarm;
    /**
     * 力矩报警
     */
    private Integer momentAlarm;
    /**
     * 碰撞报警
     */
    private Integer collisionAlarm;
    /**
     * 风速报警
     */
    private Integer windSpeedAlarm;
    /**
     * 倾斜报警
     */
    private Integer tiltAlarm;
    /**
     * 日期
     */
    private Date workDate;

    /**
     * 总运行时长
     */
    private BigInteger timeSum;

    /**
     * 在线状态
     */
    private Integer isOnline;

    private String builderName;

    /**
     * 开始时间
     */
    private String startTime;
}
