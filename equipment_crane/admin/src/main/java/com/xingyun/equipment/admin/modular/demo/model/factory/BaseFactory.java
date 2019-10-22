package com.xingyun.equipment.admin.modular.demo.model.factory;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


/**
 * @author jiangjintai
 *
 */
public abstract class BaseFactory<T,V extends Object> {
    private T tb;
    private Class<V> clazz;
    private V vo;

    /**
     * 构造时需要传入PO，与VO的class
     * @param t
     * @param clazz
     * @throws InstantiationException
     * @throws IllegalAccessException
     */

    public BaseFactory(T t ,Class<V> clazz) throws InstantiationException, IllegalAccessException {
        this.clazz = clazz;
        this.tb=t;
    }

    /**
     * 方便复用
     */
    public void setTb(T tb){
        this.tb=tb;
    }

    /**
     * 调用该方法造一个VO
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    public V build() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
        //普通字段copy
        vo = BaseVoUtil.getVo(getTb(), clazz);
        //特殊字段注入
        doOrderThingForVo(vo);
        return vo;
    }

    /**
     * 把需要处理的特殊字段交给子类
     * @param vo
     */

    protected abstract void doOrderThingForVo(V vo);

    /**
     *
     * @return
     */
    protected T getTb(){
        return this.tb;
    }
}