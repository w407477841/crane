package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-06-21
 */
@TableName("t_project_crane_alarm")
@Data
public class ProjectCraneAlarm extends Model<ProjectCraneAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@ApiModelProperty(value = "ID")
	@TableId(value = "id", type = IdType.AUTO)
	private String id;
    /**
     * 塔吊id
     */
	@TableField("crane_id")
	private Integer craneId;
    /**
     * 实时数据ID
     */
	@TableField("detail_id")
	private Integer detailId;
    /**
     * 报警id
     */
	@TableField("alarm_id")
	private Integer alarmId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 报警内容
     */
	@TableField("alarm_info")
	private String alarmInfo;
    /**
     * 删除标志
     */
	@TableField("is_del")
	private Integer isDel;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	private Integer createUser;
    /**
     * 修改日期
     */
	@TableField("modify_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modifyTime;
    /**
     * 修改人（处理人id）
     */
	@TableField("modify_user")
	private Integer modifyUser;
    /**
     * 处理人姓名
     */
	@TableField("modify_user_name")
	private String modifyUserName;
    /**
     * 状态
     */
    @TableField("status")
	private Integer status;

	/**
	 *  意见
	 */
    private String comments;
	/**
	 * 是否处理
	 */

	@TableField("is_handle")
	private Integer isHandle;
	/**
	 * 处理时间
	 *
	 * */


	@TableField("alarm_time")
	private Date alarmTime;






	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
