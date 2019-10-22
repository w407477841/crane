package com.xywg.equipmentmonitor.modular.device.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:30 2019/5/6
 * Modified By : wangyifei
 */
@Data
public class CraneAPIVO {


    Integer dataStatus;

    /**
     * 设备编号
     */

    private String deviceNo;

    /**
     * 时间
     */

    private Date deviceTime;

    /**
     * 重量
     */

    private BigDecimal weight;
    /**
     * 力矩
     */

    private BigDecimal moment;

    private BigDecimal momentPercentage ;

    /**
     * 高度
     */

    private BigDecimal height;
    /**
     * 幅度
     */

    private BigDecimal range;
    /**
     * 力矩百分比
     */

    /**
     * 回转角度
     */

    private BigDecimal rotaryAngle;
    /**
     * 倾斜角度
     */

    private BigDecimal tiltAngle;
    /**
     * 风速
     */

    private BigDecimal windSpeed;
    //姓名
    private String key1;
    //id
    private String key2;
    //身份证
    private String key3;
}
