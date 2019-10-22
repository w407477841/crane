package com.xingyun.equipment.plugins.core.bootstrap;


import com.xingyun.equipment.plugins.core.config.properties.EnviromentProperties;
import com.xingyun.equipment.plugins.core.handler.BaseHandler;

/**
 * 启动类接口
 *
 * @author lxr
 * @create 2017-11-18 14:05
 **/
public interface BootstrapServer {

    /**
     *  关闭
     */
    void shutdown();

    /**
     *  初始化参数
      * @param serverBean
     */
    void setServerBean(EnviromentProperties serverBean);

    /**
     * 处理类
     * @param handler
     */
    void setHandler(BaseHandler handler);
    /**
     *  启动
     */
    void start();


}
