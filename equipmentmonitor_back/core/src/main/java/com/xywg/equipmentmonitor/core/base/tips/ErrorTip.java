package com.xywg.equipmentmonitor.core.base.tips;

import com.xywg.equipmentmonitor.core.exception.ServiceExceptionEnum;

/**
 * 返回给前台的错误提示
 *
 * @author wangcw
 * @date 2016年11月12日 下午5:05:22
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
    
    public ErrorTip(ServiceExceptionEnum serviceExceptionEnum) {
    	super();
    	this.code = serviceExceptionEnum.getCode();
    	this.message = serviceExceptionEnum.getMessage();
    }
}
