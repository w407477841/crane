package com.xywg.equipmentmonitor.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:49 2019/1/21
 * Modified By : wangyifei
 */
@Data
public class MonitorDetailDTO {

    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * pm10
     */
    private BigDecimal pm10;
    /**
     * pm25
     */
    private BigDecimal pm25;
    /**
     * 噪音（分贝）
     */
    private BigDecimal noise;
    /**
     * 风速（m/s)
     */
    private BigDecimal windSpeed;
    /**
     * 风向
     */
    private String windDirection;
    /**
     * 温度
     */
    private BigDecimal temperature;
    /**
     * 湿度
     */
    private BigDecimal humidity;
    /**
     * tsp
     */
    private BigDecimal tsp;
    /**
     * 风力
     */

    private BigDecimal windForce;

    private Date createTime;

    private Date deviceTime;

    private Date time;

}
