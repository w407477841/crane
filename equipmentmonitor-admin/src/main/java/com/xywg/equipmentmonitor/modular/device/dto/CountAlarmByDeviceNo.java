package com.xywg.equipmentmonitor.modular.device.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hjy
 * @date 2018/8/26
 * 按设备count实时监控各种异常次数 实体
 */
@Data
public class CountAlarmByDeviceNo {
    /**
     * 监控ID
     */
    private Integer monitorId;
    /**
     * 报警编号
     */
    private Integer alarmId ;

    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 总次数
     */
    private Integer total;
    /**
     * 温度
     */
    private Integer temperature;
    /**
     * 湿度
     */
    private Integer humidity;
    /**
     * pm2.5
     */
    private Integer pm25;
    /**
     * pm10
     */
    private Integer pm10;
    /**
     * 风速
     */
    private Integer windSpeed;
    /**
     * 噪音
     */
    private Integer noise;
    /**
     * tsp
     */
    private Integer tsp;

    private Date createTime;

    private Integer isDel;

    private String  beginTime;

    private String  endTime;
    /** 时间段内的所有年月**/
    private List<String>  months;

    public CountAlarmByDeviceNo(String deviceNo,Integer total, Integer temperature, Integer humidity, Integer pm25, Integer pm10, Integer windSpeed, Integer noise, Integer tsp) {
        this.deviceNo=deviceNo;
        this.total = total;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.windSpeed = windSpeed;
        this.noise = noise;
        this.tsp = tsp;
    }

    public CountAlarmByDeviceNo() {
    }
}
