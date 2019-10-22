package com.xingyun.equipment.admin.modular.alipay.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDeviceDTO {

    /**
     * 塔吊id
     */
    @TableField("crane_id")
    private Integer craneId;
    /**
     * 设备编号
     */
    @TableField("device_no")
    private String deviceNo;
    /**
     * SIM卡号
     */
    private String gprs;

    private String projectName;

    private Integer projectId;

    private String craneNo;
}
