package com.xingyun.equipment.security;

import com.xingyun.equipment.system.service.SecurityService;
import com.xingyun.equipment.core.properties.JwtProperties;
import com.xingyun.equipment.core.properties.XywgProperties;
import com.xingyun.equipment.security.filter.JWTAuthenticationFilter;
import com.xingyun.equipment.security.filter.JWTLoginFilter;
import com.xingyun.equipment.security.provider.UsernameAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * 
 * 认证配置
 * @author: wyf
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = false)
@EnableConfigurationProperties(value ={JwtProperties.class,XywgProperties.class})
@Order(value = 10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private SecurityService securityService;

	@Autowired
	JwtProperties jwtProperties;
	@Autowired
	XywgProperties xywgProperties;
	@Autowired
	Ignore ignore;


	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder(){

		return new BCryptPasswordEncoder();
	}



	@Bean
	UsernameAuthenticationProvider usernameAuthenticationProvider(){
		return new UsernameAuthenticationProvider();
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth
		//.authenticationProvider(authenticationProvider)
		.authenticationProvider(usernameAuthenticationProvider())
		.userDetailsService(securityService)
		.passwordEncoder(bCryptPasswordEncoder())
		// .and().authenticationProvider(authenticationProvider)
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		ignore.ignore(web);
		web.ignoring().antMatchers("/admin-login/login");

	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.cors().and()
				// 由于使用的是JWT，我们这里不需要csrf
				.csrf().disable()

				// .exceptionHandling().disable() //去掉异常处理
				// 原本403异常会被处理掉，现在会抛出异常。并且只能在为进入controler的全局异常处理捕获
				// 基于token，所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()

				// .antMatchers("/other","/other/**").hasRole("ADMIN")
				// 除上面外的所有请求全部需要鉴权认证
				.anyRequest().authenticated().and()
				//.addFilter(loginFilter)
				.addFilterAfter(new JWTAuthenticationFilter(jwtProperties
				,securityService), JWTLoginFilter.class);


		// 添加JWT filter
		// httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
		// UsernamePasswordAuthenticationFilter.class);
		// 禁用缓存
		/// httpSecurity.headers().cacheControl();

	}
	//配置 密码器
		@Bean
		public BCryptPasswordEncoder passwordEncoder(){
			return new BCryptPasswordEncoder();
		}

	/**
	 * 进入controller后的异常处理
	 * 
	 * @return
	@Bean
	public CustomExceptionResolver customExceptionResolver() {

		return new CustomExceptionResolver();

	}
	 */

	/*
	 * @Bean MessageSource getMessageSource(){
	 * ReloadableResourceBundleMessageSource parentMessageSource = new
	 * ReloadableResourceBundleMessageSource();
	 * parentMessageSource.setDefaultEncoding("UTF-8");
	 * parentMessageSource.setBasename(
	 * "classpath:org/springframework/security/messages"); return
	 * parentMessageSource; }
	 */
	/*
	 * @Bean(name="localeResolver") public LocaleResolver localeResolverBean(){
	 * CookieLocaleResolver localeResolver=new CookieLocaleResolver();
	 * localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE); return
	 * localeResolver;
	 * 
	 * }
	 */

}
