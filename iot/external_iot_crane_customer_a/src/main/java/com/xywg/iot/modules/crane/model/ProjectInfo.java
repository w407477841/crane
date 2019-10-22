package com.xywg.iot.modules.crane.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

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
@Data
@TableName("t_project_info")
public class ProjectInfo extends BaseEntity<ProjectInfo> {

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
	private Double buildingSize;
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
	 * uuid
	 */
	private String uuid;
    /**
     * 备注
     */
	private String comments;
    /**
     * 组织机构
     */
	@TableField("org_id")
	private Integer orgId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void setBuilder(Integer builder) {
		this.builder = builder;
	}

	public void setSurveyor(String surveyor) {
		this.surveyor = surveyor;
	}

	public void setBuildingSize(Double buildingSize) {
		this.buildingSize = buildingSize;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public void setFixDays(String fixDays) {
		this.fixDays = fixDays;
	}

	public void setPlacePoint(String placePoint) {
		this.placePoint = placePoint;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Integer getPosition() {
		return position;
	}

	public String getAddress() {
		return address;
	}

	public Integer getType() {
		return type;
	}

	public String getIntroduction() {
		return introduction;
	}

	public Integer getBuilder() {
		return builder;
	}

	public String getSurveyor() {
		return surveyor;
	}

	public Double getBuildingSize() {
		return buildingSize;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public String getConstructionUnit() {
		return constructionUnit;
	}

	public String getManager() {
		return manager;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public String getFixDays() {
		return fixDays;
	}

	public String getPlacePoint() {
		return placePoint;
	}

	public String getUuid() {
		return uuid;
	}

	public String getComments() {
		return comments;
	}

	public Integer getOrgId() {
		return orgId;
	}
}
