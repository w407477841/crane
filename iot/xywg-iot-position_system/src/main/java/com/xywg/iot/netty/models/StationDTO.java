package com.xywg.iot.netty.models;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:22 2019/3/29
 * Modified By : wangyifei
 */
@Data
public class StationDTO {

    private double x;
    private double y;

    private String stationNo;

}
