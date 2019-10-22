package com.xingyun.equipment.admin.core.aop;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.xingyun.equipment.admin.core.common.constant.Const;
import org.springframework.stereotype.Component;

/**
* 自动填充属性
* @author: wangyifei
* Description:
* Date: 15:29 2018/8/30
*/
public class MyMetaObjectHandler extends MetaObjectHandler {
	
	@Override
	public void insertFill(MetaObject metaObject) {

		Field field = null;
		try {
			 field = metaObject.getOriginalObject().getClass().getDeclaredField("orgId");

		}catch (Exception e) {

		}

		if(field != null){
			Object orgId = getFieldValByName("orgId", metaObject);
			if(null== orgId){
				setFieldValByName("orgId", Const.orgId.get(), metaObject);
			}
		}



		Object isDel =  metaObject.getValue("isDel");
		Object createTime = metaObject.getValue("createTime");
		Object createtUser  =metaObject.getValue("createUser");
		if(null == isDel ){
			setFieldValByName("isDel", 0,metaObject);
		}
		if(null==createTime){
			setFieldValByName("createTime", new Date(),metaObject);
		}
		if(null == createtUser){
			if(Const.currUser.get()!=null){
				setFieldValByName("createUser",  Const.currUser.get().getId(),metaObject);
			}

		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Object modifyTime = getFieldValByName("modifyTime", metaObject);
        Object modifyUser = getFieldValByName("modifyUser", metaObject);



            setFieldValByName("modifyTime", new Date(System.currentTimeMillis()), metaObject);

			if(Const.currUser.get()!=null){
				setFieldValByName("modifyUser", Const.currUser.get().getId(), metaObject);
			}


		
	}

}
