package com.xingyun.equipment.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
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

import com.xingyun.equipment.admin.config.properties.JwtProperties;
import com.xingyun.equipment.admin.config.properties.XywgProperties;
import com.xingyun.equipment.admin.core.security.filter.JWTAuthenticationFilter;
import com.xingyun.equipment.admin.core.security.filter.JWTLoginFilter;
import com.xingyun.equipment.admin.core.security.provider.UsernameAuthenticationProvider;
import com.xingyun.equipment.admin.core.security.service.SecurityService;
import org.springframework.util.AntPathMatcher;


/**
 * 
 * 认证配置
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = false)
@Order(value = SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private SecurityService securityService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UsernameAuthenticationProvider usernameAuthenticationProvider;
	@Autowired
	JwtProperties  jwtProperties;
	@Autowired
	XywgProperties  xywgProperties  ;
	



	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth
		//.authenticationProvider(authenticationProvider)
		.authenticationProvider(usernameAuthenticationProvider)
		.userDetailsService(securityService)
		.passwordEncoder(bCryptPasswordEncoder)
		// .and().authenticationProvider(authenticationProvider)
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers(HttpMethod.POST, "/users").antMatchers("/", "/auth/**", "/resources/**",
				"/static/**", "/public/**", "/webui/**", "/h2-console/**", "/configuration/**", "/swagger-ui/**",
				"/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**", "/*.html", "/**/*.html",
				"/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf",

				"/**/*.woff",
				"/ssdevice/crane/craneInfo/**",
				"/ssdevice/environment/environmentInfo/**",
				"/ssdevice/project/projectEnvironmentMonitorDetail/trend",
				//近期最后一条扬尘数据
				"/ssdevice/project/projectEnvironmentMonitorDetail/getMonitorData"
				,"/ssdevice/project/projectEnvironmentMonitorDetail/getDeviceStatus"
				,"/ssdevice/lift/liftInfo/**"
				 //近期最后一条扬尘数据
				,"/ssdevice/project/projectLiftDetail/getMonitorData"
				,"/ssdevice/project/realTimeMonitoringTower/getMonitorData"
				,"/ssdevice/project/alarmInfo/update"
				,"/ssdevice/project/alarmInfo/getList"
				,"/ssdevice/project/alarmInfo/getListByFlag"
				,"/ssdevice/fileUpload/fileupload"
				,"/ssdevice/fileUpload/photoupload"
				,"/ssdevice/fileUpload/filedownload"
				,"/ssdevice/project/getTypeChart"
				,"/ssdevice/project/getChart"
				,"/ssdevice/login"
				,"/ssdevice/alipay/topay"
				,"/ssdevice/alipay/notifyUrl"
				,"/ssdevice/alipay/returnUrl"
				,"/ssdevice/alipay/queryAlipay"
		)
				//扬尘数据大屏页面接口
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectEnvironmentMonitor/getEnvironmentInfoForScreen")
				//7小时趋势
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectEnvironmentMonitorDetail/trendForScreen")
				//设备在线状态
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectEnvironmentMonitorDetail/getMonitorStatus")
				//设备在线状态
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectEnvironmentMonitorDetail/getLiftStatus")
				//设备在线状态
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectEnvironmentMonitorDetail/getCraneStatus")
				//扬尘数据
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectEnvironmentMonitorDetail/getMonitorDataForScreen")
				//固定返回升降机100条数据(假数据)
				.antMatchers(HttpMethod.GET,"/ssdevice/lift/liftInfo/getLiftDetails")
				//对外 升降机接口
				.antMatchers(HttpMethod.GET,"/ssdevice/lift/*")
				.antMatchers(HttpMethod.GET,"/ssdevice/crane/*")
				.antMatchers(HttpMethod.GET,"/ssdevice/enviroment/*")


				// 没找到
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectLiftAlarm/getAlarmDetail")
				//智慧工地拉取用接口
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectWaterMeter/getWaterDetailInfo")
				//智慧工地拉取报警信息
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectWaterMeter/getAlarmInfo")
				//智慧工地拉取报警信息明细
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectWaterMeter/getAlarmDetail")
				//智慧工地拉取用接口
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectElectricPower/getElectricDetailInfo")
				//智慧工地拉取报警信息
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectElectricPower/getAlarmInfo")
				//智慧工地拉取报警信息明细
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectElectricPower/getAlarmDetail")
				//查询塔吊模拟数据(就是假数据)
				.antMatchers(HttpMethod.GET,"/ssdevice/device/projectCrane/getCraneDetails")
				//获取用电统计信息给智慧工地
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectElectricPowerDetail/getElectricInfo")
				//获取用水统计信息给智慧工地
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectElectricPowerDetail/getWaterInfo")
				// 安全帽接口-》智慧工地（带健康数据）
				.antMatchers(HttpMethod.GET,"/ssdevice/health/*")
				.antMatchers(HttpMethod.GET,"/ssdevice/rfid/device/liftAlarm/getAlarmList")

				.antMatchers(HttpMethod.GET,"/ssdevice/rfid/device/craneAlarm/getAlarmList")
				// 扬尘数据接口
				.antMatchers(HttpMethod.GET,"/ssdevice/project/projectEnvironmentMonitorDetail/getMonitorInfo")
				// 智慧工地新增项目
				.antMatchers(HttpMethod.POST,"/ssdevice/projectManagement/projectInfo/insertProject")
				// 新增扬尘实时数据
				.antMatchers(HttpMethod.POST,"/ssdevice/device/monitor/insertData")
				// 新增升降机实时数据
				.antMatchers(HttpMethod.POST,"/ssdevice/device/lift/insertData")
				// 新增塔吊实时数据
				.antMatchers(HttpMethod.POST,"/ssdevice/device/crane/insertData")
				// 劳务通 基站接口
				.antMatchers(HttpMethod.GET,"/ssdevice/station2lwt/*")
				// 银江国际 接口
				.antMatchers("/ssdevice/yjgj/**")

				// 智慧工地 基站接口
				.antMatchers("/ssdevice/station2zhgd/*")
				// 智慧工地 安全帽
				.antMatchers("/ssdevice/helmet2zhgd/*")
				//获取下线设备
				.antMatchers("/ssdevice/project/projectEnvironmentMonitorDetail/getOfflines")
				//获取天气信息
				.antMatchers("/ssdevice/project/projectEnvironmentMonitorDetail/getWeatherInfo")
				// 智慧工地
				.antMatchers("/ssdevice/zhgd/api/**")
				// 塔吊数据
				//.antMatchers("/ssdevice/craneData/api/**")
				// word导出
				.antMatchers("/ssdevice/word/download")
