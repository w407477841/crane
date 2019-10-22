package com.xywg.equipmentmonitor.core.common.constant;

import org.springframework.stereotype.Component;

@Component
/**
* @author: wangyifei
* Description:
* Date: 16:14 2018/9/13
*/
public class NettyConstant {

    /** 最大线程量    */
    private static final int MAX_THREADS = 1024;
    /** 数据包最大长度    */
    private static final int MAX_FRAME_LENGTH = 65535;

    /**
     * 安全帽设备在redis中的头部
     */
    public static final String  XYWG_IOT_HELMET="xywg_iot_helmet.";
	
}
