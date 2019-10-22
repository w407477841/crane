package com.xywg.iot.modules.netty.handler;


import com.xywg.iot.modules.helmet.model.DeviceWorker;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hjy
 * 用于统一管理所有的链接  通道与设备编号互相持有
 */
@Component
public class NettyChannelManage {

    /**
     * 管理所有的连接
     */
    public static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    public static final AttributeKey<DeviceWorker> NETTY_CHANNEL_KEY = AttributeKey.valueOf("helmet.channel");

    /**
     * 移除连接
     */
    public static void removeChannel(Channel channel) {
        //当前的
        DeviceWorker deviceWorker = getDeviceWorkerByChannel(channel);
        if(StringUtils.isNotBlank(deviceWorker.getImei())){
            //原来的
            Channel channelMap=CHANNEL_MAP.get(deviceWorker.getImei());
            //原来的
            DeviceWorker  deviceInfo=  getDeviceWorkerByChannel(channelMap);

            if(deviceWorker.getUuid().equals(deviceInfo.getUuid())){
                CHANNEL_MAP.remove(deviceWorker.getImei());
            }
        }
    }

    /**
     * 根据信道返回设备属性
     *
     * @param channel
     * @return
     */
    public static DeviceWorker getDeviceWorkerByChannel(Channel channel) {
        return channel.attr(NETTY_CHANNEL_KEY).get();
    }

    /**
     * 根据设备编号获取通道
     * @param deviceCode
     * @return
     */
    public static Channel getChannel(String deviceCode) {
        return CHANNEL_MAP.get(deviceCode);
    }


}
