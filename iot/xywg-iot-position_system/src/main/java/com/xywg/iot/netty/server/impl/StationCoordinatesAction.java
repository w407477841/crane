package com.xywg.iot.netty.server.impl;

import cn.hutool.core.util.StrUtil;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 回复基站坐标
 * Date: Created in 15:31 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class StationCoordinatesAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationCoordinatesAction.class);

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("基站坐标");
        print(dataDomain);
        validLogin(ctx);
    }

    @Override
    public boolean supports(String code) {
        return "000B".equals(code);
    }


    public static String packageData(String sn,String id,String x,String y){
        StringBuffer sb =new StringBuffer();
                        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 0B ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKEAGE_REQUEST+" ");

                // data length
               String datalength =  StrUtil.fillBefore(Integer.toHexString(12+37),'0',4);
                sb.append(datalength.substring(0,2)+" "+datalength.substring(2,4)+" ")
                //AQ status
                .append("00 ")
                // crc16
                .append("## ## ")
                /**设备号块*/
                .append("00 13 ")
                .append("01 ")
                .append(Crc16Util.byteTo16String(sn.getBytes()))
                /**基站ID 块*/
                .append("00 04 ")
                .append("02 ")
                .append(id+" ")
                /**基站X轴坐标*/
                .append("00 07 ")
                .append("03 ")
                .append(x+" ")
                /**基站Y轴坐标*/
                .append("00 07 ")
                .append("04 ")
                .append(y);
                    String crc = doCrc(sb.toString());


        return changeCrc(sb.toString(),crc);
    }

}
