package com.xywg.iot.netty.model;

import com.xywg.iot.modular.station.model.ProjectMap;
import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.FilePojo;
import com.xywg.iot.netty.models.StationDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hjy
 * @date 2019/2/19
 */
@Data
public class DeviceConnectInfo {
    private String uuid;
    /**项目ID*/
    private String sn;

    private Integer stationId;
    /**文件*/
    private FilePojo file;

    public DeviceConnectInfo(String uuid, String sn) {
        this.uuid = uuid;
        this.sn = sn;
    }

    public DeviceConnectInfo() {
    }
}
