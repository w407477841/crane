package com.xywg.iot.netty.zbus;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:56 2019/4/17
 * Modified By : wangyifei
 */
@Data
public class WebsocketDTO {

    private String data;
    private String topic;

    public WebsocketDTO(String data, String topic) {
        this.data = data;
        this.topic = topic;
    }
}
