package com.xywg.equipmentmonitor.modular.device.vo;

import java.util.List;

import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年8月22日 
*/

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectEnvironmentMonitorVO extends ProjectEnvironmentMonitor{

	
  private String projectName;
  
  private String statusName;
	
  private List<ProjectEnvironmentMonitorAlarm> infoList;
}
