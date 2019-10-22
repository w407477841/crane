package com.xywg.equipmentmonitor.modular.infromation.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.model.BaseEntity;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 电表指标设置
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_target_set_electric")
public class ProjectTargetSetElectric extends BaseEntity<ProjectTargetSetElectric> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
	@TableId(value="id", type= IdType.AUTO)
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
     * 每小时耗电量
     */
	@ApiModelProperty(value = "每小时耗电量")
	private BigDecimal dissipation;
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
     * 占用
     */
    @ApiModelProperty(value = "占用")
	@TableField("call_times")
	private Integer callTimes;
	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
