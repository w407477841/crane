package com.xywg.equipmentmonitor.modular.device.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author xieshuaishuai
 * @Date Create in 2018/9/18 9:34
 */
@Data
public class TrendVO {
    private BigDecimal amount;
    private Date trendTime;
}
