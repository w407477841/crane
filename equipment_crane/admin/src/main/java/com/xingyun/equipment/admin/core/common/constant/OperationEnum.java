package com.xingyun.equipment.admin.core.common.constant;

public enum OperationEnum {

	INSERT_SUCCESS("新增成功"),
	UPDATE_SUCCESS("编辑成功"),
	DELETE_SUCCESS("删除成功"), 
	SUBMIT_SUCCESS("提交成功"),
	CANCEL_SUBMIT_SUCCESS("取消成功"),
	INSERT_ERROR("新增失败"), 
	UPDATE_ERROR("编辑失败"), 
	DELETE_ERROR("删除失败"), 
	DELETE_FAIL("记录已被引用，不可删除"),
	SUBMIT_ERROR("提交失败"),
	CANCEL_SUBMIT_ERROR("取消失败"),
	CANCEL_SUBMIT_FAIL("记录已被引用，不可取消提交"),
	NOLOGIN_ERROR("请重新登录"),
	FILE_ERROR("归档失败"),
	FILE_SUCCESS("归档成功"),
	APPROVE_ERROR("审核失败"),
	APPROVE_SUCCESS("审核成功"),
	SEND_SUCCESS("发送成功"),
	SEND_ERROR("发送失败"),
	CLOSE_FAIL("停用失败"),
	CLOSE_SUCCESS("停用成功"),
	CHANGE_FAIL("修改失败"),
	CHANGE_SUCCESS("修改成功"),
	START_FAIL("启用失败"),
	START_SUCCESS("启用成功");
	

	private String message;

	private OperationEnum(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
