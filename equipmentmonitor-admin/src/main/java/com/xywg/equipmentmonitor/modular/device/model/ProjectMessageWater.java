package com.xywg.equipmentmonitor.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
@TableName("t_project_message_water")
public class ProjectMessageWater extends BaseEntity<ProjectMessageWater> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 短信模板编号
     */
	private Integer model;
    /**
     * 短信标题
     */
	private String title;
    /**
     * 发送时间
     */
	@TableField("send_time")
	private Date sendTime;
    /**
     * 内容
     */
	private String content;
    /**
     * 指定人
     */
	@TableField("related_user")
	private String relatedUser;
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
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
