package com.xingyun.equipment.admin.modular.device.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author : caolj
 * Description
 * Date: Created in 22:33 2019/6/23
 * Modified By : caolj
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectCraneStatisticsByDateVO {

    /**
     * ID
     */
    private Integer id;
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

    private Integer craneId;

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
     * 日期
     */
    private String workDate;

    /**
     * 天数
     */
    private Integer days;


    /**
     * 平均吊重次数
     */
    private BigDecimal liftFrequencyAvg;


    /**
     * 最大吊重次数
     */
    private Integer liftFrequencyMax;

    /**
     * 最小调用次数
     */
    private Integer liftFrequencyMin;


    public ProjectCraneStatisticsByDateVO() {
    }

    public ProjectCraneStatisticsByDateVO(String craneNo, String deviceNo, Integer liftFrequency,BigDecimal liftFrequencyAvg,Integer liftFrequencyMax,Integer liftFrequencyMin) {
        this.craneNo = craneNo;
        this.deviceNo = deviceNo;
        this.liftFrequency = liftFrequency;
        this.liftFrequencyAvg = liftFrequencyAvg;
        this.liftFrequencyMax = liftFrequencyMax;
        this.liftFrequencyMin = liftFrequencyMin;
    }
}
