package com.xywg.iot.core.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hjy
 * @date 2018/11/20
 * 功能码注解
 */
//目标是方法
@Target(ElementType.METHOD)
//注解会在class中存在，运行时可通过反射获取
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketCommand {
     int command();
}
