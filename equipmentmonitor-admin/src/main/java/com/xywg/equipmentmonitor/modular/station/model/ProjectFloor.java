package com.xywg.equipmentmonitor.modular.station.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-03-22
 */
@TableName("t_project_floor")
@Data
public class ProjectFloor extends Model<ProjectFloor> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String name;
	private Integer pid;
	/**
	 * 删除标志
	 */
	@TableField(value = "is_del", fill = FieldFill.INSERT)
	@TableLogic
	private Integer isDel;
	/**
	 * 创建日期
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(value="create_user",fill = FieldFill.INSERT)
	private Integer createUser;
	/**
	 * 创建人名称
	 */
	@TableField(exist = false)
	private String createUserName;
	/**
	 * 修改日期
	 */
	@TableField(value = "modify_time", fill = FieldFill.UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 修改人
	 */
	@TableField(value="modify_user", fill = FieldFill.UPDATE)
	private Integer modifyUser;
	@TableField("project_id")
	private Integer projectId;
	@TableField(exist = false)
	private Integer status;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectFloor{" +
			"id=" + id +
			", name=" + name +
			", pid=" + pid +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			", projectId=" + projectId +
			"}";
	}
}
