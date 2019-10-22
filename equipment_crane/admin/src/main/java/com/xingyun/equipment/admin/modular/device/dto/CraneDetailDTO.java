package com.xingyun.equipment.admin.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:57 2019/1/21
 * Modified By : wangyifei
 */
@Data
public class CraneDetailDTO {
    /**
     * 设备编号
     */

    private String deviceNo;

    /**
     * 时间
     */

    private Date deviceTime;
    /**
     * 状态
     */

    private Integer status;
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

    private Date createTime;

    private Date time;


}
