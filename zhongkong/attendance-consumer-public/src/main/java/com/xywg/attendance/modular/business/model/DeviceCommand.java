package com.xywg.attendance.modular.business.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("buss_device_command")
public class DeviceCommand extends Model<DeviceCommand> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	@TableField("device_sn")
	private String deviceSn;
	private Integer type;

	/**
	 * 0=未执行，1=成功，-1=失败，999=取消
	 */
	private Integer state;
	/**
	 * 执行时间
	 */
	@TableField("process_date")
	private Date processDate;

	private String description;

	@TableField("create_date")
	private Date createDate;

	@TableField("create_user")
	private String createUser;

	@TableField("update_date")
	private Date updateDate;

	@TableField("update_user")
	private String updateUser;

	private String remark;

	@TableField("is_del")
	private Integer isDel;

	private String uuid;


	public DeviceCommand(String deviceSn, Integer state, Date processDate, Date updateDate, String uuid) {
		this.deviceSn = deviceSn;
		this.state = state;
		this.processDate = processDate;
		this.updateDate = updateDate;
		this.uuid = uuid;
	}

	public DeviceCommand() {
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
