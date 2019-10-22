package com.xywg.equipment.monitor.iot.modular.base.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
@TableName("t_project_master_protocol_config")
public class MasterProtocolConfig extends Model<MasterProtocolConfig> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 1:塔吊 2:升降机 3:扬尘
     */
	private Integer type;
    /**
     * 协议头
     */
	private String head;
	@TableField("create_time")
	private Date createTime;
	@TableField("create_user")
	private Integer createUser;
	@TableField("modify_time")
	private Date modifyTime;
	@TableField("modify_user")
	private Integer modifyUser;
	@TableField("is_del")
	private Integer isDel;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(Integer modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MasterProtocolConfig{" +
			"id=" + id +
			", type=" + type +
			", head=" + head +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			", isDel=" + isDel +
			"}";
	}
}
