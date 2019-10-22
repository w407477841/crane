package com.xywg.equipment.monitor.modular.dlt.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@TableName("t_project_electric_power")
public class ProjectElectricPower extends Model<ProjectElectricPower> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 设备名称
	 */
	@TableField("name")
	private String name;
    /**
     * 通信地址
     */
	@TableField("device_no")
	private String deviceNo;
	/**
	 * 采集器编号
	 */
	@TableField("collecter_no")
	private String collecterNo;
	/**
	 * 设备类别
	 */
	@TableField("type")
	private Integer type;
    /**
     * 项目
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * GPRS
     */
	private String gprs;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 制造厂商
     */
	private String manufactor;
    /**
     * 每秒耗电量(千瓦时)
     */
	private BigDecimal dissipation;
    /**
     * 是否在线
     */
	@TableField("is_online")
	private Integer isOnline;
    /**
     * 设备状态
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
     * 组织 
     */
	@TableField("org_id")
	private Integer orgId;

	/**
	 * 互感系数
	 */
	private BigDecimal ratio;

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

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

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getGprs() {
		return gprs;
	}

	public void setGprs(String gprs) {
		this.gprs = gprs;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getManufactor() {
		return manufactor;
	}

	public void setManufactor(String manufactor) {
		this.manufactor = manufactor;
	}

	public BigDecimal getDissipation() {
		return dissipation;
	}

	public void setDissipation(BigDecimal dissipation) {
		this.dissipation = dissipation;
	}

	public Integer getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPlacePoint() {
		return placePoint;
	}

	public void setPlacePoint(String placePoint) {
		this.placePoint = placePoint;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCollecterNo(String collecterNo) {
		this.collecterNo = collecterNo;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getCollecterNo() {
		return collecterNo;
	}

	public Integer getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ProjectElectricPower{" +
			"id=" + id +
			", name=" + name +
			", deviceNo=" + deviceNo +
			", collecterNo=" + collecterNo +
			", type=" + type +
			", projectId=" + projectId +
			", gprs=" + gprs +
			", specification=" + specification +
			", manufactor=" + manufactor +
			", dissipation=" + dissipation +
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
			"}";
	}
}
