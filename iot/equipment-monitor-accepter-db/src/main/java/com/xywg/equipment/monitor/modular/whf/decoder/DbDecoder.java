package com.xywg.equipment.monitor.modular.whf.decoder;

import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.modular.whf.dto.*;
import com.xywg.equipment.monitor.modular.whf.init.DbChannelInit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channels;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xywg.equipment.monitor.core.util.OriDataUtil.DB_LOGGER;

/**
 * @author : wangyifei
 * Description  用于解析 王海飞牌子的三个设备
 * Date: Created in 8:14 2018/10/19
 * Modified By : wangyifei
 */
public class DbDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbDecoder.class);
     //FEFEFEFE6803000206182068910833333333574C34338216    48
    //
    //201806020003  12
    /**
     *  连接上报数据长度
     *  设备连接后上报的数据
     * */
    public static final int LINK_SN_LENGTH = 12;
    /**数据长度   */
    private static final int DATA_LENGTH = 48;



    /**电表数据正则*/
    private static final String PATTERN_DB_DATA  ="^(FE)*68(\\w{12})68(\\w{22})16(FE)*$";



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)  {
        // 得到 下位机发送的16进制数据包
        String data = ByteBufUtil.hexDump(in).toUpperCase();
        in.skipBytes(in.readableBytes());
        LOGGER.info("#########解析开始##########");
        DB_LOGGER.info("#########原始数据 :<"+data+">##########");
        int validata = validate(data);
        if(validata<0){
            //数据不符合
            LOGGER.error("##########数据异常 异常信息["+validata+"]，服务器将断开此连接#########");
            ctx.close();
            return ;
        }
        if(validata==1){
            // 初次连接
            LOGGER.info("####设备号，将交给'连接号'处理器处理####");
            LinkSNDTO linkSNDTO =  LinkSNDTO.factory(data);
            out.add(linkSNDTO);
        }else{
            //数据
            LOGGER.info("####设备数据，将交给'数据'处理器处理####");
            DbDataDTO  dbDataDTO =  DbDataDTO.factory(StrUtil.sub(data,36,44),StrUtil.sub(data,10,22));
            out.add(dbDataDTO);
        }
        LOGGER.info("#########解析完成##########");

    }

    /**
     *  判断是什么数据
     *  -1 符合设备号长度，但已连接过
     *  -2 数据crc校验失败
     *  -3 数据不符合正则表达式
     *  -4 长度非法
     *  0 数据
     *  1 初次连接
     * @param data
     * @return
     */
    private int validate(String data){
        if(data.length()==DATA_LENGTH) {
            //数据长度
            if(Pattern.matches(PATTERN_DB_DATA,data)){
                int startIndex = data.indexOf("68");
                int endIndex =data.lastIndexOf("16");
                data.substring(startIndex,endIndex);
                String crc = data.substring(endIndex-2,endIndex);
                String toCrc = data.substring(startIndex,endIndex-2);

                String crcStr =getCS(toCrc).toUpperCase();
                if(!crc.equals(crcStr)){
                    return -2;
                }
            }else{
                //判断 本地有没有存储该设备号
                int snAcount = data.length()/LINK_SN_LENGTH;
                for(int i = 0; i<snAcount; i ++){
                    if(DbChannelInit.channels.containsKey(StrUtil.sub(data,i*LINK_SN_LENGTH,(i+1)*LINK_SN_LENGTH))){
                        return -1 ;
                    }
                }
                return 1;
            }
        }else if(data.length()!=0&&data.length() % LINK_SN_LENGTH == 0){
            //可能是初始连接 上报的设备号
            //判断 本地有没有存储该设备号
             int snAcount = data.length()/LINK_SN_LENGTH;
             for(int i = 0; i<snAcount; i ++){
                 if(DbChannelInit.channels.containsKey(StrUtil.sub(data,i*LINK_SN_LENGTH,(i+1)*LINK_SN_LENGTH))){
                     return -1 ;
                 }
             }
            return 1;
        }else{
            //非法
            return -4;
        }

    return 0;

    }
    /**
     * 获取偶校验CS的值
     * @return
     */
    private static String getCS(String str){
        int sum = 0;
        for (int i = 0; i < str.length()/2; i++) {
            sum += Integer.parseInt(str.substring(i*2,(i+1)*2),16)%256;
        }
        String kou = Integer.toHexString(sum);
        return kou.substring(kou.length()-2,kou.length());
    }

public static void main (String []args){

        String data= "6803000206182068910833333333574C3433";
        System.out.println(getCS(data));


}
}
