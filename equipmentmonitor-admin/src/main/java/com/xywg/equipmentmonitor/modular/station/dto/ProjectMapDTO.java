package com.xywg.equipmentmonitor.modular.station.dto;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:01 2019/3/22
 * Modified By : wangyifei
 */
@Data
public class ProjectMapDTO extends ProjectMap {


    List<FloorsDTO>  floors;

}
