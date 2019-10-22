package com.xywg.equipment.monitor.config;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:40 2018/9/3
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = AliConfig.ALI )
public class AliConfig {
    public static final  String ALI =  "ali";

    private String accessKeyId;

    private String accessKeySecret;

    private long iosAppKey;

    private long androidAppKey;
    /** 环境 */
    private String env ;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }


    public long getIosAppKey() {
        return iosAppKey;
    }

    public void setIosAppKey(long iosAppKey) {
        this.iosAppKey = iosAppKey;
    }

    public long getAndroidAppKey() {
        return androidAppKey;
    }

    public void setAndroidAppKey(long androidAppKey) {
        this.androidAppKey = androidAppKey;
    }

    @Bean
    public IClientProfile profile (){

        return DefaultProfile.getProfile("cn-hangzhou", accessKeyId,accessKeySecret);
    }


}
