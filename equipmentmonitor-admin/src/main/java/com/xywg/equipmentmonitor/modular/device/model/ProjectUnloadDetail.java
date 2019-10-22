package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
@TableName("t_project_unload_detail")
@Data
public class ProjectUnloadDetail extends Model<ProjectUnloadDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卸料id
     */
	@TableField("unload_id")
	private Integer unloadId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 司机
     */
	private Integer driver;
    /**
     * 时间
     */
	@TableField("device_time")
	private Date deviceTime;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 实时起重量
     */
	private BigDecimal weight;
    /**
     * 重量百分比
     */
	@TableField("weight_percentage")
	private BigDecimal weightPercentage;
    /**
     * 实时倾斜度1
     */
	@TableField("tilt_angle1")
	private BigDecimal tiltAngle1;
    /**
     * 倾斜度百分比1
     */
	@TableField("tilt_percent1")
	private BigDecimal tiltPercent1;
    /**
     * 实时倾斜度2
     */
	@TableField("tilt_angle2")
	private BigDecimal tiltAngle2;
    /**
     * 倾斜度百分比2
     */
	@TableField("tilt_percent2")
	private BigDecimal tiltPercent2;

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
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectUnloadDetail{" +
				"id=" + id +
				", unloadId=" + unloadId +
				", deviceNo='" + deviceNo + '\'' +
				", driver=" + driver +
				", deviceTime=" + deviceTime +
				", status=" + status +
				", weight=" + weight +
				", weightPercentage=" + weightPercentage +
				", tiltAngle1=" + tiltAngle1 +
				", tiltPercent1=" + tiltPercent1 +
				", tiltAngle2=" + tiltAngle2 +
				", tiltPercent2=" + tiltPercent2 +
				", isDel=" + isDel +
				", createTime=" + createTime +
				", createUser=" + createUser +
				", modifyTime=" + modifyTime +
				", modifyUser=" + modifyUser +
				'}';
	}
}
