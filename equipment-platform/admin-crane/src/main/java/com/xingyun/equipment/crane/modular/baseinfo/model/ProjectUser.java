package com.xingyun.equipment.crane.modular.baseinfo.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.core.BaseEntity;

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
 * @author wyf
 * @since 2018-08-20
 */
@Data
@TableName("t_project_user")
public class ProjectUser extends BaseEntity<ProjectUser> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 项目名称
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 姓名
     */
	private String name;
    /**
     * 身份证号
     */
	@TableField("identity_code")
	private String identityCode;
    /**
     * 手机号
     */
	private String phone;
    /**
     * 证书类型
     */
	@TableField("certificate_type")
	private Integer certificateType;
    /**
     * 证书名称
     */
	@TableField("certificate_name")
	private String certificateName;
    /**
     * 证书编号
     */
	@TableField("certificate_no")
	private String certificateNo;
    /**
     * 开始日期
     */
	@TableField("begin_date")
	private Date beginDate;
    /**
     * 结束日期
     */
	@TableField("end_date")
	private Date endDate;
	private Integer status;
    /**
     * 备注
     */
	private String comments;
    /**
     * 组织机构
     */
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
