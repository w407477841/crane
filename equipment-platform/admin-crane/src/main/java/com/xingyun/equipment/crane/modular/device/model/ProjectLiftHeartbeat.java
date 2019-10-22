package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.core.BaseEntity;

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
@TableName("t_project_lift_heartbeat")
public class ProjectLiftHeartbeat extends BaseEntity<ProjectLiftHeartbeat> {

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
     * 开始时间
     */
    @TableField(exist = false)
    private Date deviceTimeBegin;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private Date deviceTimeEnd;
    /**
 
    /**
     * 升降机状态
     */
    @ApiModelProperty(value = "升降机状态")
    private Integer status;
	/**
	 * 创建日期
	 */
	@TableField(value = "end_time", fill = FieldFill.INSERT)
	private Date endTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
