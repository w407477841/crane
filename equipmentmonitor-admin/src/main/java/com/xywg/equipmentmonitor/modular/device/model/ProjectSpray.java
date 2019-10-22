package com.xywg.equipmentmonitor.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_spray")
public class ProjectSpray extends BaseEntity<ProjectSpray>{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 工程名称
     */
    @ApiModelProperty(value = "工程名称")
    @TableField("project_id")
    private Integer projectId;
    /**
     * 产权编号
     */
    @ApiModelProperty(value = "产权编号")
    private String identifier;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String specification;
    /**
     * 产权单位
     */
    @ApiModelProperty(value = "产权单位")
    private String owner;
    /**
     * 制造厂家
     */
    @ApiModelProperty(value = "制造厂家")
    private String manufactor;

    /**
     * 在线状态
     */
    @ApiModelProperty(value = "在线状态")
    @TableField("is_online")
    private Integer isOnline;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 位置
     */
    @ApiModelProperty(value = "位置")
    @TableField("place_point")
    private String placePoint;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comments;
    /**
     * 组织结构
     */
    @ApiModelProperty(value = "组织结构")
    @TableField(value = "org_id",fill = FieldFill.INSERT)
    private Integer orgId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @TableField("device_no")
    private String deviceNo;
    /**
     * GPRS
     */
    @ApiModelProperty(value = "GPRS")
    private Integer gprs;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name ;
    /**
     * PM10
     */
    @ApiModelProperty(value = "PM10")
    private String pm10 ;
    /**
     * PM2.5
     */
    @ApiModelProperty(value = "PM2.5")
    private String pm25 ;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
