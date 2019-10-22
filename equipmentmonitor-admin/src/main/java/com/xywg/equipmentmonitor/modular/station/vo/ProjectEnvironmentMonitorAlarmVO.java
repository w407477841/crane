package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:08 2019/4/13
 * Modified By : wangyifei
 */
@Data
public class ProjectEnvironmentMonitorAlarmVO extends ProjectEnvironmentMonitorAlarm {

    private double dataInfo;

}
