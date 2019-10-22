package com.xingyun.equipment.admin.core.common.constant;

/**
 * 
 * @date 2018年8月2日
 * @company jsxywg
 */
public enum ResultCodeEnum {
    /** 成功 */
    SUCCESS(200, "操作成功"),
    
    /** 没有登录 */
    FAIL(300, "操作失败"),

    /** 没有登录 */
    NOT_LOGIN(400, "请重新登录"),

    /** 发生异常 */
    EXCEPTION(401, "发生异常"),

    /** 系统错误 */
    SYS_ERROR(402, "系统错误"),

    /** 参数错误 */
    PARAMS_ERROR(403, "参数错误 "),
    /** 密码错误*/
    UNAUTHORIZED(404, "密码错误 "),
    /**用户名错误或用户名不存在*/
    NOUSER(405, "用户名错误或用户名不存在 "),
    /** 不支持或已经废弃 */
    NOT_SUPPORTED(410, "不支持或已经废弃"),

    /** AuthCode错误 */
    INVALID_AUTH_CODE(444, "无效的AuthCode"),

    /** 太频繁的调用 */
    TOO_FREQUENT(445, "太频繁的调用"),

    /** 未知的错误 */
    UNKNOWN_ERROR(499, "未知错误"),
    
    /** 无权限 */
    NO_PERMISSION(1999, "暂未开通此功能"),
    
    /** 录入的用户手机号或者登陆名已经存在*/
    HAS_USER(1998, "手机号或者登陆名已经存在"),
    
    /** 用户不在任何公司*/
    NO_ORG(1997, "用户没有加入任何公司"),

    
    /** 不能删除主公司*/
    NO_DELORG(1996, "不能删除主公司"),
    
    /** 不能删除管理员*/
    NO_DELMANAGGER(1995, "不能删除管理员"),
    
    /** 不能删除管理员*/
    HAS_PERMISSION(1994, "已经存在该权限字符"),
    
    /** 不能删除管理员*/
    NO_DATASHOW(1993, "没有浏览权限"),
    
    /** 被踢出下线*/
    HAS_KICK_OUT(1992, "被迫下线,请重新登录"),

    /**需要选择组织*/
    SELECT_ORG(1890,"请选择一个组织"),


    /** 数据字典设备类型名称重复*/
    DICT_SERVICE_TYPE_NAME_REPEAT(2001, "名称重复"),

    /** 数据字典设备类型名称为空*/
    DICT_SERVICE_TYPE_NAME_BLANK(2002, "数据字典设备类型名称为空"),

    PROJECT_NAME_REPEAT(2003,"工程名称重复"),

    LOGIN_NAME_REPEAT(2005,"登录名重复"),

    PASSWORD_WRONG(2006,"原密码错误"),

    PHONE_REPEAT(2007,"手机号重复"),

    STANDARD_REPEAT(2008,"规格型号重复"),

    PROJECT_DEVICE_NO_REPEAT(2009,"工程名称和设备编号不能同时重复"),

    PROJECT_DEVICE_STRAT_REPEAT(2010,"相同设备编号的设备不能同时启用"),

    PROJECT_UUID_EXIST(2011,"uuid已存在"),

    PROJECT_TOPIC_NOT_EXIST(2012,"topic不存在"),

    APP_NAME_REPEAT(2013,"平台名称重复"),

    USER_IDENTITY_CODE_REPEAT(2004,"身份证号重复");

     ResultCodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    private Integer code;
    private String msg;
}
