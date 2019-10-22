package com.xywg.equipment.monitor.iot.netty.device.dto;

import com.xywg.equipment.monitor.iot.modular.crane.dto.CurrentCraneData;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:42 2019/7/18
 * Modified By : wangyifei
 */
@Data
public class WSDetailDTO {

    private String uuid;
    private String type;
    private String sn;
    private String projectId;
    private CurrentCraneData currentCraneData;

}
