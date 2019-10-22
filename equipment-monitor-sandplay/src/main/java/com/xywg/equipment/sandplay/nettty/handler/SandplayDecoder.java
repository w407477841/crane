package com.xywg.equipment.sandplay.nettty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : wangyifei
 * Description  解码器
 * Date: Created in 9:13 2018/9/21
 * Modified By : wangyifei
 */
@Component
public class SandplayDecoder   extends ByteToMessageDecoder {

    private static final  String START_CODE = "55";


    /**
     * 报文头固定长度10个
     */
    private static final int BASE_LENGTH = 16;


    /**
     * 一次报文信息允许的消息最大长度
     */
    private static final int MAX_LENGTH = 1024 * 2;

    private static final Logger   log = LoggerFactory.getLogger(SandplayDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        //可读长度
        int readableLength = in.readableBytes();
        //不满足读取条件
        if (readableLength < BASE_LENGTH) {
            return;
        }
        if (readableLength > MAX_LENGTH) {
            in.skipBytes(in.readableBytes());
        }

    }
}
