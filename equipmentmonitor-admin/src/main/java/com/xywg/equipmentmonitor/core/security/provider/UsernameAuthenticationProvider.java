package com.xywg.equipmentmonitor.core.security.provider;

import com.xywg.equipmentmonitor.core.aop.GlobalExceptionHandler;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.security.dto.UserDTO;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;
import com.xywg.equipmentmonitor.core.security.token.MyUsernameAuthenticationToken;
import com.xywg.equipmentmonitor.modular.system.model.User;
import com.xywg.equipmentmonitor.modular.system.service.IUserService;
/**
 * 自定义用户名密码认证
 * 与MyUsernameAuthenticationToken绑定
 */
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

	 @Autowired
			/**检测用户名*/
	 SecurityService securityService;

	    @Autowired
			/**密码生成器*/
	    BCryptPasswordEncoder passwordEncoder;
		private final static String TOKEN_KEY ="token-";
		
		private final static String PERMISSION_KEY ="permission-";
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		String username=authentication.getName();
		String password =(String) authentication.getCredentials();
		UserDTO user = (UserDTO) securityService.loadUserByUsername(username);
		if(user ==null){
			GlobalExceptionHandler.UNAUTHORIZED.set(ResultCodeEnum.NOUSER);
			throw new UsernameNotFoundException("用户名不存在");

		}
		if(passwordEncoder.matches(password,user.getPassword())){
			return new MyUsernameAuthenticationToken(user, password, null);
		}else{
			GlobalExceptionHandler.UNAUTHORIZED.set(ResultCodeEnum.UNAUTHORIZED);
			throw new BadCredentialsException("密码错误");	
		}
		
		
	}

	//与MyUsernameAuthenticationToken绑定。
	
	@Override
	public boolean supports(Class<?> authentication) {
		return MyUsernameAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
