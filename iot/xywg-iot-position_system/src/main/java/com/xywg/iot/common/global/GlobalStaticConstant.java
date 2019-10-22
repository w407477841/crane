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
    //设备在线
    public static final int DEVICE_STATE_ON_LINE=1;
    //设备离线
    public static final int DEVICE_STATE_OFF_LINE=0;
    /**
     * AQ（Package type）：表示数据包是请求包、应答包，1：请求包
     */
    public static final String DATA_PACKEAGE_REQUEST="01";

    /**
     * AQ（Package type）：表示数据包是请求包、应答包，0：应答包
     */
    public static final String DATA_PACKAGE_RESPONSE="00";


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
     * 健康
     */
    public static final int HEALTH = 0x0f;
    /**
     * 每次发送的文件长度
     */
    public static final int PER_LENGTH = 1024;


}
