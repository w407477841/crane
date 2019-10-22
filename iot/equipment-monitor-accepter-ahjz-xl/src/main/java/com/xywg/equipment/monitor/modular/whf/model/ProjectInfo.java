package com.xywg.equipment.monitor.modular.whf.model;

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
 * @author hy
 * @since 2019-07-11
 */
@TableName("t_project_info")
public class ProjectInfo extends Model<ProjectInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 项目名称
     */
	private String name;
    /**
     * 开工日期
     */
	@TableField("begin_date")
	private Date beginDate;
    /**
     * 结束日期
     */
	@TableField("end_date")
	private Date endDate;
    /**
     * 项目区域
     */
	private Integer position;
    /**
     * 项目地址
     */
	private String address;
    /**
     * 类型（1、房建 2、市政）
     */
	private Integer type;
    /**
     * 项目简介
     */
	private String introduction;
    /**
     * 施工单位
     */
	private Integer builder;
    /**
     * 勘察单位
     */
	private String surveyor;
    /**
     * 建筑面积
     */
	@TableField("building_size")
	private BigDecimal buildingSize;
    /**
     * 监理单位
     */
	private String supervisor;
    /**
     * 建设单位
     */
	@TableField("construction_unit")
	private String constructionUnit;
    /**
     * 项目经理
     */
	private String manager;
    /**
     * 联系电话
     */
	@TableField("manager_phone")
	private String managerPhone;
    /**
     * 定额工期
     */
	@TableField("fix_days")
	private String fixDays;
    /**
     * 项目经纬度
     */
	@TableField("place_point")
	private String placePoint;
    /**
     * 项目范围
     */
	@TableField("project_scope")
	private String projectScope;
    /**
     * 项目平面图
     */
	private String ichnography;
    /**
     * uuid
     */
	private String uuid;
    /**
     * 状态
     */
	private Integer status;
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
     * 组织机构
     */
	@TableField("org_id")
	private Integer orgId;
    /**
     * 订阅主题
     */
	private String topic;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getBuilder() {
		return builder;
	}

	public void setBuilder(Integer builder) {
		this.builder = builder;
	}

	public String getSurveyor() {
		return surveyor;
	}

	public void setSurveyor(String surveyor) {
		this.surveyor = surveyor;
	}

	public BigDecimal getBuildingSize() {
		return buildingSize;
	}

	public void setBuildingSize(BigDecimal buildingSize) {
		this.buildingSize = buildingSize;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getConstructionUnit() {
		return constructionUnit;
	}

	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public String getFixDays() {
		return fixDays;
	}

	public void setFixDays(String fixDays) {
		this.fixDays = fixDays;
	}

	public String getPlacePoint() {
		return placePoint;
	}

	public void setPlacePoint(String placePoint) {
		this.placePoint = placePoint;
	}

	public String getProjectScope() {
		return projectScope;
	}

	public void setProjectScope(String projectScope) {
		this.projectScope = projectScope;
	}

	public String getIchnography() {
		return ichnography;
	}

	public void setIchnography(String ichnography) {
		this.ichnography = ichnography;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectInfo{" +
			"id=" + id +
			", name=" + name +
			", beginDate=" + beginDate +
			", endDate=" + endDate +
			", position=" + position +
			", address=" + address +
			", type=" + type +
			", introduction=" + introduction +
			", builder=" + builder +
			", surveyor=" + surveyor +
			", buildingSize=" + buildingSize +
			", supervisor=" + supervisor +
			", constructionUnit=" + constructionUnit +
			", manager=" + manager +
			", managerPhone=" + managerPhone +
			", fixDays=" + fixDays +
			", placePoint=" + placePoint +
			", projectScope=" + projectScope +
			", ichnography=" + ichnography +
			", uuid=" + uuid +
			", status=" + status +
			", comments=" + comments +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			", orgId=" + orgId +
			", topic=" + topic +
			"}";
	}
}
