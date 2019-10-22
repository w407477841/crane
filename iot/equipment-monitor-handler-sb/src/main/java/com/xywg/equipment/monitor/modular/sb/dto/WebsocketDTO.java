package com.xywg.equipment.monitor.modular.sb.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:46 2018/11/2
 * Modified By : wangyifei
 */
@Data
public class WebsocketDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketDTO.class);
    private String topic ;
    private String data ;

    public static WebsocketDTO factory(String topic,String data){

        WebsocketDTO  remoteDTO   =  new WebsocketDTO();
        remoteDTO.setData(data);
        remoteDTO.setTopic(topic);
        return remoteDTO;
    }

    @Override
    public String toString() {
        return "WebsocketDTO{" +
                "topic='" + topic + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
