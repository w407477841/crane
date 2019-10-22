package com.xywg.iot.netty.server.impl;

import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 回复重启
 * Date: Created in 15:17 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class RestartAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartAction.class);


    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("回复重启");
        print(dataDomain);
        validLogin(ctx);
    }

    @Override
    public boolean supports(String code) {
        return "000A".equals(code);
    }
}
