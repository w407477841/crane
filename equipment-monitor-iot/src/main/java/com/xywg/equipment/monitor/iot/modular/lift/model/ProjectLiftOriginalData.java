package com.xywg.equipment.monitor.iot.modular.lift.model;

import java.io.Serializable;

import java.util.Date;
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
 * @since 2018-08-22
 */
@TableName("t_project_lift_original_data")
public class ProjectLiftOriginalData extends Model<ProjectLiftOriginalData> {

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
		return "ProjectLiftOriginalData{" +
			"id=" + id +
			", deviceNo=" + deviceNo +
			", originalData=" + originalData +
			", deviceTime=" + deviceTime +
			"}";
	}
}
