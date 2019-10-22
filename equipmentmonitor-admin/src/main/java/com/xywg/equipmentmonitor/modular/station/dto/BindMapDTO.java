package com.xywg.equipmentmonitor.modular.station.dto;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMapStation;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:35 2019/3/25
 * Modified By : wangyifei
 */
@Data
public class BindMapDTO {

    List<ProjectMapStation> mapStations;

}
