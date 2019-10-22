package com.xingyun.equipment.plugins.environmentplugin;

import com.xingyun.equipment.plugins.core.action.CommandAction;
import com.xingyun.equipment.plugins.core.action.impl.SimpleCommandAction;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.config.properties.EnviromentProperties;
import com.xingyun.equipment.plugins.core.handler.BaseHandler;
import com.xingyun.equipment.plugins.core.init.InitServer;
import com.xingyun.equipment.plugins.environmentplugin.handler.CraneHandler;
import com.xingyun.equipment.plugins.environmentplugin.init.InitServerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:55 2019/7/10
 * Modified By : wangyifei
 */
@ConditionalOnClass
@Configuration
@EnableConfigurationProperties(value = {EnviromentProperties.class})
public class AutoConfigEnvironmentPlugin implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)  {
        if(AutoConfigEnvironmentPlugin.applicationContext == null) {
            AutoConfigEnvironmentPlugin.applicationContext = applicationContext;
        }
    }



    /**
     * 获取applicationContext
     */

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**通过name获取 Bean.*/
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**通过class获取Bean.*/
    public static <T> T getBean(Class<T> clazz){

        return getApplicationContext().getBean(clazz);

    }

    /**通过name,以及Clazz返回指定的Bean*/
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    @Bean
    BaseHandler handler(CommandCallback commandCallback,EnviromentProperties enviromentProperties){
        return new CraneHandler(commandCallback,enviromentProperties);
    }

    @Bean(initMethod = "open",destroyMethod = "close")
    InitServer initServer(EnviromentProperties enviromentProperties,BaseHandler handler){
        return new InitServerImpl(enviromentProperties, handler);
    }
    /** 由服务端主动调用的指令 */
    @Bean
    CommandAction commandAction(){
        return new SimpleCommandAction();
    }

}
