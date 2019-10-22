package com.xywg.attendance.common.global;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     *设备获取命令redis的key 的前缀(这个key必须要和web端下发命令时存储到redis中的保持一致,否则获取不到web端下发的命令)
     */
    public static final String DEVICE_GET_COMMAND_REDIS_KEY= "xywg.zk.public.getCommand.";
    /**
     * 核心线程数 100
     * 最大线程数 400
     * 保留200 毫秒
     * 排队长度 500
     *
     */
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100,400,200,TimeUnit.MILLISECONDS,new ArrayBlockingQueue(500)) ;


}
