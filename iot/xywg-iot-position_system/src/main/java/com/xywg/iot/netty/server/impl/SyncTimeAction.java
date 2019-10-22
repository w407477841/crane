package com.xywg.iot.netty.server.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author : wangyifei
 * Description 同步时间
 * Date: Created in 11:28 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class SyncTimeAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTimeAction.class);

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("同步时间");
        print(dataDomain);
        Map<String,String> map = dataDomain.getDataMap();
        validLogin(ctx);
        //TODO 业务逻辑
        // 基站ID
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());

       String timedata =   packageTimeData(deviceConnectInfo.getSn(),deviceConnectInfo.getStationId());

        PositionHandler.responseErrorMessageHandle(ctx,timedata,"00","00");
    }

    @Override
    public boolean supports(String code) {
        return "0003".equals(code);
    }

    private String BCD(){
      String data =   DateUtil.format(new Date(),"yyyy MM dd HH mm ss");
        return "00 0A 03 "+data.substring(0,2)+" "+data.substring(2);
    }


    /**
     *  回复同步时间
     * @param sn
     * @param stationId
     * @return
     */
    private String packageTimeData(String sn,int stationId){
        StringBuffer sb =new StringBuffer();
        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 03 ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKAGE_RESPONSE+" ");

        // data length
        sb.append("00 2D ")
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
                .append(StrUtil.fillBefore(Integer.toHexString(stationId),'0',2)+" ")
                .append(BCD()+" ");

        String crc = doCrc(sb.toString());


        return changeCrc(sb.toString(),crc);
    }


}
