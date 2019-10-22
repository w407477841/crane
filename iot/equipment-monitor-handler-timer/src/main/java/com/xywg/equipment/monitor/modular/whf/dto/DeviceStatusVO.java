package com.xywg.equipment.monitor.modular.whf.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:09 2018/10/30
 * Modified By : wangyifei
 */
@Data
public class DeviceStatusVO {

    private Integer id;
    private String uuid;
    private Integer liftAmount;
    private Integer liftOff;
    private Integer liftOn;
    private Integer craneAmount;
    private Integer craneOff;
    private Integer craneOn;
    private Integer monitorAmount;
    private Integer monitorOff;
    private Integer monitorOn;


}
