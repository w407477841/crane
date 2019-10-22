package com.xywg.equipment.monitor.iot.modular.base.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:07 2018/8/26
 * Modified By : wangyifei
 */
@Data
public class TrendItem {
    /** 时间点 */
    Date trendTime;
    /**  值 */
    Double amount;
}
