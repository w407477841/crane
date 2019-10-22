package com.xywg.equipmentmonitor.modular.device.vo;


import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*Description:
*Company:星云网格
*@author changmengyu
*@date 2018年8月22日 
*/

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectWaterMeterInfoVo extends ProjectWaterMeter{

	
  private String projectName;
  
  private String statusName;
}
