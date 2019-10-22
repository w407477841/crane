package com.xingyun.equipment.timer.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-06-19
 */
@TableName("t_project_crane_muti_collision_avoidance_set")
public class ProjectCraneMutiCollisionAvoidanceSet extends Model<ProjectCraneMutiCollisionAvoidanceSet> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 项目编号
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 修改序号
     */
	private String sequence;
    /**
     * 塔机编号
     */
	@TableField("crane_no")
	private String craneNo;
    /**
     * 塔机臂长
     */
	@TableField("arm_length")
	private Double armLength;
    /**
     * 相对距离
     */
	@TableField("relative_distance")
	private Double relativeDistance;
    /**
     * 相对角度
     */
	@TableField("relative_angle")
	private Double relativeAngle;
    /**
     * 相对高度
     */
	@TableField("relative_height")
	private Double relativeHeight;
    /**
     * 当前角度
     */
	@TableField("current_angle")
	private Double currentAngle;
    /**
     * 当前高度
     */
	@TableField("current_height")
	private Double currentHeight;
    /**
     * 设置状态
     */
	private Integer status;
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

	public ProjectCraneMutiCollisionAvoidanceSet() {
	}

	public ProjectCraneMutiCollisionAvoidanceSet(String deviceNo, Integer projectId, String sequence, String craneNo, Double armLength, Double relativeDistance, Double relativeAngle, Double relativeHeight, Double currentAngle, Double currentHeight, Integer status, Integer isDel, Date createTime) {
		this.deviceNo = deviceNo;
		this.projectId = projectId;
		this.sequence = sequence;
		this.craneNo = craneNo;
		this.armLength = armLength;
		this.relativeDistance = relativeDistance;
		this.relativeAngle = relativeAngle;
		this.relativeHeight = relativeHeight;
		this.currentAngle = currentAngle;
		this.currentHeight = currentHeight;
		this.status = status;
		this.isDel = isDel;
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneMutiCollisionAvoidanceSet{" +
			"id=" + id +
			", deviceNo=" + deviceNo +
			", projectId=" + projectId +
			", sequence=" + sequence +
			", craneNo=" + craneNo +
			", armLength=" + armLength +
			", relativeDistance=" + relativeDistance +
			", relativeAngle=" + relativeAngle +
			", relativeHeight=" + relativeHeight +
			", currentAngle=" + currentAngle +
			", currentHeight=" + currentHeight +
			", status=" + status +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			"}";
	}
}
