package com.xywg.equipment.monitor.iot.modular.ammeter.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@TableName("t_project_electric_power_original")
public class ProjectElectricPowerOriginal extends Model<ProjectElectricPowerOriginal> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 发送数据
     */
	@TableField("send_data")
	private String sendData;
    /**
     * 接收数据
     */
	@TableField("receive_data")
	private String receiveData;
    /**
     * 时间
     */
	@TableField("device_time")
	private Date deviceTime;
    /**
     * 备注
     */
	private String comments;
    /**
     * 删除标志
     */
	@TableField("is_del")
	private Integer isDel;
    /**
     * 创建日期
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	private Integer createUser;
    /**
     * 修改日期
     */
	@TableField("modify_time")
	private Date modifyTime;
    /**
     * 修改人
     */
	@TableField("modify_user")
	private Integer modifyUser;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSendData() {
		return sendData;
	}

	public void setSendData(String sendData) {
		this.sendData = sendData;
	}

	public String getReceiveData() {
		return receiveData;
	}

	public void setReceiveData(String receiveData) {
		this.receiveData = receiveData;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectElectricPowerOriginal{" +
			"id=" + id +
			", sendData=" + sendData +
			", receiveData=" + receiveData +
			", deviceTime=" + deviceTime +
			", comments=" + comments +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			"}";
	}
}
