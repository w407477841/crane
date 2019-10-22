package com.xywg.iot.modules.netty.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.xywg.iot.common.global.GlobalStaticConstant.PROTOCOL_FIXED_HEAD;
import static com.xywg.iot.common.global.GlobalStaticConstant.PROTOCOL_FIXED_HEAD_INTEGER;

/**
 * @author hjy
 * @date 2018/12/27
 * 解码器
 */
public class CraneDecoder extends ByteToMessageDecoder {
    /**
     * 最小长度
     */
    public final int baseLength = 2 + 1 + 1 + 1 + 1 + 4 + 1 + 4;
    public final int maxLength = 2048;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer, List<Object> out) {
       // 可读长度必须大于基本长度 readableBytes() 表示 ByteBuf 当前可读的字节数
        if (buffer.readableBytes() >= baseLength) {
            // 防止socket字节流攻击,防止客户端传来的数据过大,因为太大的数据是不合理的
            if (buffer.readableBytes() > maxLength) {
                buffer.skipBytes(buffer.readableBytes());
            }
            // 记录包头开始的index
            int beginReader;
            while (true) {
                // readerIndex()表示返回当前的读指针   readerIndex(x)表示设置指针在X位置
                beginReader = buffer.readerIndex();
                // markReaderIndex() 表示把当前的读指针保存起来
                buffer.markReaderIndex();
                //readInt() 返回当前索引的位置+2 的值  读到了协议的开始标志，结束while循环
                if (buffer.readShort() == PROTOCOL_FIXED_HEAD_INTEGER) {
                    break;
                }
                // 表示把当前的读指针恢复到之前保存的值
                buffer.resetReaderIndex();
                //readByte()表示从 ByteBuf 中读取一个字节
                buffer.readByte();
               // readBytes() 指的是读取ByteBuf中的全部数据
                if (buffer.readableBytes() < baseLength) {
                    return;
                }
            }

            // readByte()表示从 ByteBuf 中读取一个字节  这里的length是协议中的总长度
            int length = buffer.readByte();
            // 判断请求数据包数据是否到齐
            if (buffer.readableBytes() < length) {
                //readerIndex()表示返回当前的读指针
                buffer.readerIndex(beginReader);
                return;
            }

            // 读取data数据
            byte[] data = new byte[length];
            //返回当前索引的(无符号)字节，读索引加一
            buffer.readBytes(data);
            out.add(data);
        }
    }
}
