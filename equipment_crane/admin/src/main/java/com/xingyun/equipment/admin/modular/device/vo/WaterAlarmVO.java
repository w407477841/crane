package com.xingyun.equipment.admin.modular.device.vo;

import java.util.Date;
import java.util.List;



import lombok.Data;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年9月4日 
*/
@Data
public class WaterAlarmVO {

	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 报警时间
	 */
	
	private Date deviceTime;
	/**
	 * 报警次数
	 */
	private Integer amount;
	
	private String status;
	
	private Integer alarmId;
	
	private String userName;
	
	private Date modifyTime;
	
	private String deviceNo;
	

	
	
	/**
	 * 报警信息
	 */
	private String info;
	
	private List<WaterAlarmVO> infoList;
}
