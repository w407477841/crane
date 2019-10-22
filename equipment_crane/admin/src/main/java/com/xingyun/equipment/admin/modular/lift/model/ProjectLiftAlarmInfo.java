package com.xingyun.equipment.admin.modular.lift.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectLiftAlarmInfo{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    @TableField("projectName")
    private String projectName;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @TableField("device_no")
    private String deviceNo;
    /**
     *类型
     */
    @ApiModelProperty(value = "类型")
    @TableField("info")
    private String info;
    /**
     *次数
     */
    @ApiModelProperty(value = "次数")
    @TableField("amount")
    private Integer amount;

    /**
     * 类型id
     */
    @ApiModelProperty(value = "类型id")
    @TableField("alarm_id")
    private Integer alarmId;
    
    private String userName;
    
    private String status;
    
    private Date modifyTime;
    /**
     * 升降机id
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("device_time")
    private Date deviceTime;
    
   /**
    *  升降机报警明细
    */
   @ApiModelProperty(value = "升降机报警明细")
   @TableField("infoList")
   private List<ProjectLiftAlarmInfo> infoList;
 
   /**
    * 所属部门
    */
   @TableField(value = "org_id",fill = FieldFill.INSERT)
   private Integer orgId;


  

}