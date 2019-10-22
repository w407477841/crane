package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_project_environment_monitor_video")
public class ProjectEnvironmentMonitorVideo extends BaseEntity<ProjectEnvironmentMonitorVideo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * monitor_id
     */
	@TableField("monitor_id")
	private Integer monitorId;
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
     * 类别
     */
	private String type;
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
	




	@Override
	public String toString() {
		return "ProjectEnvironmentMonitorVideo{" +
			"id=" + id +
			", monitorId=" + monitorId +
			", loginName=" + loginName +
			", password=" + password +
			", ipAddress=" + ipAddress +
			", port=" + port +
			", platformType=" + platformType +
			", tunnel=" + tunnel +
			", comments=" + comments +
			", orgId=" + orgId +
			"}";
	}







	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
