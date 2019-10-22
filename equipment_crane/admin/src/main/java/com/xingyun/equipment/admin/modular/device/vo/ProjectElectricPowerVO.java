package com.xingyun.equipment.admin.modular.device.vo;


import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPower;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*Description:
*Company:星云网格
*@author hy
*@date 2018年8月22日 
*/

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectElectricPowerVO extends ProjectElectricPower{

	
  private String projectName;
  
  private String statusName;
  
  private Double currentDegree;
  
  private String deviceTime;
  
  private Double electricQuantity;
}
