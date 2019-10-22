package com.xingyun.equipment.admin.modular.system.transfer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户传输bean
 * 
 * @author wangcw
 * @Date 2017/5/5 22:40
 */
@Data
public class UserDto{

	private Integer id;
	private String account;
	private String password;
	private String salt;
	private String name;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private Integer sex;
	private String email;
	private String phone;
	private String roleid;
	private Integer deptid;
	private Integer status;
	private Date createtime;
	private Integer version;
	private String avatar;
	private Integer userType;
	private Integer dataSource;
	private Integer userFlag;
	private Integer isEnterprise;
	private Integer idCardType;
	private String idCardNumber;
}
