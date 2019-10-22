package com.xywg.equipmentmonitor.modular.infromation.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-10-15
 */
@Data
@TableName("t_project_target_set_water")
public class ProjectTargetSetWater extends BaseEntity<ProjectTargetSetWater> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 制造厂家
     */
	private String manufactor;
    /**
     * 每小时耗水量
     */
	private BigDecimal dissipation;
    /**
     * 备注
     */
	private String comments;
    /**
     * 组织结构
     */
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;
    /**
     * 占用
     */
	@TableField("call_times")
	private Integer callTimes;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
