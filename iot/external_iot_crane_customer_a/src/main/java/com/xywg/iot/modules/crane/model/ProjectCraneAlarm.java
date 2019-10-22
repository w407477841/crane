package com.xywg.iot.modules.crane.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.iot.common.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HJY
 * @since 2018-12-28
 */
@TableName("t_project_crane_alarm")
@Data
public class ProjectCraneAlarm extends DeviceAlarm<ProjectCraneAlarm> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 塔吊id
     */
    @TableField("crane_id")
    private Integer craneId;

    @TableField("detail_id")
    private Integer detailId;

    @TableField("device_no")
    private String deviceNo;

    @TableField("alarm_info")
    private String alarmInfo;

    /**
     * 该字段是一个约定,用于简化数据库存储 ,99代表未定义
     */
    @TableField("alarm_id")
    private Integer alarmId;

    public ProjectCraneAlarm(Integer craneId, Integer detailId, String deviceNo, String alarmInfo, Integer alarmId) {
        this.craneId = craneId;
        this.detailId = detailId;
        this.deviceNo = deviceNo;
        this.alarmInfo = alarmInfo;
        this.alarmId = alarmId;
    }

    public ProjectCraneAlarm() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
