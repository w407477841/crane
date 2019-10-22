package com.xywg.equipment.monitor.netty;

import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.modular.disaptch.model.Dispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author 严鹏
 * @date 2019/7/29
 */
public class NettyClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientService.class);

    @Autowired
    private XywgProerties xywgProerties;

    private static NettyClient client;

    public void init(){
        client = new NettyClient(xywgProerties.getNettyHost(), xywgProerties.getNettyPort());
        try {
            new Thread(() -> {
                try {
                    client.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            LOGGER.info("netty客户端启动成功");
        } catch (Exception e) {
            LOGGER.error("netty客户端启动失败");
            e.printStackTrace();
        }
    }

    public static void sendMsg(Object obj){
        client.sendMsg(obj);
    }

}
