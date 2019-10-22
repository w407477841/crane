package com.xywg.attendance.common.global;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hjy
 * @date 2019/2/22
 * 请求路径枚举类
 */
public enum RequestUrlEnum {
    /**
     * 设备初始化
     */
    DEVICE_INIT("handleDeviceInit", "/iclock/cdata\\?SN=([\\s\\S]*)&options=([\\s\\S]*)&pushver=([\\s\\S]*)", "GET", "设备初始化请求地址"),

    /**
     * 推送配置信息
     */
    PUSH_CONFIG_INFORMATION("pushConfigInformation", "/iclock/cdata\\?SN=([\\s\\S]*)&table=options([\\s\\S]*)", "POST", "推送配置信息"),

    /**
     * 上传更新信息
     * 主要上传客户端的固件版本号、登记用户数、登记指纹数、考勤记录数、设备IP地址、指纹算法版本、人脸算法版本、
     * 注册人脸所需人脸个数、登记人脸数、设备支持功能标示信息
     */
    UPLOAD_UPDATE_INFORMATION("uploadUpdateInformation", "/iclock/getrequest\\?SN=([\\s\\S]*)&INFO=([\\s\\S]*)", "Get", "上传更新信息"),

    /**
     * 上传考勤记录
     */
    ATTENDANCE_RECORD("handleUploadAttendance", "/iclock/cdata\\?SN=([\\s\\S]*)&table=ATTLOG([\\s\\S]*)",
            "POST",
            "考勤记录"),
    /**
     * 考勤记录图片
     */
    ATTENDANCE_RECORD_PICTURE("handleUploadAttendancePhoto",
            "/iclock/cdata\\?SN=([\\s\\S]*)&table=ATTPHOTO([\\s\\S]*)",
            "POST",
            "考勤记录图片"),
    /**
     * 考勤记录图片
     */
    ATTENDANCE_RECORD_PICTURE2("handleUploadAttendancePhoto",
            "/iclock/fdata\\?SN=([\\s\\S]*)&table=ATTPHOTO([\\s\\S]*)",
            "POST",
            "考勤记录图片"),

    /**
     * 上传身份证信息
     */
    UPLOAD_IDCARD_INFORMATION("uploadIDCardInformation",
            "/iclock/cdata\\?SN=([\\s\\S]*)&table=IDCARD([\\s\\S]*)",
            "POST",
            "上传身份证信息"),

    /**
     * 上传一体化模板
     */
    ATTENDANCE_UPLOAD_TEMP_PICTURE("handleUploadTempPhoto", "/iclock/cdata\\?SN=([\\s\\S]*)&table=BIODATA([\\s\\S]*)",
            "POST", "上传一体化模板"),

    /**
     * 获取命令
     */
    HANDLE_GET_COMMAND("handleGetCommand", "/iclock/getrequest\\?SN=([\\s\\S]*)", "Get", "获取命令"),


    /**
     * 命令回复
     */
    HANDLE_COMMAND_RESPONSE("handleCommandResponse", "/iclock/devicecmd\\?SN=([\\s\\S]*)", "POST", "命令回复"),

    /**
     * 异地考勤
     */
    HANDLE_BEYOND_ATTENDANCE("handleBeyondAttendance", "/iclock/cdata\\?SN=([\\s\\S]*)&table=RemoteAtt&PIN=([\\s\\S]*)", "POST", "异地考勤"),

    /*
     * 交换公钥
     */
    HANDLE_EXCHANGE_PUBLIC_KEY("handleExchangePublicKey", "/iclock/exchange\\?SN=([\\s\\S]*)&type=publickey", "POST", "交换公钥"),

    /**
     * 交换因子（支持通信加密的场合） Exchange factor
     */
    HANDLE_EXCHANGE_FACTOR("handleExchangeFactor", "/iclock/exchange\\?SN=([\\s\\S]*)&type=factors", "POST", "交换公钥"),


    /**
     * 具有相同的url
     */
    TABLE_OPERLOG("handleTableOperLog", "/iclock/cdata\\?SN=([\\s\\S]*)&table=OPERLOG([\\s\\S]*)", "POST", "table=OPERLOG相同URL"),


    /**
     * 第三方工人现场考勤信息(面向对象为湖北省授权的公司或者单位)
     */
    UPLOAD_WORKER_ATTENDANCE_INFO("uploadWorkerAttendanceInfo", "/appDevice/uploadWorkerAttendanceInfo", "POST",
            "第三方考勤上报"),

    /**
     * 第三方工人现场考勤信息批量上传(面向对象为湖北省授权的公司或者单位)
     */
    UPLOAD_WORKER_ATTENDANCE_INFO_BATCH("uploadWorkerAttendanceInfoBatch", "/appDevice/uploadWorkerAttendanceInfoBatch", "POST",
            "第三方考勤批量上报"),

    /**
     * 第三方工人现场考勤照片信息(面向对象为湖北省授权的公司或者单位)
     */
    UPLOAD_WORKER_ATTENDANCE_PHOTO_INFO("uploadWorkerAttendancePhotoInfo", "/appDevice/uploadWorkerAttendancePhotoInfo", "POST",
            "第三方工人现场考勤照片信息"),;

    private String methodName;
    private String url;
    private String method;
    private String name;

    RequestUrlEnum(String methodName, String url, String method, String name) {
        this.methodName = methodName;
        this.url = url;
        this.method = method;
        this.name = name;
    }


    /**
     * 根据url 匹配  得到方法名
     *
     * @param inUrl
     * @return
     */
    public static RequestUrlEnum getMethodName(String inUrl, String method) {
        for (RequestUrlEnum c : RequestUrlEnum.values()) {
            Pattern p = Pattern.compile(c.url);
            Matcher m = p.matcher(inUrl);
            boolean r = m.matches();
            if (r && method.equalsIgnoreCase(c.method)) {
                return c;
            }
        }
        return null;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
