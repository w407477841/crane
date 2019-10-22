package com.xywg.equipmentmonitor.modular.station.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:01 2019/4/10
 * Modified By : wangyifei
 */
@Data
public class CraneDeviceVo {

    private Integer id;
    private String deviceNo;

    public static CraneDeviceVo convert(Map<String,Object> map){
        CraneDeviceVo deviceVo = new CraneDeviceVo();
        deviceVo.setId((Integer) map.get("id"));
        deviceVo.setDeviceNo((String) map.get("deviceNo"));
        return deviceVo;
    }

}
