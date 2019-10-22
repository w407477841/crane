package com.xywg.iot.common.global;

/**
 * @author jingyun_hu
 * @date 2018/10/28
 * 全局静态常量类
 */
public class GlobalStaticConstant {

    //1塔吊
    public static final int DEVICE_TYPE_FLAG_CRANE = 1;
    //2升降机
    public static final int DEVICE_TYPE_FLAG_LIFT = 2;
    //3扬尘
    public static final int DEVICE_TYPE_FLAG_DUST = 3;
    //星云网格协议头开始位标识
    public static final String  XYWG_PROTOCOL_HEAD  ="FFFFFE";
    /**
     * AQ（Package type）：表示数据包是请求包、应答包，1：请求包
     */
    public static final String DATA_PACKEAGE_REQUEST="01";
    /**
     * AQ（Package type）：表示数据包是请求包、应答包，0：应答包
     */
    public static final String DATA_PACKAGE_RESPONSE="00";


    /**
     * 安全帽设备在redis中的头部
     */
    public static final String  XYWG_IOT_HELMET="xywg_iot_helmet.";



    //设备在线
    public static final int DEVICE_STATE_ON_LINE=1;
    //设备离线
    public static final int DEVICE_STATE_OFF_LINE=0;
    /**
     * 下线
     */
    public static final int TERMINAL_DOWN = Integer.MAX_VALUE;
    /**
     *  登录
     */
    public static final int LOGIN = 0x10;
    /**
     * 定位、健康
     */
    public static final int POSITION_HEALTH = 0x30;
    /**
     * // 定位
     */
    public static final int POSITION = 0x07;

    /**
     * 室内位置定位
     */
    public static final int INDOOR_POSITION = 0x11;
    /**
     * 健康
     */
    public static final int HEALTH = 0x0f;
    /**
     * 六轴一般健康数据
     */
    public static final String DATA_HEALTH_SIXAXIS_NORMAL="00";
    /**
     * 六轴跌倒健康数据
     */
    public static final String DATA_HEALTH_SIXAXIS_TUMBLE="02";
    /**
     * 心率健康阈值小值
     */
    public static final int  HEART_RATE_MIN_VALUE=40;
    /**
     * 心率健康阈值大值
     */
    public static final int  HEART_RATE_MAX_VALUE=160;
    /**
     * 血氧含量百分比阈值
     */
    public static final int  HEART_RATE_BLOOD_OXYGEN=94;

    /**
     * 体温最小值
     */
    public static final Double  HEART_RATE_TEMPERATURE_MIN=35.5;
    /**
     * 体温最小值
     */
    public static final Double  HEART_RATE_TEMPERATURE_MAX=38.5;



}
