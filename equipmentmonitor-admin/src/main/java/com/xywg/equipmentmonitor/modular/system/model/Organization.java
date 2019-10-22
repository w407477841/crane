package com.xywg.equipmentmonitor.modular.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
@TableName("t_sys_organization")
public class Organization extends BaseEntity<Organization> {

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
     * 组织等级
     */
	private Integer level;
    /**
     * PID
     */
	@TableField("parent_id")
	private Integer parentId;
    /**
     * 所属集团
     */
	@TableField("group_id")
	private Integer groupId;
    /**
     * 企业描述
     */
	private String introduction;
    /**
     * 来源标记
     */
	private Integer flag;
    /**
     * 关联组织id
     */
	@TableField("relation_org_id")
	private Integer relationOrgId;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getRelationOrgId() {
		return relationOrgId;
	}

	public void setRelationOrgId(Integer relationOrgId) {
		this.relationOrgId = relationOrgId;
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
		return "Organization{" +
			"id=" + id +
			", name=" + name +
			", level=" + level +
			", parentId=" + parentId +
			", groupId=" + groupId +
			", introduction=" + introduction +
			", flag=" + flag +
			", relationOrgId=" + relationOrgId +
			", status=" + status +
			", comments=" + comments +
		
			"}";
	}
}
