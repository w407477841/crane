package com.xingyun.equipment.admin.core.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xingyun.equipment.admin.core.common.constant.ResultCodeEnum;
import com.xingyun.equipment.admin.core.common.exception.ForbiddenException;
import com.xingyun.equipment.admin.core.dto.ResultDTO;

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
