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
@TableName("t_project_helmet_alarm")
public class ProjectHelmetAlarm extends BaseEntity<ProjectHelmetAlarm> {

    @TableField(value = "helmet_id")
    private Integer helmetId;

    @TableField(value = "alarm_type")
    private Integer alarmType;

    /**
     *
     */
    @TableField(value = "alarm_info")
    private String alarmInfo;


    @TableField(value = "detail_id")
    private Integer detailId;

    @TableField(value = "handle_status")
    private Integer handleStatus;


    @TableField(value = "handle_name")
    private String handleName;

    public ProjectHelmetAlarm() {
    }

    public ProjectHelmetAlarm(Integer helmetId, Integer alarmType, String alarmInfo, Integer detailId, Integer handleStatus) {
        this.helmetId = helmetId;
        this.alarmType = alarmType;
        this.alarmInfo = alarmInfo;
        this.detailId = detailId;
        this.handleStatus = handleStatus;
    }
}
