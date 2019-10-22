package com.xingyun.equipment.admin.modular.projectmanagement.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Data
@TableName("t_project_info")
public class ProjectInfo extends BaseEntity<ProjectInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 项目名称
     */
	private String name;
    /**
     * 开工日期
     */
	@TableField("begin_date")
	private Date beginDate;
    /**
     * 结束日期
     */
	@TableField("end_date")
	private Date endDate;
	/**
	 * 项目区域
	 */
	private Integer position;
    /**
     * 项目地址
     */
	private String address;
    /**
     * 类型（1、房建 2、市政）
     */
	private Integer type;
	/**
	 * 项目简介
	 */
	private String introduction;
	/**
	 * 施工单位
	 */
	private Integer builder;
	/**
	 * 勘察单位
	 */
	private String surveyor;
	/**
	 * 建筑面积
	 */
	@TableField("building_size")
	private Double buildingSize;
	/**
	 * 监理单位
	 */
	private String supervisor;
	/**
	 * 建设单位
	 */
	@TableField("construction_unit")
	private String constructionUnit;
	/**
	 * 项目经理
	 */
	private String manager;
	/**
	 * 联系电话
	 */
	@TableField("manager_phone")
	private String managerPhone;
	/**
	 * 定额工期
	 */
	@TableField("fix_days")
	private String fixDays;
	/**
	 * 项目经纬度
	 */
	@TableField("place_point")
	private String placePoint;
	/**
	 * 项目范围
	 */
	@TableField("project_scope")
	private String projectScope;
	/**
	 * 项目平面图
	 */
	private String ichnography;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 状态
	 */
	private Integer status;
    /**
     * 备注
     */
	private String comments;
    /**
     * 组织机构
     */
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;

	private String topic;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
