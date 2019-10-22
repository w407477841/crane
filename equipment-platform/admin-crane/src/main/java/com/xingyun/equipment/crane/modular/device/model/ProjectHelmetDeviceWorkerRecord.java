package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


/**
 * <p>
 * 项目_用户与设备绑定关系履历

 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@Data
@TableName("t_project_device_worker_record")
public class ProjectHelmetDeviceWorkerRecord extends Model<ProjectHelmetDeviceWorkerRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Integer id;
    /**
     * 项目id
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 安全帽id
     */
	@TableField("device_id")
	private Integer deviceId;
	/**
	 *
	 */
	@TableField("device_no")
	private Integer deviceNo;

    /**
     * 证件类型,默认1:身份证
     */
	@TableField("id_card_type")
	private Integer idCardType;
    /**
     * 证件编号
     */
	@TableField("id_card_number")
	private String idCardNumber;



	private String name;

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




	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
