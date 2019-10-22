package com.xingyun.equipment.admin.core.aop;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xingyun.equipment.admin.config.properties.XywgProperties;
import com.xingyun.equipment.admin.core.common.constant.Const;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.security.service.SecurityService;

@Aspect
@Component
@Slf4j
public class SelectHandler {
	@Autowired
	SecurityService securityService;
	@Autowired
	XywgProperties xywgProperties;

	@Around("execution(* com.xingyun.equipment.admin.modular.*.controller..select*(..)) ")
	public Object aroundMethod(ProceedingJoinPoint pjd) {
		List<Object> args = Arrays.asList(pjd.getArgs());
		RequestDTO request = (RequestDTO) args.get(0);
		//设置当前选择的组织
		request.setOrgId(Const.orgId.get());
		//设置当前选择的组织树
		request.setOrgIds(Const.orgIds.get());


		// 系统开始时间
		if(StrUtil.isBlank(request.getStartTime())){
			log.info("重置开始时间：{}",xywgProperties.getInitStartTime());
			request.setStartTime(xywgProperties.getInitStartTime());
		}else{
			if(DateUtil.parse(xywgProperties.getInitStartTime()).getTime()>DateUtil.parse(request.getStartTime()).getTime()){
				log.info("重置开始时间：{}",xywgProperties.getInitStartTime());
				request.setStartTime(xywgProperties.getInitStartTime());
			}
		}
		// 系统开始结束
		if(StrUtil.isBlank(request.getEndTime())){
			log.info("重置结束时间：{}",xywgProperties.getInitEndTime());
			request.setEndTime(xywgProperties.getInitEndTime());
		}else{
			if(DateUtil.parse(xywgProperties.getInitEndTime()).getTime()<DateUtil.parse(request.getEndTime()).getTime()){
				log.info("重置结束时间：{}",xywgProperties.getInitEndTime());
				request.setEndTime(xywgProperties.getInitEndTime());
			}
		}

		Object result= null;
		try {
			result = pjd.proceed(new Object[]{request});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
