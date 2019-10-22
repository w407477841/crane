package com.xingyun.equipment.crane.modular.infromation.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.core.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通知公告
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_announcement")
public class ProjectAnnouncement extends BaseEntity<ProjectAnnouncement> {

    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	 /**
     * 内容
     */
	private String code;
	 /**
     * 内容
     */
	private String title;
    /**
     * 内容
     */
	private String content;
    /**
     * 状态
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
