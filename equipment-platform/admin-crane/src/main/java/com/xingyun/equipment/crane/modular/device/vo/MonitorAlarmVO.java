package com.xingyun.equipment.crane.modular.device.vo;

import java.math.BigDecimal;
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
public class MonitorAlarmVO {

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
	
	private BigDecimal pm25;
	
	private BigDecimal pm10;
	
	private BigDecimal temperature;
	
	private BigDecimal humidity;
	
	private BigDecimal windSpeed;
	
	private String windDirection;
	
	private BigDecimal noise;
	
	private BigDecimal tsp;
	
	private BigDecimal windForce;
	
	
	/**
	 * 报警信息
	 */
	private String info;
	
	private List<MonitorAlarmVO> infoList;
}
