package com.xywg.equipmentmonitor.modular.station.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:01 2019/4/10
 * Modified By : wangyifei
 */
@Data
public class LiftDeviceVo {

    private Integer id;
    private String deviceNo;

    public static LiftDeviceVo convert(Map<String,Object> map){
        LiftDeviceVo deviceVo = new LiftDeviceVo();
        deviceVo.setId((Integer) map.get("id"));
        deviceVo.setDeviceNo((String) map.get("deviceNo"));
        return deviceVo;
    }

}
