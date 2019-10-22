package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-22
 */
@Data
@TableName("t_project_lift_video")
public class ProjectLiftVideo extends Model<ProjectLiftVideo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 升降机id
     */
	@TableField("lift_id")
	private Integer liftId;
    /**
     * 用户名
     */
	@TableField("login_name")
	private String loginName;
    /**
     * 密码
     */
	private String password;
    /**
     * ip地址
     */
	@TableField("ip_address")
	private String ipAddress;
    /**
     * 端口号
     */
	private String port;
    /**
     * 平台区分
     */
	@TableField("platform_type")
	private Integer platformType;
    /**
     * 通道
     */
	private Integer tunnel;
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
	 * appkey
	 */
	@ApiModelProperty(value = "appkey")
	private String appkey;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String secret;
	/**
	 * 区域
	 */
	@ApiModelProperty(value = "备注")
	private Integer area;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLiftId() {
		return liftId;
	}

	public void setLiftId(Integer liftId) {
		this.liftId = liftId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public Integer getTunnel() {
		return tunnel;
	}

	public void setTunnel(Integer tunnel) {
		this.tunnel = tunnel;
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

	@Override
	public String toString() {
		return "ProjectLiftVideo{" +
			"id=" + id +
			", liftId=" + liftId +
			", loginName=" + loginName +
			", password=" + password +
			", ipAddress=" + ipAddress +
			", port=" + port +
			", platformType=" + platformType +
			", tunnel=" + tunnel +
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
