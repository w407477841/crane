package com.xywg.equipment.monitor.core.netty;

import io.netty.channel.Channel;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author : wangyifei
 * Description  序列化对象
 * 存储
 * Date: Created in 16:45 2018/10/23
 * Modified By : wangyifei
 */
@Data
public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);
     /**
      * 服务器编号
      * 对应消息队列中的 订阅路径
      * */
    private String sn;
    /**
     * 设备类型
     */
    private String type ;

    /**返回Session实例*/
    public static Session factory(String sn,String type){
        Session session   =  new Session();
        session.setSn(sn);
        session.setType(type);
        return session;
    }



}
