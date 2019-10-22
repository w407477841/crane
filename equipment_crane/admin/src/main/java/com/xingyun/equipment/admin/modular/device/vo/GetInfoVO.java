package com.xingyun.equipment.admin.modular.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class GetInfoVO {

    //项目编号
    private Integer projectId;
    //项目名称
    private String projectName;
    //黑匣子编号
    private String deviceNo;
    //设备编号
    private String craneNo;
    //施工单位
    private Integer builder;
    //重量预警
    private Integer weightWarn;
    //幅度预警
    private Integer rangeWarn;
    //高度预警
    private Integer limitWarn;
    //力矩预警
    private Integer momentWarn;
    //碰撞预警
    private Integer collisionWarn;
    //总预警次数
    private Integer warnCount;
    //重量报警(违规)
    private Integer weightAlarm;
    //幅度报警
    private Integer rangeAlarm;
    //高度报警
    private Integer limitAlarm;
    //力矩报警
    private Integer momentAlarm;
    //碰撞报警
    private Integer collisionAlarm;
    //风速报警
    private Integer windSpeedAlarm;
    //倾斜报警
    private Integer tiltAlarm;
    //总报警次数
    private Integer alarmCount;
    //查询类型
    private Integer type;
//施工单位
    private String builderName;


    /**
     * 总吊重次数
     */
    private Integer liftFrequency;

    /**
     * 总运行时长
     */
    private BigInteger timeSum;

    /**
     * 在线状态
     */
    private Integer isOnline;
    private String keyWord;


}
