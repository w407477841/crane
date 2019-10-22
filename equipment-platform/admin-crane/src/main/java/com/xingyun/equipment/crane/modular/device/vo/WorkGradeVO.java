package com.xingyun.equipment.crane.modular.device.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkGradeVO {
    /**
     * 工程id
     */
    private Integer projectId;
    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 设备编号
     */
    private String craneNo;
    //黑匣子编号
    private String deviceNo;
    //规格编号
    private String specification;
    //吊重次数
    private Integer liftFrequency;
    //超载次数(重量报警+力矩报警)
    private Integer overloadFrequency;
    //超载率
    private BigDecimal overloadRate;
    //月使用频率
    private BigDecimal frequencyMonth;
    private String keyWord;
    private String workDate;
}
