package com.xywg.attendance.modular.netty;

import com.xywg.attendance.modular.netty.handler.AttendanceInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author hjy
 */
@Component
public class ChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    private AttendanceInboundHandler attendanceInboundHandler;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // HTTP协议的请求解码器和响应编码器
        pipeline.addLast(new HttpServerCodec());
        // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(10*1024*1024));
        // 处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的
        pipeline.addLast(new ChunkedWriteHandler());

        // 具体处理器
        pipeline.addLast(attendanceInboundHandler);
    }
}
