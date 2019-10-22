package com.xingyun.equipment.crane.core.util;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PushUtil extends BasePushUtil {

	public static PushUtil pushUtil;

	public PushUtil() {
		if (null == client) {
			try {
				beforeClass();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static PushUtil getInstance() {
		pushUtil = new PushUtil();
		return pushUtil;
	}

	/**
	 * type 0 广推送 1 定点推送 allDevices 所有推送的设备id Content 推送内容更 allId 所有看到信息人的id id记录的id  kind判断往企业端还是工人端推送
	 *
	 * @param type
	 * @param allDevices
	 * @param content
	 * @throws Exception
	 */
	public void advancedPush(int type, String allDevices, String content) throws Exception {
		//System.err.println("设备id="+allDevices);
		PushRequest pushRequest = new PushRequest();
		// 安全性比较高的内容建议使用HTTPS
		pushRequest.setProtocol(ProtocolType.HTTPS);
		// 内容较大的请求，使用POST请求
		pushRequest.setMethod(MethodType.POST);
		// 推送目标
		pushRequest.setAppKey(appKeyWorker);
		pushRequest.setTarget("DEVICE"); // 推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送 TAG:按标签推送; ALL: 广播推送
		pushRequest.setTargetValue(allDevices); // 根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2.
		pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
		pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
		// 推送配置
		pushRequest.setTitle("劳务通"); // 消息的标题
		pushRequest.setBody(content); // 消息的内容
		// 推送配置: iOS
		pushRequest.setIOSBadge(1); // iOS应用图标右上角角标
		pushRequest.setIOSSilentNotification(false);// 开启静默通知
		pushRequest.setIOSMusic("default"); // iOS通知声音
		pushRequest.setIOSSubtitle("");// iOS10通知副标题的内容
		pushRequest.setIOSNotificationCategory("iOS10 Notification Category");// 指定iOS10通知Category
		pushRequest.setIOSMutableContent(true);// 是否允许扩展iOS通知内容
		pushRequest.setIOSApnsEnv("DEV");// iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
		pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
		pushRequest.setIOSRemindBody("iOSRemindBody");// iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
		
		Map<String, String> info = new HashMap<String, String>();
		info.put("noticeId", allDevices);
		info.put("type", String.valueOf(type));
		String json = JSON.toJSONString(info);
		pushRequest.setIOSExtParameters(json); // 通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)

		// 推送配置: Android
		pushRequest.setAndroidNotifyType("NONE");// 通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
		pushRequest.setAndroidNotificationBarType(1);// 通知栏自定义样式0-100
		pushRequest.setAndroidNotificationBarPriority(1);// 通知栏自定义样式0-100
		pushRequest.setAndroidMusic("default"); // Android通知音乐
		pushRequest.setAndroidXiaoMiActivity("com.ali.demo1.MiActivity");// 设置该参数后启动小米托管弹窗功能,
		pushRequest.setAndroidXiaoMiNotifyTitle("Mi title");
		pushRequest.setAndroidXiaoMiNotifyBody("MiActivity Body");
		pushRequest.setAndroidExtParameters(json); // 设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
		pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
		String value = pushRequest.getTargetValue();
		
		if (StringUtils.isNotBlank(value)) {
			PushResponse pushResponse = client.getAcsResponse(pushRequest);
			System.out.printf("RequestId: %s, MessageID: %s\n", pushResponse.getRequestId(),
					pushResponse.getMessageId());
		}
	}

}
