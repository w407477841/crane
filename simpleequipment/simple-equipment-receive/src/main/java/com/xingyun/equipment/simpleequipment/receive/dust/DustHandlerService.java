package com.xingyun.equipment.simpleequipment.receive.dust;

import com.xingyun.equipment.simpleequipment.core.pojo.CompleteDataPojo;
import com.xingyun.equipment.simpleequipment.core.pojo.DuctDataPojoVo;
import com.xingyun.equipment.simpleequipment.receive.handler.CommonMethod;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.xingyun.equipment.simpleequipment.core.CommonStaticMethod.decode;
import static com.xingyun.equipment.simpleequipment.core.CommonStaticMethod.resolutionProtocol;

/**
 * @author hjy
 * @date 2018/9/18
 * 环境监控协议解析服务类(扬尘)
 */
@Service
public class DustHandlerService {
    @Autowired
    private DustBusinessLogicService businessLogicService;
    @Autowired
    private CommonMethod commonMethod;

    private Logger logger = LoggerFactory.getLogger(DustHandlerService.class);

    /**
     * 数据解析
     *
     * @param ctx
     * @param restDataString
     */
    public void dataProtocol(ChannelHandlerContext ctx, String restDataString, String sn) {
        try {



            //解析
            CompleteDataPojo completeDataPojo = resolutionProtocol(restDataString);
            //业务处理
            // 判断是否需要转发
            switchOperation(ctx.channel(), restDataString, completeDataPojo);

        } catch (Exception e) {
            logger.info("Dust--Parsing Exception: sn:" + sn + " <<" + restDataString + ">>");
            e.printStackTrace();
        }
    }

    private void switchOperation(Channel ctx, String dataStr, CompleteDataPojo monitorPojo,boolean needDispatch) {
        switch (monitorPojo.getCommand()) {
            //登录
            case "0001":
                businessLogicService.login(ctx, monitorPojo, dataStr);
                break;
            //心跳
            case "0002":
                businessLogicService.heartbeat(ctx, monitorPojo, dataStr);
                break;
            //同步时间
            case "0003":
                businessLogicService.synchronizationTime(ctx, monitorPojo);
                break;
            //上传监控数据
            case "0004":
                businessLogicService.uploadMonitorData(ctx, monitorPojo,needDispatch);
                break;
           /* //响应升级请求
            case "0005":
                businessLogicService.upgradeRequest(ctx,monitorPojo,dataStr);
                break;*/
            //传输升级数据
            case "0006":
                businessLogicService.upgradeTransferData(ctx, monitorPojo);
                break;
            //回复升级文件发送完成
            case "0007":
                businessLogicService.upgradeFileSentOver(monitorPojo);
                break;
            //上传异常情况
            case "0008":
                businessLogicService.exceptionalLog(ctx, monitorPojo);
                break;
            //设备回复远程重启
            case "0009":
                businessLogicService.remoteReboot(monitorPojo);
                break;
            default:
                break;
        }
    }
    private void switchOperation(Channel ctx, String dataStr, CompleteDataPojo monitorPojo) {
        switchOperation(ctx,dataStr,monitorPojo,false);
    }


    /**
     * 上传监控数据功能块 获取功能体数据
     *
     * @param monitorPojo
     * @param dataType
     * @param dataMessageStr
     * @return
     */
    public DuctDataPojoVo getMonitorPojoData(DuctDataPojoVo monitorPojo, String dataType, String dataMessageStr) {

        switch (dataType) {
            case "01":
                //序列号
                monitorPojo.setSn(decode(dataMessageStr));
                break;
            case "02":
                //风速
                monitorPojo.setWindSpeed(decode(dataMessageStr));
                break;
            case "03":
                //风力
                monitorPojo.setWindForce(decode(dataMessageStr));
                break;
            case "04":
                //风向
                monitorPojo.setWindDirection(convertWindDirection(dataMessageStr));
                break;
            case "05":
                //PM2.5
                monitorPojo.setPm25(decode(dataMessageStr));
                break;
            case "06":
                //PM10
                monitorPojo.setPm10(decode(dataMessageStr));
                break;
            case "07":
                //温度
                monitorPojo.setTemperature(decode(dataMessageStr));
                break;
            case "08":
                //湿度
                monitorPojo.setHumidity(decode(dataMessageStr));
                break;
            case "09":
                //噪声
                monitorPojo.setNoise(decode(dataMessageStr));
                break;
            default:
                break;
        }
        return monitorPojo;
    }


    /**
     * 风向转换
     *
     * @param code
     * @return
     */
    private String convertWindDirection(String code) {

        for(WindDirection direction : WindDirection.values()){
            if(direction.code.equals(code)){
                return direction.name;
            }
        }
        return "";
    }

}
