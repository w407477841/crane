package com.xywg.iot.common.global;

/**
 * @author jingyun_hu
 * @date 2018/10/28
 * 全局静态常量类
 */
public class GlobalStaticConstant {
    //设备在线
    public static final int DEVICE_STATE_ON_LINE = 1;
    //设备离线
    public static final int DEVICE_STATE_OFF_LINE = 0;
    //设备启用状态 启用
    public static final int DEVICE_STATE_ENABLED = 1;
    //设备启用状态 未启用
    public static final int DEVICE_STATE_NOT_ENABLED = 0;
    /**
     * 协议固定头
     */
    public static final String PROTOCOL_FIXED_HEAD = "A55A";
    /**
     * 协议固定头
     */
    public static final Integer PROTOCOL_FIXED_HEAD_INTEGER = 0xA55A;

    /**
     * 协议固定尾
     */
    public static final String PROTOCOL_FIXED_TAIL = "Cc33c33c";

    /**
     * redis 前缀  (公司外部塔吊协议头)
     */
    public static final String REDIS_PREFIX = "xywg_iot_crane_customer";
    /**
     * 比较报警信息头
     */
    public static final String COMPARE_FLAG_0001 = "0001";
    /**
     * 比较报警信息头
     */
    public static final String COMPARE_FLAG_0002 = "0002";
    /**
     * 比较报警信息头
     */
    public static final String COMPARE_FLAG_0003 = "0003";
    /**
     * 比较报警信息头
     */
    public static final String COMPARE_FLAG_0004 = "0004";


}
