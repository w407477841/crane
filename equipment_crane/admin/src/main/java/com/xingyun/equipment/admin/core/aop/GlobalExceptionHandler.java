package com.xingyun.equipment.admin.core.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xingyun.equipment.admin.core.common.constant.ResultCodeEnum;
import com.xingyun.equipment.admin.core.dto.ResultDTO;

/**
 * 
 * @author 全局异常处理
 * 
 * springsecurity 会出现  401 403 异常状态 .  此处将返回封装
 * 
 *
 */
@RestController
public class GlobalExceptionHandler implements  ErrorController {

	public static ThreadLocal<ResultCodeEnum> UNAUTHORIZED = new ThreadLocal<>();

	@Override
	public String getErrorPath() {
		return "/error";
	}
	 @RequestMapping(value = "/error")
	    public Object error(HttpServletResponse resp, HttpServletRequest req) {
		 
			 if(resp.getStatus() == HttpStatus.BAD_REQUEST.value()){
				 resp.setStatus(200);
				 return ResultDTO.factory(ResultCodeEnum.EXCEPTION);
			 }else if(resp.getStatus() == HttpStatus.UNAUTHORIZED.value()){
				 resp.setStatus(200);
				 System.out.println(UNAUTHORIZED.get().msg());
				 return  ResultDTO.factory(UNAUTHORIZED.get());
			 }else if(resp.getStatus() == HttpStatus.FORBIDDEN.value()){
				 resp.setStatus(200);
				 return  ResultDTO.factory(ResultCodeEnum.NOT_LOGIN);
			 }else{
				 resp.setStatus(200);
				 return  ResultDTO.factory(ResultCodeEnum.SYS_ERROR);
			 }
			 
	    }

	
	
}
