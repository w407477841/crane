package com.xywg.attendance.modular.command.model;

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
@TableName("buss_device_exception_record")
public class BussDeviceExceptionRecord extends Model<BussDeviceExceptionRecord> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	@TableField("device_sn")
	private String deviceSn;
	@TableField("id_card_type")
	private Integer idCardType;
	@TableField("id_card_number")
	private String idCardNumber;
	private Date time;
	private String photo;
	@TableField("create_date")
	private Date createDate;
	@TableField("create_user")
	private String createUser;
	@TableField("update_date")
	private Date updateDate;
	@TableField("update_user")
	private String updateUser;
	private String remark;
	@TableField("exception_type")
	private Integer exceptionType;
	@TableField("is_del")
	private Integer isDel;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
