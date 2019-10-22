package com.xingyun.equipment.timer.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@TableName("t_project_crane_alarm")
@Data
public class ProjectCraneAlarm extends Model<ProjectCraneAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 塔吊id
     */
	@TableField("crane_id")
	private Integer craneId;

	@TableField("detail_id")
	private Integer detailId;

	@TableField("alarm_id")
	private Integer alarmId;

	/**
	 * 设备编号
	 */
	@TableField("device_no")
	private String deviceNo;
	/**
	 * 报警类型
	 */
	@TableField("alarm_info")
	private String alarmInfo;

	/**
	 * 报警时间
	 */
	@TableField(exist = false)
	private String alarmTime;
	@TableField(exist = false)
	private Date deviceTime;
	@TableField(exist = false)
	private String value;

	/**状态*/
	private Integer status;
	/**修改人*/
	@TableField("modify_user_name")
	private String modifyUserName;

	/**
	 * 删除标志
	 */
	@TableField(value = "is_del")
	@TableLogic
	private Integer isDel;
	/**
	 * 创建日期
	 */
	@TableField(value = "create_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(value="create_user")
	private Integer createUser;
	/**
	 * 创建人名称
	 */
	@TableField(exist = false)
	private String createUserName;
	/**
	 * 修改日期
	 */
	@TableField(value = "modify_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 修改人
	 */
	@TableField(value="modify_user")
	private Integer modifyUser;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}



	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
