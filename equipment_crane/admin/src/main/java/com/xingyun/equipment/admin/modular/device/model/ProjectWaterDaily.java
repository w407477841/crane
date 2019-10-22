package com.xingyun.equipment.admin.modular.device.model;

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
 * 电表每日统计
正常情况   读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 每日统计最后一条）
第一次      读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 明细当日第一条数据）
没有数据  读数=（用电量为每日统计最后一个读数） ，用电量 = 0
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
@TableName("t_project_water_daily")
public class ProjectWaterDaily extends Model<ProjectWaterDaily> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 设备ID
     */
	@TableField("device_id")
	private Integer deviceId;
    /**
     * 统计日期
     */
	@TableField("statistics_date")
	private Date statisticsDate;
    /**
     * 当前最后读数
     */
	private BigDecimal degree;
    /**
     * 用电量
     */
	@TableField("amount_used")
	private BigDecimal amountUsed;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Date getStatisticsDate() {
		return statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public BigDecimal getDegree() {
		return degree;
	}

	public void setDegree(BigDecimal degree) {
		this.degree = degree;
	}

	public BigDecimal getAmountUsed() {
		return amountUsed;
	}

	public void setAmountUsed(BigDecimal amountUsed) {
		this.amountUsed = amountUsed;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectWaterDaily{" +
			"id=" + id +
			", deviceId=" + deviceId +
			", statisticsDate=" + statisticsDate +
			", degree=" + degree +
			", amountUsed=" + amountUsed +
			"}";
	}
}
