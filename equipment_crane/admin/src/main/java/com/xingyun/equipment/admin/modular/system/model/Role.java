package com.xingyun.equipment.admin.modular.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.admin.core.model.BaseEntity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
@TableName("t_sys_role")
public class Role extends BaseEntity<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 名称
     */
	private String name;
    /**
     * 编码
     */
	private String code;
    /**
     * 描述
     */
	private String instroction;
    /**
     * 所属组织
     */
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 备注
     */
	private String comments;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInstroction() {
		return instroction;
	}

	public void setInstroction(String instroction) {
		this.instroction = instroction;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Role{" +
			"id=" + id +
			", name=" + name +
			", code=" + code +
			", instroction=" + instroction +
			", orgId=" + orgId +
			", status=" + status +
			", comments=" + comments +

			"}";
	}
}
