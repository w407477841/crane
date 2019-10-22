package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.xywg.equipment.monitor.config.AliConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:04 2018/9/3
 * Modified By : wangyifei
 */
@Component
public  class PushService {

    @Autowired
    IClientProfile profile  ;
    @Autowired
    AliConfig aliConfig;

    public void pushMob( String title,String body ,String uuid){
        if(aliConfig.isOpen()){
            this.pushIOS(title,body,uuid);
            this.pushANDROID(title,body,uuid);
        }

    }

    /**
     * 推送IOS 基础
     * @param title
     * @param body
     */
    private void pushIOS( String title,String body ,String uuid){
    PushRequest pushRequest = pushRequest();
        System.out.println("ioskey = "+aliConfig.getIosAppKey());
        pushRequest.setAppKey(aliConfig.getIosAppKey());
        pushRequest.setTitle(title);
        pushRequest.setBody(body);
        pushRequest.setTargetValue(uuid);
        pushRequest.setDeviceType("iOS");
        // iOS应用图标右上角角标
        pushRequest.setIOSBadge(5);
        // iOS通知声音
        pushRequest.setIOSMusic("default");
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushResponse pushResponse = null;
        try {
            pushResponse = client.getAcsResponse(pushRequest);
            System.out.printf("RequestId: %s,message: %s\n",
                    pushResponse.getRequestId(), pushResponse.getMessageId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送ANDROID  基础
     * @param title
     * @param body
     */
    private void pushANDROID( String title,String body ,String uuid){
        PushRequest pushRequest = pushRequest();
        pushRequest.setTitle(title);
        pushRequest.setBody(body);
        pushRequest.setTargetValue(uuid);
        pushRequest.setAppKey(aliConfig.getAndroidAppKey());
        pushRequest.setDeviceType("ANDROID");
        //通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        pushRequest.setAndroidNotifyType("NONE");
        //通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarType(1);
        //通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);
        // Android通知音乐
        pushRequest.setAndroidMusic("default");



        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushResponse pushResponse = null;
        try {
            pushResponse = client.getAcsResponse(pushRequest);
            System.out.printf("RequestId: %s,message: %s\n",
                    pushResponse.getRequestId(), pushResponse.getMessageId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    public PushRequest  pushRequest (){



        PushRequest pushRequest =new PushRequest();

        pushRequest.setTarget("TAG");
        pushRequest.setPushType("NOTICE");
        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
        pushRequest.setIOSMusic("default"); // iOS通知声音
        pushRequest.setIOSApnsEnv(aliConfig.getEnv());//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。'DEV': 表示开发环境 'PRODUCT': 表示生产环境
        //  pushRequest.setIOSRemind(true); //  消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：**离线消息转通知仅适用于`生产环境`**
        // pushRequest.setIOSRemindBody("PushRequest summary"); // iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=`PRODUCT` && iOSRemind为true时有效
        return pushRequest;

    }

}
