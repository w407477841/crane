package com.xywg.equipmentmonitor.modular.infromation.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xywg.equipmentmonitor.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xss
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_target_set_lift")
public class ProjectTargetSetLift extends BaseEntity<ProjectTargetSetLift> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String specification;
    /**
     * 制造厂家
     */
    @ApiModelProperty(value = "制造厂家")
    private String manufactor;
    /**
     * 最大载重量
     */
    @ApiModelProperty(value = "最大载重量")
    @TableField("max_weight")
    private BigDecimal maxWeight;
    /**
     * 标准高度
     */
    @ApiModelProperty(value = "标准高度")
    @TableField("standard_height")
    private BigDecimal standardHeight;
    /**
     * 最大载人数
     */
    @ApiModelProperty(value = "最大载人数")
    @TableField("max_people")
    private BigDecimal maxPeople;
    /**
     * 运行速度
     */
    @ApiModelProperty(value = "运行速度")
    private BigDecimal speed;
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
     * 占用处理
     */
    @ApiModelProperty(value = "占用处理")
    @TableField("call_times")
    private Integer callTimes;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
