package com.xingyun.equipment.crane.modular.device.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author xieshuaishuai
 * @Date Create in 2018/9/17 16:52
 */
@Data
public class EnvironmentDeviceVO {
    private Integer flag;
    private String deviceNo;
    private BigDecimal pm25;
    private BigDecimal pm10;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal windSpeed;
    private String windDirection;
    private BigDecimal noise;
    private BigDecimal tsp;
    private BigDecimal windForce;
}
