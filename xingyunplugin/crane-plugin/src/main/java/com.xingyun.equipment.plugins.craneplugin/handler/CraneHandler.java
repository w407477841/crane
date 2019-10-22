package com.xingyun.equipment.plugins.craneplugin.handler;

import cn.hutool.json.JSONUtil;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.common.Const;
import com.xingyun.equipment.plugins.core.common.enums.LogEnum;
import com.xingyun.equipment.plugins.core.config.properties.EnviromentProperties;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.core.handler.BaseHandler;
import com.xingyun.equipment.plugins.core.libraryserver.XingyunCall;
import com.xingyun.equipment.plugins.craneplugin.common.enums.CommandEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author hjy
 * @date 2018/9/18
 * 数据处理服务
 */
@ChannelHandler.Sharable
@Slf4j
public class CraneHandler extends BaseHandler {


    public CraneHandler(CommandCallback commandCallback, EnviromentProperties properties) {
        super(commandCallback, properties);
    }

    /**收到数据后执行*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        //可读长度

        int readableLength = msg.readableBytes();
        byte[] msgData = new byte[readableLength];
        msg.readBytes(msgData);
        byte[] bytes =new byte[4096];

        log.info("开始通过JNA调用DLL解析原始数据");

        XingyunCall.EnviromentPluginService.INSTANCE.prase_packet(msgData,readableLength,bytes);
        String restDataString = new String(bytes) ;
        Map map = JSONUtil.toBean(restDataString,Map.class);
        ProtocolDTO protocolDTO;
        if(0==(int)map.get("code")){

        log.info("解析原始数据成功,data:[{}]",restDataString);
            // 成功
            Map<String,Object> data = (Map<String, Object>) map.get("data");
            protocolDTO = new ProtocolDTO();
            protocolDTO.setCommand((Integer) data.get(Const.CMD_NAME));
            protocolDTO.setSn((String) data.get(Const.SERIAL_NAME));
            protocolDTO.setData(data);
        }else{

           log.info("解析原始数据失败,data:[{}]",restDataString);

            throw  new RuntimeException(LogEnum.COMMON.format("拆包错误(",map.get("code").toString(),")"));
        }

        if(1==protocolDTO.getCommand()){
            log.info("执行登录请求,data:[{}]",restDataString);
            login(ctx,protocolDTO);
        }else{
            log.info("执行非登录请求,data:[{}]",restDataString);
            if(isLogin(ctx)){
                log.info("通过认证,即将执行请求",restDataString);
                CommandEnum.doCommand(protocolDTO,ctx,commandCallback);
            }else{
                log.info("未通过认证,即将执行请求",restDataString);
                throw  new RuntimeException("未登录");
            }
        }
    }



}
