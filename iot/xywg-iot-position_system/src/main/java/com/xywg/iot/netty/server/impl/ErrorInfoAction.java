package com.xywg.iot.netty.server.impl;

import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 错误信息
 * Date: Created in 15:23 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class ErrorInfoAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorInfoAction.class);

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("错误信息");
        print(dataDomain);
        validLogin(ctx);
        //TODO 业务逻辑
        PositionHandler.responseErrorMessageHandle(ctx,dataDomain.getOri(),"00","00");

    }

    @Override
    public boolean supports(String code) {
        return "0009".equals(code);
    }
}
