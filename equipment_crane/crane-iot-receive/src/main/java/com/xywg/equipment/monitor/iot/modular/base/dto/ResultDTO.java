package com.xywg.equipment.monitor.iot.modular.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:28 2018/8/28
 * Modified By : wangyifei
 */
@Data
public class ResultDTO implements Serializable {



    Object data ;
    int flag;

    public static ResultDTO factory(List<AlarmDTO> alarms,int flag){
        ResultDTO resultDTO = new ResultDTO();
        Map<String,Object> alarmInfo= new HashMap<>();
        alarmInfo.put("alarmInfo",alarms);
        resultDTO.setData(alarmInfo);
        resultDTO.setFlag(flag);
        return resultDTO;
    }

}
