package com.xingyun.equipment.admin.modular.projectmanagement.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-23
 */
@TableName("t_project_area")
public class ProjectArea extends BaseEntity<ProjectArea> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer code;
	private String name;
	@TableField("parent_id")
	private Integer parentId;


	/**
	 * 删除标志
	 */
	@TableField(exist = false)
	private Integer isDel;
	/**
	 * 创建日期
	 */
	@TableField(exist = false)
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(exist = false)
	private Integer createUser;
	/**
	 * 创建人名称
	 */
	@TableField(exist = false)
	private String createUserName;
	/**
	 * 修改日期
	 */
	@TableField(exist = false)
	private Date modifyTime;
	/**
	 * 修改人
	 */
	@TableField(exist = false)
	private Integer modifyUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectArea{" +
			"id=" + id +
			", code=" + code +
			", name=" + name +
			", parentId=" + parentId +
			"}";
	}
}