//				.antMatchers("/ssdevice/device/projectCrane/getDeviceAccountExcel")
//				.antMatchers("/ssdevice/device/projectCrane/getAnalysisListExcel")
//		       //塔吊数据导出Excel
//				.antMatchers("/ssdevice/craneData/api/getDeviceFlowExcel")
//				.antMatchers("/ssdevice/craneData/api/deviceOfflineExcel")
//				.antMatchers("/ssdevice/craneData/api/getMomentPercentExcel")
//				.antMatchers("/ssdevice/craneData/api/selectMomentPercentInfoExcel")
//				.antMatchers("/ssdevice/craneData/api/selectIsOnlineExcel")
//				.antMatchers("/ssdevice/craneData/api/selectAlarmInfoExcel")
//				.antMatchers("/ssdevice/craneData/api/selectWorkGradeExcel")
//				.antMatchers("/ssdevice/craneData/api/selectIsOnlineExcel")
//				.antMatchers("/ssdevice/craneData/api/getWarnInfoExcel")
//				.antMatchers("/ssdevice/craneData/api/getWorkGradeExcel")

				.antMatchers("/ssdevice/craneToPdf/report")
		;


	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		/*
		 * http.authorizeRequests()
		 * .antMatchers(HttpMethod.GET,"/admin/").hasAnyRole("ADMIN")
		 * .antMatchers(HttpMethod.GET, "/user/").hasRole("USER")
		 * .and().formLogin().loginProcessingUrl("/login/").passwordParameter(
		 * "password").usernameParameter("username").permitAll()
		 * .and().csrf().disable().logout().logoutUrl("/logout/").permitAll() ;
		 */

	JWTLoginFilter  loginFilter=	new JWTLoginFilter(jwtProperties,xywgProperties, authenticationManager(),
				securityService);
		httpSecurity
				.cors().and()
				// 由于使用的是JWT，我们这里不需要csrf
				.csrf().disable()

				// .exceptionHandling().disable() //去掉异常处理
				// 原本403异常会被处理掉，现在会抛出异常。并且只能在为进入controler的全局异常处理捕获
				// 基于token，所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				// 允许对于网站静态资源的无授权访问
				.antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
				.permitAll().antMatchers(HttpMethod.GET, "/task/**").permitAll()// 任务
				.antMatchers(HttpMethod.GET, "/upload/image/**").permitAll()// 上传
				.antMatchers(HttpMethod.POST, "/user/signup").permitAll()
				.antMatchers(HttpMethod.GET, "/validRegisterCode").permitAll()// 验证注册码
				.antMatchers(HttpMethod.GET, "/phoneCode").permitAll()// 获取手机验证码
				.antMatchers(HttpMethod.GET, "/validCode").permitAll()// 验证手机号+验证码\
				.antMatchers(HttpMethod.GET, "/loginCode").permitAll()// 验证手机登录验证码
				// 注册
				.antMatchers(HttpMethod.POST, "/register").permitAll()
				.antMatchers(HttpMethod.POST,"/ssdevice/login").permitAll()
				.antMatchers(HttpMethod.GET,"/ssdevice/auth").permitAll()
				.antMatchers(HttpMethod.GET, "/hello/**").permitAll()// websocket
				.antMatchers(HttpMethod.GET, "/send").permitAll()// websocket
				.antMatchers(HttpMethod.GET, "/send/**").permitAll()// websocket
				.antMatchers(HttpMethod.GET,"/ssdevice/shuidian/**").permitAll()
				// .antMatchers("/other","/other/**").hasRole("ADMIN")
				// 除上面外的所有请求全部需要鉴权认证
				.anyRequest().authenticated().and()
				.addFilter(loginFilter)
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
