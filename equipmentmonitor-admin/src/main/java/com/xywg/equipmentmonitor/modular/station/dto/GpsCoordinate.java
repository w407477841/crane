package com.xywg.equipmentmonitor.modular.station.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:59 2019/5/20
 * Modified By : wangyifei
 */
@Data
public class GpsCoordinate {

    /**
     *  精度
     */
    private  double longitude;
    /**
     * 维度
     */
    private double latitude;

    public GpsCoordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
