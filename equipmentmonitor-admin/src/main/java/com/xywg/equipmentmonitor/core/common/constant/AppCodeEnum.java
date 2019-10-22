package com.xywg.equipmentmonitor.core.common.constant;

/**
 * @author liuwei
 * @date 2018/11/19 15:21
 */
public enum AppCodeEnum {
    //************************系统提醒*****************************//
    /**
     * 参数错误
     */
    PARAMS_ERROR(403, "参数错误 "),
    /**
     * 密码错误
     */
    ERROR_PASSWORD(1990, "密码错误"),
    /**
     * 用户名错误或用户不存在
     */
    USER_NOT_EXSIT(1991, "用户名错误或用户不存在"),
    /**
     * 用户不在任何公司
     */
    NO_ORG(1997, "用户没有加入任何公司"),
    /**
     * 无权限
     */
    NO_PERMISSION(1999, "暂未开通此功能"),
    //************************通用提醒*****************************//

    INSERT_SUCCESS(200, "新增成功"),

    UPDATE_SUCCESS(200, "编辑成功"),

    DELETE_SUCCESS(200, "删除成功"),

    SUBMIT_SUCCESS(200, "提交成功"),

    CANCEL_SUBMIT_SUCCESS(200, "取消成功"),

    INSERT_ERROR(300, "新增失败"),

    UPDATE_ERROR(300, "编辑失败"),

    DELETE_ERROR(300, "删除失败"),


    //************************业务提醒*****************************//

    DELETE_FAIL(300, "记录已被引用，不可删除"),

    SUBMIT_ERROR(300, "提交失败"),

    CANCEL_SUBMIT_ERROR(300, "取消失败"),

    CANCEL_SUBMIT_FAIL(300, "记录已被引用，不可取消提交"),

    NOLOGIN_ERROR(300, "请重新登录"),

    FILE_ERROR(300, "归档失败"),

    FILE_SUCCESS(200, "归档成功"),

    APPROVE_ERROR(300, "审批失败"),

    APPROVE_SUCCESS(200, "审批成功"),

    RECTIFY_ERROR(300, "整改失败"),

    RECTIFY_SUCCESS(200, "整改成功"),

    REVIEW_ERROR(300, "复查失败"),

    REVIEW_SUCCESS(200, "复查成功"),

    PERSON_EXIST(301, "该人员已处于此项目，不能重复录入"),

    PLACE_EXIST(301, "改巡检点已经存在于此项目中，不能重复录入"),

    OPERATION_SUCCESS(200, "操作成功"),

    OPERATION_FAIL(300, "操作失败"),

    ENTER_FAIL(300, "该人员已处于进场状态，不能重复进场"),

    EXIT_FAIL(300, "该人员已处于退场状态，不能重复退场"),

    ENTER_SUCCESS(200, "进场成功"),

    EXIT_SUCCESS(200, "退场成功"),

    MODEL_FAIL(300, "模板类型和部门区分不能同时重复"),

    MODELType_FAIL(300, "模板类型和部门区分和设备类型不能同时重复"),

    START_SUCCESS(200, "启用成功"),

    START_FAIL(300, "启用失败"),

    USE_SUCCESS(200, "领用成功"),

    USE_FAIL(300, "领用失败"),

    EXCHANGE_SUCCESS(200, "调拨成功"),

    EXCHANGE_FAIL(300, "调拨失败"),

    REPAIR_SUCCESS(200, "维修成功"),

    REPAIR_FAIL(300, "维修失败"),

    BIND_SUCCESS(200, "绑定成功"),

    BIND_FAIL(300, "绑定失败"),

    RETURN_SUCCESS(200, "归还成功"),

    RETURN_FAIL(300, "归还失败"),

    REPAIROVER_SUCCESS(200, "结束成功"),

    REPAIROVER_FAIL(300, "结束失败"),

	QUERYBYLABELNO_SUCCESS(200, "该标签号无绑定的巡检点"),
	
	BANGDING_FAIL(300, "抱歉，已绑定巡检点"),
	
	JIEBANG_FAIL(300, "该标签号未绑定在该巡检点上,不能被解绑"),
	
	XUNJIAN_SUCCESS(200, "新增巡检成功"),
    
	FUWU_FAIL(2000,"服务出现问题，抱歉");

    private Integer code;
    private String message;

    AppCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String msg() {
        return message;
    }

    public Integer code() {
        return code;
    }
}
