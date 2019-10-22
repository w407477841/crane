package com.xingyun.equipment.security.provider;

import com.xingyun.equipment.core.enums.ResultCodeEnum;
import com.xingyun.equipment.core.exception.GlobalExceptionHandler;
import com.xingyun.equipment.system.dto.UserDTO;
import com.xingyun.equipment.system.service.SecurityService;
import com.xingyun.equipment.security.token.MyUsernameAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义用户名密码认证
 * 与MyUsernameAuthenticationToken绑定
 */
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
