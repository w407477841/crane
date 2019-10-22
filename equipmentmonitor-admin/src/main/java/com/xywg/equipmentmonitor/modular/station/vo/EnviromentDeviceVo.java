package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:04 2019/4/10
 * Modified By : wangyifei
 */
@Data
public class EnviromentDeviceVo {

    private Integer id;
    private String deviceNo;

    public static EnviromentDeviceVo convert(Map<String,Object> map){
        EnviromentDeviceVo deviceVo = new EnviromentDeviceVo();
        deviceVo.setId((Integer) map.get("id"));
        deviceVo.setDeviceNo((String) map.get("deviceNo"));
        return deviceVo;
    }

}
