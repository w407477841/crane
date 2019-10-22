package com.xingyun.equipment.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = CacheProperties.CACHE_PREFIX)
public class CacheProperties {
	 
	public  static final String CACHE_PREFIX = "xywg.cache";


	public static final String CACHE_VALUE_YEAR_2 =  "xywg-sbgl-year-2" ;

	/**
	 * 存两天
	 */
	public  static final String CACHE_VALUE_DAY_2 ="xywg-sbgl-day-2" ;
	/**
	 * 存30分钟
	 */
	public  static final String CACHE_VALUE_MIN_30 ="xywg-sbgl-min-30" ;
	/**
	 * 存2分钟
	 */
	public  static final String CACHE_VALUE_MIN_2 ="xywg-sbgl-min-2" ;



	/**
	  存2小时
	 */
	public  static final String CACHE_VALUE_HOUR_2 ="xywg-sbgl-hour-2" ;
	
	public static final  String TOKEN_KEY_PREFIX = "sbgl-token-";
	
	public static final  String PERMISSION_KEY_PREFIX = "sbgl-permission-";
	
	public static final  String VERIFICATION_KEY_RPEFIX = "sbgl-verification-" ;
	
	public static final  String USER_KEY_PREFIX   = "sbgl-user-";
	
	
	public static final String VERIFICATION_TYPE_LOGIN= "login-" ;
	
	public static final String ORGID_KEY_PREFIX = "sbgl-orgid-";
	
}
