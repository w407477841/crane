package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@TableName("t_project_crane_original_data")
public class ProjectCraneOriginalData extends Model<ProjectCraneOriginalData> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	@TableField("device_no")
	private String deviceNo;
	@TableField("original_data")
	private String originalData;
	@TableField("device_time")
	private Date deviceTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getOriginalData() {
		return originalData;
	}

	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneOriginalData{" +
			"id=" + id +
			", deviceNo=" + deviceNo +
			", originalData=" + originalData +
			", deviceTime=" + deviceTime +
			"}";
	}

	public ProjectCraneOriginalData(String deviceNo, String originalData, Date deviceTime) {
		this.deviceNo = deviceNo;
		this.originalData = originalData;
		this.deviceTime = deviceTime;
	}

	public ProjectCraneOriginalData() {
	}
}
