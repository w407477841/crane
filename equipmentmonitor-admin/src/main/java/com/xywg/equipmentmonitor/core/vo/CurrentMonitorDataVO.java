package com.xywg.equipmentmonitor.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:32 2018/8/29
 * Modified By : wangyifei
 */
@Data
public class CurrentMonitorDataVO implements Serializable {
    Integer flag;
    String deviceNo;
    Double pm25;
    Double pm10;
    Double temperature;
    Double humidity;
    Double windSpeed;
    Double windForce;
    String windDirection;
    Double noise;
    String status;
    Date deviceTime;

}
