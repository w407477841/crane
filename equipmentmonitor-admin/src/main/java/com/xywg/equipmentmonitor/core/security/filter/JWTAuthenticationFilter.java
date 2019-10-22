package com.xywg.equipmentmonitor.core.security.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xywg.equipmentmonitor.core.security.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xywg.equipmentmonitor.config.properties.CacheProperties;
import com.xywg.equipmentmonitor.config.properties.JwtProperties;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static com.xywg.equipmentmonitor.core.common.constant.Const.currUser;


/**
* @author: wangyifei
* Description: 拦截需要权限验证的所有请求 判断 header 中 或 参数中是否有JWT
* Date: 13:48 2018/9/8
*/
@SuppressWarnings("all")
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	/**
	 * jwt.header = Authorization jwt.secret = zyiot jwt.expiration = 604800
	 * jwt.tokenHead = "Bearer "
	 */
	
	private JwtProperties  jwtProperties;
	
	
	private SecurityService   securityService;
	

	public JWTAuthenticationFilter(JwtProperties jwtProperties,SecurityService securityService) {
		super();
		this.jwtProperties =jwtProperties;
		this.securityService = securityService;

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String headertoken = request.getHeader(this.jwtProperties.getHeader());

			if (headertoken == null || "".equals(headertoken)) {
				chain.doFilter(request, response);
				return;
			}
		// 将认证信息放入security上下文
		SecurityContextHolder.getContext().setAuthentication(getAuthentication(request, headertoken.replace(this.jwtProperties.getAuthPath()+" ", "")));
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
		//System.out.println("==token=="+token);
	// 获取IP地址
	//	System.out.println("x-forwarded-for:" + request.getHeader("x-forwarded-for"));
	//	System.out.println("remoteAddr:" + request.getRemoteAddr());
		log.info("x-forwarded-for：[{}]",request.getHeader("x-forwarded-for"));
		log.info("remoteAddr:[{}]" , request.getRemoteAddr());
		if (token != null) {
			// 解析 Authorization 得到用户名.
			Claims claims = null;

			try {
				//解析获得 权鉴
				claims = Jwts.parser().setSigningKey(this.jwtProperties.getSecret())
						.parseClaimsJws(token).getBody();

			} catch (ExpiredJwtException e) {
				e.printStackTrace();
			} catch (UnsupportedJwtException e) {
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
				if (claims == null) {
					// ThreadLocalExceptionMessage.push("登录过期", 403);
					// throw new RuntimeException("登录过期");
					return null;

				}
			}
			String user = claims.getSubject();
			//将token保存在当前线程中

			Const.token.set(token.split("\\.")[2]);
			//将用户保存在当前线程中
			UserVO curUser= securityService.getUser(user, CacheProperties.USER_KEY_PREFIX);

			Const.currUser.set(curUser);
			if(Const.currUser.get().getGroups().size()==1){
				//只有一个 组织

//				List<Integer> orgids= securityService.getOrgids(Const.currUser.get().getId());
//				if(null==orgids){
//					securityService.updateOrgids(Const.currUser.get().getId());
//				}
//				Const.orgIds.set(orgids);
				Const.orgId.set(securityService.getOrgId(Const.token.get()));
				Const.orgIds.set(securityService.getOrgids(Const.currUser.get().getId()));

			}else{
				//存在多个组织时,需要调用下system/organization/chooseOrg
				//将组织保存在当前线程中
				Const.orgId.set(securityService.getOrgId(Const.token.get()));
				Const.orgIds.set(securityService.getOrgids(Const.currUser.get().getId()));
			}


			return new UsernamePasswordAuthenticationToken(user, null,securityService.loadGrantedAuthorityByUser(user,CacheProperties.PERMISSION_KEY_PREFIX) );
		}
		return null;
	}

}
