package com.xywg.equipmentmonitor.core.exception;

/**
 * 封装guns的异常
 *
 * @author wangcw
 * @Date 2017/12/28 下午10:32
 */
public class XywgException extends RuntimeException {

    private Integer code;

    private String message;

    public XywgException(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.message = serviceExceptionEnum.getMessage();
    }
    
    public XywgException(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
