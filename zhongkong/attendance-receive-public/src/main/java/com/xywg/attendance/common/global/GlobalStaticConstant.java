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


    /**
     * rabbitMq 消息中的topic
     */
    public static final String RABBITMQ_TOPIC_NAME_MQ= "xywg.suzhong.public.attendance.mq.topic.messages";



    //--------↓-------------↓-----↓-----redis中的--------↓-------------↓--------------↓-------------
    /**
     *设备获取命令redis的key 的前缀(这个key必须要和web端下发命令时存储到redis中的保持一致,否则获取不到web端下发的命令)
     */
    public static final String DEVICE_GET_COMMAND_REDIS_KEY= "xywg.zk.public.getCommand.";

    /**
     *设备信息状态存储到redis(用于判断设备是否在线)
     */
    public static final String DEVICE_INFO_STATUS_REDIS_KEY= "xywg.zk.public.deviceInfoStatus.";

    /**
     *用于存储设备的公钥 因子  keyModel
     */
    public static final String DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY= "xywg.zk.public.deviceServerPublicKey.";
    //-------↑--------↑----------↑------redis中的--------↑------------↑---------↑----------↑-------

}
