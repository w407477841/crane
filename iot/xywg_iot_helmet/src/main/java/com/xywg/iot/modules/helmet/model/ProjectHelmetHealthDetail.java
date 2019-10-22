package com.xywg.iot.modules.helmet.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.common.domain.BaseEntity;
import lombok.Data;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Data
@TableName("t_project_helmet_health_detail")
public class ProjectHelmetHealthDetail extends BaseEntity<ProjectHelmetHealthDetail> {

    @TableField(value = "helmet_id")
    private Integer helmetId;

    private String imei;


    /**
     * 心率
     */
    @TableField(value = "heart_rate")
    private Integer heartRate;
    /**
     * 血氧
     */
    @TableField(value = "blood_oxygen")
    private Integer bloodOxygen;
    /**
     * 温度
     */
    @TableField(value = "temperature")
    private Double temperature;
    /**
     * 六轴(姿态)
     */
    @TableField(value = "sixAxis")
    private String sixAxis;


    @TableField(value = "collect_time")
    private String collectTime;

}
