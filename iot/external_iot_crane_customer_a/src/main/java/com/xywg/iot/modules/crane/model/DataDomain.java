package com.xywg.iot.modules.crane.model;


import lombok.Data;

/**
 * @author hjy
 *  接收消息
 */
@Data
public class DataDomain {
	/**
	 * 固定头部
	 */
	private String head;
	/**
	 * 消息总长度
	 */
	private Integer length;
	/**
	 * 厂家编号
	 */
	private String vendorNumber;
	/**
	 * 协议版本
	 */
	private String protocolVersion;
	/**
	 * 消息类型
	 */
	private String commandCode;
	/**
	 * 设备编号
	 */
	private String deviceCode;
	/**
	 * 校验码
	 */
	private String checkCode;
	/**
	 * 报文结尾码
	 */
	private String endCode;
	/**
	 * 数据体
	 */
	private String dataMessage;

	public DataDomain(String head, Integer length, String vendorNumber, String protocolVersion, String commandCode, String deviceCode, String checkCode, String endCode, String dataMessage) {
		this.head = head;
		this.length = length;
		this.vendorNumber = vendorNumber;
		this.protocolVersion = protocolVersion;
		this.commandCode = commandCode;
		this.deviceCode = deviceCode;
		this.checkCode = checkCode;
		this.endCode = endCode;
		this.dataMessage = dataMessage;
	}

	public DataDomain() {
	}
}
