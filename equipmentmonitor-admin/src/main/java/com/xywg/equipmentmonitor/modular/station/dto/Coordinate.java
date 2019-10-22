package com.xywg.equipmentmonitor.modular.station.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:48 2019/5/20
 * Modified By : wangyifei
 */
@Data
public class Coordinate {

    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
