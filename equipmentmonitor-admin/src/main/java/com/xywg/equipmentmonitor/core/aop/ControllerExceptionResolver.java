package com.xywg.equipmentmonitor.core.aop;

import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
/**
 * 统一异常处理、在执行controller是发生的异常
 * @author Administrator
 *
 */

public class ControllerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
		Map map = new HashMap();
		map.put("status", 400);
		map.put("msg", ex.getMessage());
		mv.addAllObjects(map);
		response.setStatus(400);
		return mv;

}
}