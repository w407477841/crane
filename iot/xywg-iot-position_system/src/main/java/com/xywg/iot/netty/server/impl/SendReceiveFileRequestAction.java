package com.xywg.iot.netty.server.impl;

import cn.hutool.core.util.StrUtil;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.models.FilePojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

/**
 * @author : wangyifei
 * Description 发送接收文件请求
 * Date: Created in 15:25 2019/2/26
 * Modified By : wangyifei
 */
@Component
public class SendReceiveFileRequestAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendReceiveFileRequestAction.class);

    @Override
    public boolean supports(String code) {
        return "0006".equals(code);
    }

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("发送接收文件请求");
        print(dataDomain);
        Map<String,String> map = dataDomain.getDataMap();
        //TODO 业务逻辑
        // 基站ID
        validLogin(ctx);
        int reviceLength = Integer.parseInt(map.get("03"),16);
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());

        FilePojo file = deviceConnectInfo.getFile();
        int fileMaxLength = file.getLength();
        if(fileMaxLength!=reviceLength){

            if((fileMaxLength-reviceLength-GlobalStaticConstant.PER_LENGTH)>0){
                //路还长着呢

                try {
                    File data  = new File(file.getUrl());
                    InputStream inStream = new FileInputStream(data);
                    LOGGER.info("需要发送的包大小："+(GlobalStaticConstant.PER_LENGTH));
                    byte []   buffer = new byte[GlobalStaticConstant.PER_LENGTH];
                    inStream.skip(reviceLength);
                    inStream.read(buffer);
                    String message =  packageFileData(GlobalStaticConstant.PER_LENGTH,buffer,deviceConnectInfo.getSn(),deviceConnectInfo.getStationId());
                    PositionHandler.responseMessage(ctx,message);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }else{
                //这是最后一次
                try {
                    File data  = new File(file.getUrl());
                    InputStream inStream = new FileInputStream(data);
                    LOGGER.info("需要发送的包大小："+(fileMaxLength-reviceLength));
                    byte []   buffer = new byte[fileMaxLength-reviceLength];
                    inStream.skip(reviceLength);
                    inStream.read(buffer);
                    String message =  packageFileData(fileMaxLength-reviceLength,buffer,deviceConnectInfo.getSn(),deviceConnectInfo.getStationId());
                    PositionHandler.responseMessage(ctx,message);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }else{
            // 还要发一次 完成指令
            String completeMessage = packageCompletedData(deviceConnectInfo.getSn(),deviceConnectInfo.getStationId());
            PositionHandler.responseMessage(ctx,completeMessage);
        }









    }

    /**
     *  包装下发文件内容指令
     * @param dataLength
     * @param data
     * @param sn
     * @param stationId
     * @return
     */
    private String packageFileData(int dataLength,byte[] data,String sn,int stationId){
        StringBuffer sb =new StringBuffer();
        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 07 ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKAGE_RESPONSE+" ");

        // data length
        String datalength =  StrUtil.fillBefore(Integer.toHexString(12+19+4+5+3+dataLength),'0',4);
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
                .append(StrUtil.fillBefore(Integer.toHexString(stationId),'0',2)+" ")
                /**本包文件内容长度*/
                .append("00 05 ")
                .append("03 ")
                .append(StrUtil.fillBefore(Integer.toHexString(dataLength),'0',4)+" ");
                /**文件内容*/
                //todo 计算长度
                String length = StrUtil.fillBefore(Integer.toHexString((3+dataLength)),'0',4);
                String dataHex = DataUtil.bytesToHexString(data);
                sb.append(length.substring(0,2)+" "+length.substring(2,4)+" ")
                .append("04 ")
                .append(dataHex);
        String crc = doCrc(sb.toString());


        return changeCrc(sb.toString(),crc);
    }

    /**
     *  包装完成数据
     * @param sn
     * @param stationId
     * @return
     */
    private String packageCompletedData(String sn,int stationId){
        StringBuffer sb =new StringBuffer();
        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 08 ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKEAGE_REQUEST+" ")

            // data length
            .append("00 23 ")
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
                .append(StrUtil.fillBefore(Integer.toHexString(stationId),'0',2)+" ");

        String crc = doCrc(sb.toString());


        return changeCrc(sb.toString(),crc);
    }

    public static String packageUpgradeData(String sn,int stationId,int fileLength){
        StringBuffer sb =new StringBuffer();
        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 06 ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKEAGE_REQUEST+" ")

                // data length  12+19+4+7
                .append("00 2A ")
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
                .append(StrUtil.fillBefore(Integer.toHexString(stationId),'0',2)+" ");
                /** 升级文件长度 */
               String fileLengthHex = StrUtil.fillBefore(Integer.toHexString(fileLength),'0',8);
                sb.append("00 07 ")
                .append("03 ")
                .append(fileLengthHex.substring(0,2)+" "+fileLengthHex.substring(2,4)+" "+fileLengthHex.substring(4,6)+" "+fileLengthHex.substring(6,8)+" ");


        String crc = doCrc(sb.toString());


        return changeCrc(sb.toString(),crc);


    }



}
