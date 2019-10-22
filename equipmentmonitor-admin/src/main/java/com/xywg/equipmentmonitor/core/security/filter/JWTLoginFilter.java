package com.xywg.equipmentmonitor.core.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.security.vo.UserVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xywg.equipmentmonitor.config.properties.CacheProperties;
import com.xywg.equipmentmonitor.config.properties.JwtProperties;
import com.xywg.equipmentmonitor.config.properties.XywgProperties;
import com.xywg.equipmentmonitor.core.security.dto.UserDTO;
import com.xywg.equipmentmonitor.core.security.enums.LoginType;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;
import com.xywg.equipmentmonitor.core.security.token.MyUsernameAuthenticationToken;

import cn.hutool.json.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import static com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum.NO_ORG;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端 
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 
 * attemptAuthentication ：接收并解析用户凭证。 
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。 
 * 
 * 会拦截
 *  POST /login/
 *  requestBody:{
 *  username:""
 *  password:""
 *  
 *  }
 * 
 */  
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
	private JwtProperties jwtProperties  ;
	private XywgProperties   xywgProperties;
	private AuthenticationManager  authenticationManager;
	private SecurityService  userService;
	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		super();

		this.authenticationManager = authenticationManager;
	}
	public JWTLoginFilter(JwtProperties jwtProperties,XywgProperties xywgProperties,AuthenticationManager authenticationManager,SecurityService  userService) {
		super();
		this.jwtProperties = jwtProperties;
		this.authenticationManager = authenticationManager;
		this.userService  =  userService;
	}



	/**
	 * 接收并解析用户凭证
	 * @param req
	 * @param res
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException {
			//获取请求中的用户名/密码
			try {
				
				UserDTO user = new ObjectMapper()  
				            .readValue(req.getInputStream(), UserDTO.class);
				if(user.getLoginType().intValue()==LoginType.username.getType()){
					Authentication auth=authenticationManager.authenticate(
							//对应UsernameAuthenticationProvider
		                    new MyUsernameAuthenticationToken(
		                            user.getUsername(),  
		                            user.getPassword(),  
		                            user.getAuthorities()));	
		            
		            return   auth;	
				}else if(user.getLoginType().intValue()==LoginType.phone.getType()){
					if(xywgProperties.getPhoneValid()){
						//已开启手机号+验证码认证
						
					}else{
						//未开启手机号+验证码认证
						
					}
				}		
				
				return null;
			} catch (IOException e) {
			 	e.printStackTrace();
			 	return null;
			}
	           
	          
	          
	}
    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token  
	//返回给前台的数据
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
			HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		Claims claims =new  DefaultClaims();
		Long lasttime=System.currentTimeMillis() + jwtProperties.getExpiration();
		 String username = ((UserDTO) auth.getPrincipal()).getUsername();
		//持有者
		 claims.put("sub",username);
		//过期时间
		claims.put("exp", new Date(lasttime));
		//权限
		claims.put("auths",null);
		 String token = Jwts.builder()  
	               .setClaims(claims)
	                .setExpiration(new Date(lasttime))  
	                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())  
	                .compact(); 
		 
		 //token  加入    缓存key= xywg:user:token  
		 //权限   加入缓存  key  =xywg:user:permission
		

		 JSONObject   resultJson = new JSONObject();

		Const.token.set(token.split(".")[2]);

		UserVO user = 	userService.getUser(username,CacheProperties.USER_KEY_PREFIX);
		//删除之前的集团缓存，防止切换集团后无反应
		//userService.removeOrgids(user.getId());

		if(user.getGroups()==null||user.getGroups().size()==0){
			// 没有集团
			resultJson.put("code",NO_ORG.code());
			resultJson.put("message",NO_ORG.msg() );
			if(user.getGroups().size()==1){
				userService.updateOrgId(Const.token.get(),user.getGroups().get(0).getId());
			}
		} else{
			resultJson.put("user",user);
			resultJson.put("code",200);
			resultJson.put("message","登录成功" );
			resultJson.put("auth",jwtProperties.getAuthPath()+" " + token);
		}


		res.setContentType("application/json;charset=UTF-8");
		 	res.getWriter().write(
		 			resultJson.toString()
		 			);
	        res.addHeader(jwtProperties.getHeader(), jwtProperties.getAuthPath()+" " + token);  
	}
	
	
	private boolean checkUserAgent(String ua){
		String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" }; //定义移动端请求的所有可能类型
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
		// 排除 苹果桌面系统
		if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
			for (String item : agent) {
				if (ua.contains(item)) {
					flag = true;
					break;
				}
			}
		}
		}
		return flag;
		}

}
