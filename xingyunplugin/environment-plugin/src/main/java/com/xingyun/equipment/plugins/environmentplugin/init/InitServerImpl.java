package com.xingyun.equipment.plugins.environmentplugin.init;

import com.xingyun.equipment.plugins.core.bootstrap.BootstrapServer;
import com.xingyun.equipment.plugins.core.bootstrap.NettyBootstrapServer;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.config.properties.EnviromentProperties;
import com.xingyun.equipment.plugins.core.handler.BaseHandler;
import com.xingyun.equipment.plugins.core.init.InitServer;
import com.xingyun.equipment.plugins.environmentplugin.AutoConfigEnvironmentPlugin;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:44 2019/7/8
 * Modified By : wangyifei
 */
@Slf4j
public class InitServerImpl implements InitServer {


    private final EnviromentProperties properties;
    private final BaseHandler handler;
    BootstrapServer bootstrapServer;
    public InitServerImpl(EnviromentProperties properties, BaseHandler handler) {
        this.properties = properties;
        this.handler = handler;
    }

    @Override
    public void open() {
        try {
            AutoConfigEnvironmentPlugin.getBean(CommandCallback.class);
        }catch (Exception e){
            log.error("请实现 com.xingyun.equipment.enviromentplugin.netty.service.callback.CommandCallback 接口 ， 并添加@Component注解");
        }


        if(properties!=null){
            bootstrapServer = new NettyBootstrapServer();
            bootstrapServer.setServerBean(properties);
            bootstrapServer.setHandler(handler);
            bootstrapServer.start();
        }


    }

    @Override
    public void close() {
        if(bootstrapServer!=null){
            bootstrapServer.shutdown();
        }
    }
}
