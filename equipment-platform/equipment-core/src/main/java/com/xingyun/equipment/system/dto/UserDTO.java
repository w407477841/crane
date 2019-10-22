package com.xingyun.equipment.system.dto;

import com.xingyun.equipment.system.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDTO extends User implements UserDetails{

	private Collection<? extends GrantedAuthority> authorities;
	/**
	 * 账户已过期
	 */
	private boolean accountNonExpired ;
	/**
	 * 账户已被锁定
	 */
	private boolean accountNonLocked;
	/**
	 * 证书已过期
	 */
	private boolean credentialsNonExpired;
	/**
	 * 已激活
	 */
	private boolean isEnabled ;
	/**
	 *用户名
	 */
	private String username;
	/**
	 * 登录方式   1手机登录  2 用户名+密码
	 */
	private Integer loginType;
	/**
	 * 组织树
	 */
	private List<Integer> orgIds;
	
	

	

}
