package com.xywg.equipment.sandplay.nettty.weight;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:17 2018/10/11
 * Modified By : wangyifei
 */
@Component
public class WeightDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightDecoder.class);
    /**
     * 解析 形如：
     * F=0.89
     * OK
     *
     * */
    private static final String PATTERN = "F=((0)|([1-9]\\d)).\\d{2}(\\n|\\r|\\n\\r|\\r\\n)OK";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int readableLength = in.readableBytes();

        byte[] msgData = new byte[readableLength];
        in.readBytes(msgData);
        String data  = new String (msgData);
        LOGGER.info("###############进入解析#############");
        LOGGER.info("###############原始数据#############");
        LOGGER.info("###############"+data+"#############");
        if(Pattern.matches(PATTERN,data)){
            out.add(data);
        }else{
            throw new RuntimeException("数据异常"+data);
        }
    }
}
