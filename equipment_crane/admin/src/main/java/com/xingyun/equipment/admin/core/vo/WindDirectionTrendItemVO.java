package com.xingyun.equipment.admin.core.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:01 2018/8/29
 * Modified By : wangyifei
 */
@Data
public class WindDirectionTrendItemVO {
    private String windDirection;
    private Double windSpeed ;
    private Date trendTime;

}
