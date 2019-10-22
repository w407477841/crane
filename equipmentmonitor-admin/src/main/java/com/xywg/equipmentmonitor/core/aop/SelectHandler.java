package com.xywg.equipmentmonitor.core.aop;

import java.util.Arrays;
import java.util.List;

import com.xywg.equipmentmonitor.core.common.constant.Const;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.xywg.equipmentmonitor.config.properties.CacheProperties;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;
import com.xywg.equipmentmonitor.core.security.vo.UserVO;

@Aspect
@Component
public class SelectHandler {
	@Autowired
	SecurityService securityService;

	@Around("execution(* com.xywg.equipmentmonitor.modular.*.controller..select*(..)) ")
	public Object aroundMethod(ProceedingJoinPoint pjd) {
		List<Object> args = Arrays.asList(pjd.getArgs());
		RequestDTO request = (RequestDTO) args.get(0);
		//设置当前选择的组织
		request.setOrgId(Const.orgId.get());
		//设置当前选择的组织树
		request.setOrgIds(Const.orgIds.get());
		Object result= null;
		try {
			result = pjd.proceed(new Object[]{request});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
