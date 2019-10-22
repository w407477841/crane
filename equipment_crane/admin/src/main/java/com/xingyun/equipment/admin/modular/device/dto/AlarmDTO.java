package com.xingyun.equipment.admin.modular.device.dto;

import lombok.Data;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年12月18日 
*/
@Data
public class AlarmDTO {

	/**
	 * 报警数量
	 */
	private Integer alarmAmount;
	/**
	 * 预警数量
	 */
	private Integer warningAmount;
	/**
	 * 违章数量
	 */
	private Integer peccancyAmount;
}
