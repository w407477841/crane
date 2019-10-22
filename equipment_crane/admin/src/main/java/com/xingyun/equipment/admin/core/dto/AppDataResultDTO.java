package com.xingyun.equipment.admin.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.core.common.constant.OperationEnum;
import com.xingyun.equipment.admin.core.common.constant.ResultCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppDataResultDTO<T> {
	/**
	 * 状态码
	 */
	private Integer code;
	/**
	 * message
	 */
	private String message;
	/**
	 * 总条数
	 */
	// private Long total;
	/**
	 * 数据
	 */
	private T data;

	public AppDataResultDTO() {
	}

	public AppDataResultDTO(Boolean success) {
		this(success, null);
	}

	public AppDataResultDTO(String message) {
		this(true, null, message);
	}

	public AppDataResultDTO(Boolean success, T data) {
		this(success, data, null);
	}

	public AppDataResultDTO(Boolean success, T data, String message) {
		this(success, data, message, null);
	}

	public AppDataResultDTO(Boolean success, T data, String message, Long total) {
		this.code = success ? ResultCodeEnum.SUCCESS.code() : ResultCodeEnum.FAIL.code();
		this.data = data;
		this.message = message == null ? success ? ResultCodeEnum.SUCCESS.msg() : ResultCodeEnum.FAIL.msg() : message;
		// this.total = total;
	}

	/**
	 * 静态工厂
	 *
	 * @param operation
	 * @return
	 */
	public static <T> AppDataResultDTO<T> resultFactory(OperationEnum operation) {
		AppDataResultDTO<T> dto = null;
		switch (operation) {
		case INSERT_SUCCESS:
		case UPDATE_SUCCESS:
		case SUBMIT_SUCCESS:
		case CANCEL_SUBMIT_SUCCESS:
		case DELETE_SUCCESS:
		case APPROVE_SUCCESS:
		case START_SUCCESS:
		case CLOSE_SUCCESS:
		case SEND_SUCCESS:
			dto = new AppDataResultDTO<>(true, null, operation.getMessage());
			break;
		case INSERT_ERROR:
		case UPDATE_ERROR:
		case SUBMIT_ERROR:
		case DELETE_ERROR:
		case CANCEL_SUBMIT_ERROR:
		case CANCEL_SUBMIT_FAIL:
		case DELETE_FAIL:
		case APPROVE_ERROR:
		case START_FAIL:
		case CLOSE_FAIL:
		case SEND_ERROR:
			dto = new AppDataResultDTO<>(false, null, operation.getMessage());
			break;
		default:
			new AppDataResultDTO<>(true, null);
			break;
		}
		return dto;
	}


	/**
	 * 成功
	 * @param message
	 * @return
	 */
	public static <T> AppDataResultDTO<T> success(String message) {
		AppDataResultDTO<T> dto = new AppDataResultDTO<T>(true,null,message);
		return dto;
	} 
	
	/**
	 * 失败
	 * @param message
	 * @return
	 */
	public static <T> AppDataResultDTO<T> fail(String message) {
		AppDataResultDTO<T> dto = new AppDataResultDTO<T>(false,null,message);
		return dto;
	} 

}
