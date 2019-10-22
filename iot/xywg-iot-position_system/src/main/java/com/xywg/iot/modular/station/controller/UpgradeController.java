package com.xywg.iot.modular.station.controller;

import cn.hutool.core.util.RandomUtil;
import com.xywg.iot.common.utils.FtpUtil;
import com.xywg.iot.config.properties.FtpPropteries;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.FilePojo;
import com.xywg.iot.netty.server.impl.SendReceiveFileRequestAction;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:20 2019/2/27
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class UpgradeController {
    @Autowired
    private FtpPropteries propteries;
    @Value("${localPath}")
    private String localPath;

    @GetMapping("upgrade")
    public Object upgrade(@RequestParam("deviceNo") String deviceNo,@RequestParam("path") String path){
        //deviceNo =  XY20190212020001
        //path = industrial_iot/firmware/2019/03/15/1552640021045.bin

        Channel channel  = NettyChannelManage.getChannel(deviceNo) ;
        DeviceConnectInfo connectInfo =null;
        connectInfo =      NettyChannelManage. getDeviceCodeByChannel(channel);

        FilePojo filePojo =new FilePojo();

        // 下载网络文件
        int bytesum = 0;
        String local = this.localPath+"/"+RandomUtil.randomString(20)+"."+path.split("\\.")[1];
        File file =  new File(local);
        FileOutputStream fos = null;
        try {
            fos =  new FileOutputStream(file);
            FtpUtil.downloadFile(fos,propteries.getHost(),propteries.getPort(),propteries.getUsername(),propteries.getPassword(),path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != fos){
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bytesum = (int) file.length();

        filePojo.setLength(bytesum);
        filePojo.setUrl(local);
        connectInfo.setFile(filePojo);
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(connectInfo);
       String data =  SendReceiveFileRequestAction.packageUpgradeData(connectInfo.getSn(),connectInfo.getStationId(),bytesum);
        PositionHandler.responseMessage(channel,data);
        return "发送完毕";
    }

}
