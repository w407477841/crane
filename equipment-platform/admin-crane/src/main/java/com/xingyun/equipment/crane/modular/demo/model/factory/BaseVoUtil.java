package com.xingyun.equipment.crane.modular.demo.model.factory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;




/**
 * @author jiangjintai
 * @param <V>
 *
 */
public  class BaseVoUtil {
    //T代表PO，V代表VO
    public static <T,V>  V getVo(T tb,Class<V> voClazz) throws IntrospectionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        //获取vo的全部属性值
        Field[]  fields = voClazz.getDeclaredFields();//获取所有域名
        //并创建一个VO对象
        V vo=voClazz.newInstance();

        //获取tb的全部属性名
        Field[] fieldsTb = tb.getClass().getDeclaredFields();
        List<String> fieldNameList = new ArrayList<String>();
        for(Field field : fieldsTb){
            fieldNameList.add(field.getName());
        }


        for(Field field : fields){
            //获取vo里面的写方法
            PropertyDescriptor voPropDesc=new PropertyDescriptor(field.getName(),voClazz);
            Method methodWrite =voPropDesc.getWriteMethod();
            //获取tb里面的读方法
            //如果tb里面存在Vo里面的字段值，就会自动copy
            if(fieldNameList.contains(field.getName())){
            PropertyDescriptor tbPropDesc=new PropertyDescriptor(field.getName(),tb.getClass());
            Method methodRead =tbPropDesc.getReadMethod();
            methodWrite.invoke(vo,methodRead.invoke(tb));
            }

        }
        //返回一个VO
        return vo;
    }
}