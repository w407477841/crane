package com.xywg.attendance.common.global;

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

    public static final String  HTTP_METHOD_POST="post";

    public static final String  HTTP_METHOD_GET="get";

    /**
     * separator
     */
    public static final String  SEPARATOR_EQUAL_SIGN="=";
    public static final String  SEPARATOR_WITH="&";

    /**
     * rabbitMq 消息中的topic
     */
    public static final String RABBITMQ_TOPIC_NAME_MQ= "xywg.suzhong.public.attendance.mq.topic.messages";



    /**
     * mongodb 湖北省考勤原始数据时Key(存储到mongodb)
     */
    public static final String MONGODB_ORIGINAL_DATA= "xywg_attendance_public_original_data";


}
