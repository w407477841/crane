package com.xywg.equipmentmonitor.core.model;

import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用bean类
 * 
 * @author liujj
 * @param <T>
 * @date 2018年01月22日
 *
 */
@Getter
@Setter
public abstract class BaseEntity<T> extends Model<BaseEntity<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

}
