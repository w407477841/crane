package com.xywg.equipment.monitor.core.netty;

import io.netty.channel.Channel;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


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
    private List<String> serverNo = new ArrayList<>(10);

    private String type;

    /**上次通信时间*/
    private long lastCommunicateTimeStamp = 0L;
    /**返回Session实例*/
    public static Session factory(Session session,String serverNo,String type){
        if(session == null) {
            session = new Session();
        }
        session.getServerNo().add(serverNo);
        session.setType(type);
        return session;
    }



}
