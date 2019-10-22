package com.xywg.equipmentmonitor.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:12 2018/11/26
 * Modified By : wangyifei
 */
@Data
public class HealthInfo {
    /**
     * 是否戴帽子
     */
    private int maozi;
    /**
     * 进场时间
     */
    private String inTime;
    /**
     *
     */
    private String outTime;
    /**
     * 证件编号
     */
    private String code;

    private Double temperature;
    private Double pressure;
    /**
     * 血氧浓度
     */
    private Double oxygen;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 采集时间
     */
    private String collectTime;


}
