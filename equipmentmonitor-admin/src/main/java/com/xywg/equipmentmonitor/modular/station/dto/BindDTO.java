package com.xywg.equipmentmonitor.modular.station.dto;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMapStation;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:40 2019/3/27
 * Modified By : wangyifei
 */
@Data
public class BindDTO {
  private String uuid;

  ProjectMap projectMap ;

  List<ProjectMapStation> mapStations;

}
