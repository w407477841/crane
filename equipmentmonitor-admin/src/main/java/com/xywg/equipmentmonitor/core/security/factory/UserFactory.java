package com.xywg.equipmentmonitor.core.security.factory;

import com.xywg.equipmentmonitor.core.security.dto.UserDTO;
import com.xywg.equipmentmonitor.modular.system.model.User;

public class UserFactory {

	public static User getUser(){
		return null;
	}
	/**
	 * 返回用户
	 * @param user
	 * @return
	 */
	public static UserDTO  getUserDTO(User user){
		
		if(user == null){ return null;}
		
		UserDTO  userDTO = new UserDTO();
		userDTO.setUsername(user.getCode());
		userDTO.setPassword(user.getPassword());
		//账户认证过期
		userDTO.setCredentialsNonExpired(false);
		// 账户已过期
		userDTO.setAccountNonExpired(false);
		// 已启用
		userDTO.setEnabled(user.getStatus() == 0);
		//账户未被锁定
		userDTO.setAccountNonLocked(true);
		return userDTO;
	}
	
}
