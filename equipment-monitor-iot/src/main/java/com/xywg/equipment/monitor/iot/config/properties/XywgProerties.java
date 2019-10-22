package com.xywg.equipment.monitor.iot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangyifei
 */
@Component
@ConfigurationProperties(prefix = "xywg")
@Data
public class XywgProerties {
    /**
     * 扬尘 塔吊 升降机  端口
     */
    private int[] nettyServerPorts;
    /**
     * 水 端口
     */
    private int[] nettyWaterHexPorts;
    /**
     * 电  端口
     */
    private int[] nettyPowerHexPorts;

    /**
     * 心跳 端口
     */
    private int nettyHeartPort;
    /**
     * 环境监控服务端口
     */
    private Integer[] nettyMonitorServerPorts;

    private String redisHead;

    /**
     * 扬尘缓存前缀
     */
    private String redisYcDispatchPrefix;
    /**
     * 塔吊缓存前缀
     */
    private String redisTdDispatchPrefix;

    private String upgradeFileBasePath;

    private String nettyHost;

    private Integer nettyPort;

    public int[] getNettyWaterHexPorts() {
        return nettyWaterHexPorts;
    }

    public void setNettyWaterHexPorts(int[] nettyWaterHexPorts) {
        this.nettyWaterHexPorts = nettyWaterHexPorts;
    }

    public int[] getNettyPowerHexPorts() {
        return nettyPowerHexPorts;
    }

    public void setNettyPowerHexPorts(int[] nettyPowerHexPorts) {
        this.nettyPowerHexPorts = nettyPowerHexPorts;
    }

    public String getRedisHead() {
        return redisHead;
    }

    public void setRedisHead(String redisHead) {
        this.redisHead = redisHead;
    }

    public int[] getNettyServerPorts() {
        return nettyServerPorts;
    }

    public void setNettyServerPorts(int[] nettyServerPorts) {
        this.nettyServerPorts = nettyServerPorts;
    }

    public int getNettyHeartPort() {
        return nettyHeartPort;
    }

    public void setNettyHeartPort(int nettyHeartPort) {
        this.nettyHeartPort = nettyHeartPort;
    }

    public Integer[] getNettyMonitorServerPorts() {
        return nettyMonitorServerPorts;
    }

    public void setNettyMonitorServerPorts(Integer[] nettyMonitorServerPorts) {
        this.nettyMonitorServerPorts = nettyMonitorServerPorts;
    }

    public String getUpgradeFileBasePath() {
        return upgradeFileBasePath;
    }

    public void setUpgradeFileBasePath(String upgradeFileBasePath) {
        this.upgradeFileBasePath = upgradeFileBasePath;
    }


    public String getNettyHost() {
        return nettyHost;
    }

    public void setNettyHost(String nettyHost) {
        this.nettyHost = nettyHost;
    }

    public Integer getNettyPort() {
        return nettyPort;
    }

    public void setNettyPort(Integer nettyPort) {
        this.nettyPort = nettyPort;
    }
}
