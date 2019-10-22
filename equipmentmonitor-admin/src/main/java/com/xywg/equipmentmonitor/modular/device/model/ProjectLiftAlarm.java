package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_lift_alarm")
public class ProjectLiftAlarm extends BaseEntity<ProjectLiftAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 升降机id
     */
    @ApiModelProperty(value = "升降机id")
    @TableField("lift_id")
    private Integer liftId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @TableField("device_no")
    private String deviceNo;
    
    /**
     * 报警id
     */
    @ApiModelProperty(value = "报警id")
    @TableField("alarm_id")
    private Integer alarmId;
    /**
     * 报警类型
     */
    @ApiModelProperty(value = "报警类型")
    @TableField("alarm_info")
    private String alarmInfo;

    /**状态*/
    private Integer status;
    /**修改人*/
    @TableField("modify_user_name")
    private String modifyUserName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
