package com.xywg.equipmentmonitor.modular.station.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
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
 * @since 2019-03-20
 */
@TableName("t_project_map")
@Data
public class ProjectMap extends Model<ProjectMap> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 图片地址
     */
	private String path;
    /**
     * 属性（1:平面图 2：楼号楼层图）
     */
	private Integer type;

	@TableField("x_zhou")
	private BigDecimal xZhou;

	@TableField("y_zhou")
	private BigDecimal yZhou;

	@TableField("location")
	private String location;

    /**
     * 项目id
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 备注
     */
	private String comments;
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




	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectMap{" +
			"id=" + id +
			", path=" + path +
			", type=" + type +
			", projectId=" + projectId +
			", comments=" + comments +
			", isDel=" + isDel +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", modifyTime=" + modifyTime +
			", modifyUser=" + modifyUser +
			"}";
	}
}
