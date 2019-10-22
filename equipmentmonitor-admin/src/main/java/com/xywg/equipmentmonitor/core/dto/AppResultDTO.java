package com.xywg.equipmentmonitor.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResultDTO<T> {
	/**
	 * 状态码
	 */
	private Integer code;
	/**
	 * message
	 */
	private String message;


	public AppResultDTO() {
	}

	public AppResultDTO(Boolean success) {
		this(success, null);
	}




	public AppResultDTO(Boolean success,  String message) {
		this.code = success ? ResultCodeEnum.SUCCESS.code() : ResultCodeEnum.FAIL.code();
		this.message = message == null ? success ? ResultCodeEnum.SUCCESS.msg() : ResultCodeEnum.FAIL.msg() : message;
		// this.total = total;
	}




	


}
