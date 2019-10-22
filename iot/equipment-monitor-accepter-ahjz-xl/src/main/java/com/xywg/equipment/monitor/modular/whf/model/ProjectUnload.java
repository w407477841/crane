package com.xywg.equipment.monitor.modular.whf.model;

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
 * 卸料设备表
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
@TableName("t_project_unload")
@Data
public class ProjectUnload extends Model<ProjectUnload> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 工程名称
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 产权编号
     */
	private String identifier;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 产权单位
     */
	private String owner;
    /**
     * 制造厂家
     */
	private String manufactor;
    /**
     * 最大载重量
     */
	@TableField("max_weight")
	private BigDecimal maxWeight;
    /**
     * 最大倾角1
     */
	@TableField("max_tilt_angle1")
	private BigDecimal maxTiltAngle1;
    /**
     * 最小倾角1
     */
	@TableField("min_tilt_angle1")
	private BigDecimal minTiltAngle1;
    /**
     * 最大倾角2
     */
	@TableField("max_tilt_angle2")
	private BigDecimal maxTiltAngle2;
    /**
     * 最小倾角2
     */
	@TableField("min_tilt_angle2")
	private BigDecimal minTiltAngle2;
    /**
     * 在线状态
     */
	@TableField("is_online")
	private Integer isOnline;
    /**
     * 状态  (启用状态)
     */
	private Integer status;
    /**
     * 位置
     */
	@TableField("place_point")
	private String placePoint;
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
     * 组织结构
     */
	@TableField("org_id")
	private Integer orgId;
    /**
     * SIM卡号
     */
	private Integer gprs;
    /**
     * 固件当前版本号
     */
	@TableField("firmware_version")
	private String firmwareVersion;
    /**
     * 固件升级时间
     */
	@TableField("upgrade_time")
	private Date upgradeTime;
    /**
     * 卸料平台编号
     */
	@TableField("unload_no")
	private String unloadNo;
    /**
     * 倍率
     */
	private Integer multiple;
    /**
     * 名称
     */
	private String name;
	@TableField("no_load_weight_ad")
	private BigDecimal noLoadWeightAD;
	@TableField("no_load_weight")
	private BigDecimal noLoadWeight;
	@TableField("load_weight_ad")
	private BigDecimal loadWeightAD;
	@TableField("load_weight")
	private BigDecimal loadWeight;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	@Override
	public String toString() {
		return "ProjectUnload{" +
			"id=" + id +
			", projectId=" + projectId +
			", deviceNo=" + deviceNo +
			", identifier=" + identifier +
			", specification=" + specification +
			", owner=" + owner +
			", manufactor=" + manufactor +
			", maxWeight=" + maxWeight +
			", maxTiltAngle1=" + maxTiltAngle1 +
			", minTiltAngle1=" + minTiltAngle1 +
			", maxTiltAngle2=" + maxTiltAngle2 +
			", minTiltAngle2=" + minTiltAngle2 +
			", isOnline=" + isOnline +
			", status=" + status +
			", placePoint=" + placePoint +
			", comments=" + comments +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			", orgId=" + orgId +
			", gprs=" + gprs +
			", firmwareVersion=" + firmwareVersion +
			", upgradeTime=" + upgradeTime +
			", unloadNo=" + unloadNo +
			", multiple=" + multiple +
			", name=" + name +
			"}";
	}
}
