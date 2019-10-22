package com.xywg.equipment.monitor.modular.whf.decoder;

import com.xywg.equipment.monitor.modular.whf.dto.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xywg.equipment.monitor.core.util.OriDataUtil.SJJ_LOGGER;
import static com.xywg.equipment.monitor.core.util.OriDataUtil.TD_LOGGER;
import static com.xywg.equipment.monitor.core.util.OriDataUtil.YC_LOGGER;

/**
 * @author : wangyifei
 * Description  用于解析 王海飞牌子的三个设备
 * Date: Created in 8:14 2018/10/19
 * Modified By : wangyifei
 */
public class WhfDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhfDecoder.class);

    /**数据最小长度   目前做小情况为  ycht:编号 */
    private static final int MIN_LENGTH = 16;
    /**数据最大长度   */
    private static final int MAX_LENGTH = 256;

    /**扬尘 数据头*/
    private  static final String  HEAD_YC_DATA = "sdsyr";

    /**扬尘 数据正则*/
    private static final String PATTERN_YC_DATA  ="(sdsyr:\\d{11},a:\\d{3},b:\\d{3},c:\\d+\\.\\d+,d:\\d+\\.\\d+,e:\\d{3},f:(\\d*)?,g:(\\d*)?,h:\\d+,i:\\d+,j:\\d+\\.\\d+,k:\\d{3},l:\\d+,m:\\d+)(\\S*)";
    /**扬尘 心跳头 */
    private static final String HEAD_YC_HT = "ycht";
    /** 扬尘 心跳正则*/
    private static final String PATTERN_YC_HT = "(ycht:\\d{11})(\\S*)?";
    /**升降机 数据头*/
    private static final String HEAD_SJJ_DATA = "dltysjj";
    /**升降机 数据正则*/
    private static final String PATTERN_SJJ_DATA  ="(dltysjj:\\d{11},a:\\d{2}\\.\\d{2},b:\\d{3}\\.\\d{2},c:\\d{2}\\.\\d{2},d:\\d,e:\\d,f:\\d,g:\\d,h:\\d,i:\\d,j:\\d,k:\\d,l:\\d,m:\\d)(\\S*)";

    /**升降机 心跳头 */
    private static final String HEAD_SJJ_HT = "sjjht";
    /** 升降机 心跳正则*/
    private static final String PATTERN_SJJ_HT = "(sjjht:\\d{11})(\\S*)?";

    /**塔基 数据头*/
    private static final String HEAD_TJ_DATA = "dltytj";
    /**塔基 数据正则*/
    //private static final String PATTERN_TJ_DATA  ="(dltytj:\\d{11},a:\\d{2}\\.\\d{2},b:\\d{3}\\.\\d{2},c:\\d{3}\\.\\d{2},d:\\d{3}\\.\\d{2},e:\\d{3}\\.\\d{2},f:\\d,g:\\d{3}\\.\\d{2},h:\\d{3}\\.\\d{3},i:\\d{2},j:\\d,k:\\d,l:\\d,m:\\d)(\\S*)";
    private static final String PATTERN_TJ_DATA  ="(dltytj:\\d{11},a:\\d{0,5}\\.?\\d{0,5},b:\\d{0,5}\\.?\\d{0,5},c:\\d{0,5}\\.?\\d{0,5},d:\\d{0,5}\\.?\\d{0,5},e:\\d{0,5}\\.?\\d{0,5},f:\\d{0,5},g:\\d{0,5}\\.?\\d{0,5},h:\\d{0,5}\\.?\\d{0,5},i:\\d{0,5},j:\\d{0,5},k:\\d{0,5},l:\\d{0,5},m:\\d{0,5})(\\S*)";
    /**升降机 心跳头 */
    private static final String HEAD_TJ_HT = "qzjht";
    /** 升降机 心跳正则*/
    private static final String PATTERN_TJ_HT = "(qzjht:\\d{11})(\\S*)?";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();
        if(len<MIN_LENGTH){
            //小于最小长度  ， 直接退出
            return ;
        }
        if(len>MAX_LENGTH){
            //大于最大长度 ， 直接去除
            in.skipBytes(len);
            return ;
        }
        //  可读开始位置
        int start  =  in.readerIndex();

        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        String data  = new String(msgData);
        LOGGER.info("#########原始数据##########");
        LOGGER.info(data);


        if(data.startsWith(HEAD_YC_DATA)){
            YC_LOGGER.info(data);
            YcDataDTO ycDataDTO  =new YcDataDTO();
            processor(PATTERN_YC_DATA,start,data,1,4,in,out,ycDataDTO);
        }else if(data.startsWith(HEAD_YC_HT)){
            YcHtDTO ycHtDTO  = new YcHtDTO();
            processor(PATTERN_YC_HT,start,data,1,2,in,out,ycHtDTO);
        }else if(data.startsWith(HEAD_SJJ_DATA)){
            SJJ_LOGGER.info(data);
            SjjDataDTO sjjDataDTO = new SjjDataDTO();
            processor(PATTERN_SJJ_DATA,start,data,1,2,in,out,sjjDataDTO);
        }else if(data.startsWith(HEAD_SJJ_HT)){
            SjjHtDTO sjjHtDTO =new SjjHtDTO();
            processor(PATTERN_SJJ_HT,start,data,1,2,in,out,sjjHtDTO);
        }else if(data.startsWith(HEAD_TJ_DATA)){
            TD_LOGGER.info(data);
            TjDataDTO tjDataDTO  =new TjDataDTO();
            processor(PATTERN_TJ_DATA,start,data,1,2,in,out,tjDataDTO);
        }else if(data.startsWith(HEAD_TJ_HT)){
            TjHtDTO tjHtDTO   = new TjHtDTO();
            processor(PATTERN_TJ_HT,start,data,1,2,in,out,tjHtDTO);
        }else{
            LOGGER.info("############异常数据，移动可读下标到结尾############");
            in.readerIndex(start+len);
        }

    }

    /**
     *
     * @param regex  匹配正则
     * @param start  读通道 开始下标
     * @param data   数据
     * @param fullDataGroup 完整数据匹配组号
     * @param redundanceGroup 冗余数据匹配组号
     * @param in  缓冲区
     * @param out 返回
     */
    private void processor(String regex, int start, String data, int fullDataGroup, int redundanceGroup, ByteBuf in, List<Object> out , WhfDataBaseDTO dataBaseDTO){
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(data);

        if(matcher.find()){
            //取出原始数据
            String   fullData = matcher.group(fullDataGroup);
            LOGGER.info("############完整数据############");
            LOGGER.info(fullData);
            //交给 handler
            dataBaseDTO.setData(fullData);
            dataBaseDTO.setNow(new Date());
            dataBaseDTO.setOridata(data);
            out.add(dataBaseDTO);
            //取出 冗余数据
            String redundance = matcher.group(redundanceGroup);
            if(redundance.length()>0){
            LOGGER.info("############冗余数据，移动可读下标到完整数据结尾############");
            LOGGER.info(redundance);
            in.readerIndex(start+fullData.length());
            }
        }else{
            //未匹配到，说明是半包数据，等待下条
            LOGGER.info("############半包数据，移动可读下标到开始位置############");
            in.readerIndex(start);
        }

    }

}
