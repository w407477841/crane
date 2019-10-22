package com.xingyun.equipment.admin.modular.device.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author xieshuaishuai
 * @Date Create in 2018/8/22 18:30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectCraneVO extends ProjectCrane{

    private String projectName;

    private String createUserName;

    /**
     * 施工单位
     */
    private String builderName;
    /**
     * 判断在线离线状态
     */
    private String statusName;

    /**
     * 吊重次数
     */
    private Integer liftFrequency;

    /**
     * 日期
     */
    private String workDate;

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

}
