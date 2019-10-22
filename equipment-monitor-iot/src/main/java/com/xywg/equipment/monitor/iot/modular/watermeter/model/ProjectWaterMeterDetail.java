package com.xywg.equipment.monitor.iot.modular.watermeter.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
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
 * @since 2018-09-17
 */
@TableName("t_project_water_meter_detail")
public class ProjectWaterMeterDetail extends Model<ProjectWaterMeterDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 电力id
     */
	@TableField("electric_id")
	private Integer electricId;
    /**
     * 当前
     */
	@TableField("current_degree")
	private BigDecimal current;
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
	/**
	 * 真实值
	 */
	@TableField("actual_degree")
	private BigDecimal actualDegree;

	public BigDecimal getActualDegree() {
		return actualDegree;
	}

	public void setActualDegree(BigDecimal actualDegree) {
		this.actualDegree = actualDegree;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getElectricId() {
		return electricId;
	}

	public void setElectricId(Integer electricId) {
		this.electricId = electricId;
	}

	public BigDecimal getCurrent() {
		return current;
	}

	public void setCurrent(BigDecimal current) {
		this.current = current;
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

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectWaterMeterDetail{" +
			"id=" + id +
			", electricId=" + electricId +
			", current=" + current +
			", comments=" + comments +
			", deviceTime=" + deviceTime +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			"}";
	}
}
