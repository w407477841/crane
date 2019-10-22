package com.xywg.equipmentmonitor.core.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.common.exception.ForbiddenException;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;

/**
 * 异常拦截
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
	
	@ExceptionHandler(value=ForbiddenException.class)
	@ResponseBody
    public ResultDTO<Object> handleException(ForbiddenException e) {
		
        return ResultDTO.factory(ResultCodeEnum.NO_PERMISSION);
    }
	
	
}
