package com.xywg.iot.modules.helmet.model;

import lombok.Data;

/**
 *
 * @date 2018年9月20日
 *  设备编号和工人身份证对应实体
 */
@Data
public class DeviceWorker {
	private String uuid;
	private Integer helmetId;
	private String imei;
	private String idCardType;
	private String idCardNumber;

}
