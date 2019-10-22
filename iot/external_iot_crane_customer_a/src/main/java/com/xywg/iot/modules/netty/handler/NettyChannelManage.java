package com.xywg.iot.modules.netty.handler;

import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.modules.crane.model.DeviceHold;
import com.xywg.iot.modules.crane.model.ProjectCrane;
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

    public static final AttributeKey<DeviceHold> NETTY_CHANNEL_KEY = AttributeKey.valueOf("crane.channel");

    /**
     * 移除连接
     */
    public static void removeChannel(Channel channel) {
        //当前的
        DeviceHold deviceHold = getDeviceInfo(channel);
        if (deviceHold.getProjectCrane() != null) {
            //原来的
            Channel channelMap = CHANNEL_MAP.get(deviceHold.getProjectCrane().getDeviceNo());
            //原来的
            DeviceHold deviceInfo = getDeviceInfo(channelMap);
            if (deviceHold.getUuid().equals(deviceInfo.getUuid())) {
                CHANNEL_MAP.remove(deviceHold.getProjectCrane().getDeviceNo());
            }
        }
    }

    /**
     * 根据信道返回设备属性
     *
     * @param channel
     * @return
     */
    public static DeviceHold getDeviceInfo(Channel channel) {
        return channel.attr(NETTY_CHANNEL_KEY).get();
    }

    /**
     * 根据设备编号获取通道
     *
     * @param deviceCode
     * @return
     */
    public static Channel getChannel(String deviceCode) {
        return CHANNEL_MAP.get(deviceCode);
    }

    /**
     * 保存信道相关持有信息
     */
    public static void saveChannel(Channel channel, ProjectCrane projectCrane) {
        DeviceHold deviceHold = new DeviceHold(DataUtil.getUUID(), projectCrane);
        CHANNEL_MAP.put(projectCrane.getDeviceNo(), channel);
        channel.attr(NETTY_CHANNEL_KEY).set(deviceHold);
    }

}
