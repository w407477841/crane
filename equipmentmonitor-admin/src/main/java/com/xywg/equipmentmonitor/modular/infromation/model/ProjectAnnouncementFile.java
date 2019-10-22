package com.xywg.equipmentmonitor.modular.infromation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 公告附件
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
@Data
@TableName("t_project_announcement_file")

@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectAnnouncementFile extends BaseEntity<ProjectAnnouncementFile> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * announcement_id
     */
	@TableField("announcement_id")
	private Integer announcementId;
    /**
     * 文件编码
     */
	private String code;
    /**
     * 文件名称
     */
	private String name;
    /**
     * 文件类型
     */
	private String type;
    /**
     * 文件大小
     */
	private String size;
    /**
     * 文档编写人
     */
	private String author;
    /**
     * 保存路径
     */
	private String path;
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


	

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	
}
