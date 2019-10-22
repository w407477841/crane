package com.xingyun.equipment.admin.modular.device.vo;


import com.xingyun.equipment.admin.modular.device.model.ProjectWaterMeter;

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
