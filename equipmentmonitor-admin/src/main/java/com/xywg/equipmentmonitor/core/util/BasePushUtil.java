package com.xywg.equipmentmonitor.core.util;

import org.springframework.context.annotation.Configuration;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

@Configuration
public class BasePushUtil {

	protected String region;

	protected long appKey;
	protected long appKeyWorker;

	protected DefaultAcsClient client;
	
	
	private final static String KEYID = "LTAI6HsdkjddOmpW";
	private final static String KEYSECRET = "1CNiUqGTK4LIju4iq3rieu7SqywMaB";
	
//	private final static String KEYID = "LTAIrenF0FN5gPjp";
//	private final static String KEYSECRET = "DqtRzu9RyM0s7iVxJ2R0RN0rbK1bzP";
	
	//企业端
	private final static String APPKEY = "24807205";
	
	//工人端
	private final static String APPPERSONKEY = "24808233";


	/**
	 * 从配置文件中读取配置值，初始化Client
	 * <p>
	 * <p>
	 * <p>
	 * 1. 如何获取 accessKeyId/accessKeySecret/appKey 照见README.md 中的说明<br/>
	 * <p>
	 * 2. 先在 push.properties 配置文件中 填入你的获取的值
	 */

	public void beforeClass() throws Exception {
		String accessKeyId = KEYID;
		String accessKeySecret = KEYSECRET;
		region = "cn-hangzhou";
		appKey = Long.valueOf(APPKEY);
		appKeyWorker= Long.valueOf(APPPERSONKEY);
		IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
		client = new DefaultAcsClient(profile);
	}

}
