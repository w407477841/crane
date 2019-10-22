package com.xingyun.equipment.plugins.core.handler;

import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.common.Const;
import com.xingyun.equipment.plugins.core.common.enums.LogEnum;
import com.xingyun.equipment.plugins.core.config.properties.EnviromentProperties;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.core.pojo.XingyunSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:44 2019/7/10
 * Modified By : wangyifei
 */
@Slf4j
public abstract class BaseHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected final CommandCallback commandCallback;

    protected  final EnviromentProperties properties;

    public BaseHandler(CommandCallback commandCallback, EnviromentProperties properties) {
        this.commandCallback = commandCallback;
        this.properties = properties;
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("超时");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*写超时*/
                // System.out.println("===服务端===(WRITER_IDLE 写超时)");
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
                // System.out.println("===服务端===(ALL_IDLE 总超时)");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        log.error("异常信息:[{}]",cause.getMessage(),cause);
        ctx.close();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Const.LINK_ACCOUNT.increment();
        log.info("连接+1,当前总连接数:[{}]",Const.LINK_ACCOUNT);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Const.LINK_ACCOUNT.decrement();
        log.info("连接-1,当前总连接数:[{}]",Const.LINK_ACCOUNT);
        logout(ctx);
    }
    protected void logout(ChannelHandlerContext ctx){
        log.info("执行登出逻辑");
        XingyunSession session = ctx.channel().attr(Const.XINGYUN_SESSION_ATTRIBUTE_KEY).get();
        if(session!=null){
            log.info("当前会话已注册，需要进行注销 ,session:[{}]",session.toString());
            if(Const.CHANNEL_MAP.containsKey(session.getSn())){
                // 之前的连接还没注销
                log.info("本地MAP中存在会话，需要清除 , session:[{}]",session.toString());
                Const.CHANNEL_MAP.remove(session.getSn());
                log.info("设备-1 当前设备数:[{}]",Const.CHANNEL_MAP.size());
                log.info("执行注销回调");
                commandCallback.logoutCallback(session.getSn());
                log.info("执行注销回调完成");
            }
        }
    }
    protected void login(ChannelHandlerContext ctx, ProtocolDTO protocolDTO){
        XingyunSession session ;
        log.info("执行登录逻辑,params:[{}]",protocolDTO.toString());
        if(!isLogin(ctx)){
            //第一次
            log.info("当前会话第一次执行登录,params:[{}]",protocolDTO.toString());
            if(Const.CHANNEL_MAP.containsKey(protocolDTO.getSn())){
                log.info("该会话存在风险，需要手动注销旧会话，并结束当前会话,params:[{}]",protocolDTO.toString());
                // 之前的连接还没注销
                Const.CHANNEL_MAP.get(protocolDTO.getSn()).close();
                throw  new RuntimeException((LogEnum.COMMON.format("注销失效连接","sn:",protocolDTO.getSn())));
            }else{
                log.info("执行正常登录逻辑:[{}]",protocolDTO.toString());
                session = new XingyunSession();
                session.setSn(protocolDTO.getSn());
                ctx.channel().attr(Const.XINGYUN_SESSION_ATTRIBUTE_KEY).set(session);
                Const.CHANNEL_MAP.put(protocolDTO.getSn(),ctx.channel());
                Integer projectId = commandCallback.loginCallback(protocolDTO);
                log.info("设备+1 当前设备数:[{}]",Const.CHANNEL_MAP.size());
                Map<String,Object> objectMap = new HashMap<>(16);
                objectMap.put(Const.CMD_NAME,protocolDTO.getCommand());
                objectMap.put(Const.SERIAL_NAME,protocolDTO.getSn());
                objectMap.put(Const.PROJECT_NAME,projectId);
                Const.combinationAndSend(objectMap,ctx);
            }
        }else{
            log.info("当前会话已执行过登录[{}]",protocolDTO.toString());
        }
        log.info("执行登录逻辑完毕[{}]",protocolDTO.toString());
    }

    protected boolean isLogin(ChannelHandlerContext ctx){
        XingyunSession session = ctx.channel().attr(Const.XINGYUN_SESSION_ATTRIBUTE_KEY).get();
        if(session==null){
            return false;
        }else{
            return true;
        }
    }

}
