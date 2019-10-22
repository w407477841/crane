package com.xywg.equipment.monitor.iot.netty.device.spray;

import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayService;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xywg.equipment.global.GlobalStaticConstant.DEVICE_STATE_ON_LINE;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.resolutionProtocol;

/**
 * @author hjy
 * @date 2019/4/2
 * 喷淋设备数据的处理
 */
@Service
public class SprayHandleService {
    private Logger logger = LoggerFactory.getLogger(SprayHandleService.class);
    @Autowired
    private SprayBusinessLogicService sprayBusinessLogicService;
    @Autowired
    private ProjectSprayService projectSprayService;
    @Autowired
    private CommonMethod commonMethod;

    /**
     * 数据处理
     *
     * @param ctx            上下文
     * @param restDataString 原始数据
     * @param sn             设备编号
     */
    public void dataProtocol(ChannelHandlerContext ctx, String restDataString, String sn) {
        try {
            //解析
            CompleteDataPojo completeDataPojo = resolutionProtocol(restDataString);
            //设备状态处理
            // 只有登陆会改变在线状态
//            commonMethod.handleDeviceIsOnline(sn,DEVICE_STATE_ON_LINE);
            //业务处理
            switchOperation(ctx.channel(), restDataString, completeDataPojo);


        } catch (Exception e) {
            logger.info("Spray--Parsing Exception: sn:" + sn + " <<" + restDataString + ">>");
            e.printStackTrace();
        }


    }


    private void switchOperation(Channel channel, String restDataString, CompleteDataPojo completeDataPojo) {
        switch (completeDataPojo.getCommand()) {
            //登录
            case "0001":
                sprayBusinessLogicService.login(channel, completeDataPojo);
                break;
            //心跳
            case "0002":
                sprayBusinessLogicService.heartbeat(channel, completeDataPojo, restDataString);
                break;
            //发送继电器状态
            case "0003":
                sprayBusinessLogicService.sprayStatus(channel, completeDataPojo, restDataString);
                break;
              //发送手动操作结果(设备本地直接操作硬件的结果)
            case "0004":
                sprayBusinessLogicService.manualOperationResult(channel, completeDataPojo, restDataString);
                break;
                //返回系统端的后台控制结果
            case "0005":
                sprayBusinessLogicService.systemOperationResult(channel, completeDataPojo, restDataString);
                break;
            //远程重启
            case "0006":
                sprayBusinessLogicService.reboot(channel, completeDataPojo, restDataString);
                break;
            default:
                break;


        }

    }




}
