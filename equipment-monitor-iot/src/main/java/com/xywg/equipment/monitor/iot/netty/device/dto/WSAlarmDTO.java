package com.xywg.equipment.monitor.iot.netty.device.dto;

import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:42 2019/7/18
 * Modified By : wangyifei
 */
@Data
public class WSAlarmDTO {

    private String uuid;
    private String type;
    private String sn;
    private String projectId;
    private ResultDTO resultDTO;

}
