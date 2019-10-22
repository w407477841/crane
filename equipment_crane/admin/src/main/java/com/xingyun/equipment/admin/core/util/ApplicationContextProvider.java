package com.xingyun.equipment.admin.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
* @author: wangyifei
* Description:  获得spring上下文
* Date: 15:17 2018/9/13
*/
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	  /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;
	
	@Override
	public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextProvider.applicationContext = applicationContext;
	}











	
}
