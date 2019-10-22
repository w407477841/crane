package com.xingyun.equipment.admin.core.security.vo;

import java.util.List;

import com.xingyun.equipment.admin.modular.system.model.Organization;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.xingyun.equipment.admin.modular.system.model.User;

/**
* @author: wangyifei
* Description:
* Date: 11:22 2018/9/4
*/
@Data
public class UserVO extends  User{
	private static final long serialVersionUID = 1L;
	
	private List<Integer>  orgids;

	private List<Organization> groups;





	public static UserVO factory(User user){
		
		UserVO  userVO = new UserVO();
		
		BeanUtils.copyProperties(user, userVO);
		
		return userVO;
	}
	
	
}
