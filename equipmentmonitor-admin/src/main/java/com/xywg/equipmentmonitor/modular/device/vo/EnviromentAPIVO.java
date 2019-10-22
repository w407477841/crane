package com.xywg.equipmentmonitor.modular.device.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:40 2019/3/20
 * Modified By : wangyifei
 */
@Data
public class EnviromentAPIVO {
    Integer dataStatus;
    String deviceNo;
    Double pm25;
    Double pm10;
    Double temperature;
    Double humidity;
    Double windSpeed;
    Double windForce;
    String windDirection;
    Double noise;
    Date deviceTime;


}
