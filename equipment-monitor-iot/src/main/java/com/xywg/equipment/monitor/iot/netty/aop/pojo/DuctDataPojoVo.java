package com.xywg.equipment.monitor.iot.netty.aop.pojo;

import lombok.Data;

/**
 * @author hjy
 * @date 2018/9/19
 * 一条完整的报文数据结构实体
 *  扬尘
 */
@Data
public class DuctDataPojoVo extends  CompleteDataPojo {
    /**
     * 0x02	设备类型	NUMBER
     */
    private Integer deviceType;

    /**
     * 0x03	序列号	STRING
     */
    private String sn;

    /**
     * 0x04	固件版本号	STRING
     */
    private String firmware;

    /**
     * 0x05	时间	BCD
     */
    private String dateTime;
    /**
     * 0x06	升级文件长度	NUMBER
     */
    private Integer fileLength;

    /**
     * 0x07	升级文件内容	BIN
     */
    private String fileContent;

    /**
     * 0X08	已接收文件长度	NUMBER
     */
    private Integer fileLengthReceived;

    /**
     * 0x09	日志文件长度	NUMBER
     */
    private Integer logFileLength;

    /**
     * 0x0A	日志文件内容	STRING
     */
    private String logFileContent;

    /**
     * 0x0B	风速	STRING
     */
    private String windSpeed;
    /**
     * 0x0C	风力	STRING
     */
    private String windForce;
    /**
     * 0x0D	风向	STRING
     */
    private String windDirection;
    /**
     * 0x0E	PM2.5	STRING
     */
    private String pm25;
    /**
     * 0x0F	PM10	STRING
     */
    private String pm10;
    /**
     * 0x10	温度	STRING
     **/
    private String temperature;
    /**
     * 0x11	湿度	STRING
     **/
    private String humidity;
    /**
     * 0x12	噪声	STRING
     **/
    private String noise;
    /**
     * 大气压
     */
    private String atmospheric;

}
