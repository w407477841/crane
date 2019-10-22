package com.xingyun.equipment.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@ConfigurationProperties(prefix = XywgProperties.PREFIX)
public class XywgProperties {
 
    public static final String PREFIX = "xywg";

    private Boolean kaptchaOpen = false;

    private Boolean swaggerOpen = false;
    
    private Integer nettyServerPorts[]  ;
    
    private Integer nettyHeartPort;


    private String defaultPassword;


    private String fileUploadPath;

    private Boolean haveCreatePath = false;

    private Boolean springSessionOpen = false;
    /**
     * 手机关闭
     */
    private Boolean phoneValid =false;
    

    public Boolean getPhoneValid() {
		return phoneValid;
	}

	public void setPhoneValid(Boolean phoneValid) {
		this.phoneValid = phoneValid;
	}

	/**
     * session 失效时间（默认为30分钟 单位：秒）
     */
    private Integer sessionInvalidateTime = 30 * 60;

    /**
     * session 验证失效时间（默认为15分钟 单位：秒）
     */
    private Integer sessionValidationInterval = 15 * 60;

    private  String  initStartTime = "2019-06-14 00:00:00";

    private String    initEndTime = "2019-07-13 00:00:00";

    public String getInitStartTime() {
        return initStartTime;
    }

    public void setInitStartTime(String initStartTime) {
        this.initStartTime = initStartTime;
    }

    public String getInitEndTime() {
        return initEndTime;
    }

    public void setInitEndTime(String initEndTime) {
        this.initEndTime = initEndTime;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public Boolean getKaptchaOpen() {
        return kaptchaOpen;
    }

    public void setKaptchaOpen(Boolean kaptchaOpen) {
        this.kaptchaOpen = kaptchaOpen;
    }

    public Boolean getSwaggerOpen() {
        return swaggerOpen;
    }

    public void setSwaggerOpen(Boolean swaggerOpen) {
        this.swaggerOpen = swaggerOpen;
    }

    public Boolean getSpringSessionOpen() {
        return springSessionOpen;
    }

    public void setSpringSessionOpen(Boolean springSessionOpen) {
        this.springSessionOpen = springSessionOpen;
    }

    public Integer getSessionInvalidateTime() {
        return sessionInvalidateTime;
    }

    public void setSessionInvalidateTime(Integer sessionInvalidateTime) {
        this.sessionInvalidateTime = sessionInvalidateTime;
    }

    public Integer getSessionValidationInterval() {
        return sessionValidationInterval;
    }

    public void setSessionValidationInterval(Integer sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
    }

	public Integer[] getNettyServerPorts() {
		return nettyServerPorts;
	}

	public void setNettyServerPorts(Integer[] nettyServerPorts) {
		this.nettyServerPorts = nettyServerPorts;
	}

	public Integer getNettyHeartPort() {
		return nettyHeartPort;
	}

	public void setNettyHeartPort(Integer nettyHeartPort) {
		this.nettyHeartPort = nettyHeartPort;
	}


    
}
