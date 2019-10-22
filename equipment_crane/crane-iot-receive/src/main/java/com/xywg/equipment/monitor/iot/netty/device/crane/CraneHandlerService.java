package com.xywg.equipment.monitor.iot.netty.device.crane;

import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneOriginalDataMapper;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.resolutionProtocol;

/**
 * @author hjy
 * @date 2018/10/25
 * 塔吊 协议解析
 */
@SuppressWarnings("all")
@Component
public class CraneHandlerService {
    @Autowired
    private ProjectCraneOriginalDataMapper projectCraneOriginalDataMapper;
    @Autowired
    private ProjectCraneMapper projectCraneMapper;
    private IProjectCraneService projectCraneService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private CraneBusinessLogicService attendanceBusinessLogicService;
    @Autowired
    private CommonMethod commonMethod;

    private Logger logger = LoggerFactory.getLogger(CraneHandlerService.class);

    /**
     * 数据解析
     *
     * @param ctx            上下文
     * @param restDataString 原始数据
     */
    public void dataProtocol(ChannelHandlerContext ctx, String restDataString, String sn) {
        //解析
        CompleteDataPojo completeDataPojo = resolutionProtocol(restDataString);
        //业务处理
        long start = System.currentTimeMillis();
        switchOperation(ctx.channel(), restDataString, completeDataPojo);
        logger.info("业务处理耗时:{}", (System.currentTimeMillis() - start));

    }

    /**
     * 离线
     *
     * @param channel
     * @param sn
     */
    public void offline(Channel channel, String sn) {
        attendanceBusinessLogicService.offline(channel, sn, GlobalStaticConstant.OFFLINE_REASON_NETWORK);
    }

    /**
     * 选择业务操作
     *
     * @param channel          上下文
     * @param restDataString   原始串
     * @param completeDataPojo 解析后的对象
     */
    private void switchOperation(Channel channel, String restDataString, CompleteDataPojo completeDataPojo) {
        System.out.println(completeDataPojo);
        switch (completeDataPojo.getCommand()) {
            //登录（）
            case "0001":
                logger.info("同步时间原始数据:{}", restDataString);
                attendanceBusinessLogicService.login(channel, completeDataPojo);
                break;
            //心跳（√）
            case "0002":
                logger.info("同步时间原始数据:{}", restDataString);
                attendanceBusinessLogicService.heartbeat(channel, completeDataPojo, restDataString);
                break;
            //同步时间（√）
            case "0003":
                logger.info("同步时间原始数据:{}", restDataString);
                attendanceBusinessLogicService.synchronizationTime(channel, completeDataPojo);
                break;
            //上传监控数据(√)
            case "0004":
                logger.info("监控数据原始数据:{}", restDataString);
                attendanceBusinessLogicService.uploadMonitorData(channel, completeDataPojo);
                break;
            //上传离线监控数据(√)
            case "0012":
                logger.info("同步离线监控数据:{}", restDataString);
                attendanceBusinessLogicService.uploadOfflineMonitorData(channel, completeDataPojo);
                break;
            // 上传工作循环
            case "0013":
                logger.info("工作循环原始数据:{}", restDataString);
                attendanceBusinessLogicService.operatingCycle(channel, completeDataPojo);
                break;
            // 上传离线工作循环
            case "0011":
                logger.info("离线工作循环原始数据:{}", restDataString);
                attendanceBusinessLogicService.offlineOperatingCycle(channel, completeDataPojo);
                break;
            // 上传累计工作时间
            case "0014":
                logger.info("累计工作时间原始数据:{}", restDataString);
                attendanceBusinessLogicService.offlineReason(channel, completeDataPojo);
                break;
            //0005为升级  主动下发操作
            /*case "0005":
                break;*/
            //发送接收文件请求（√）
            case "0006":
                logger.info("发送接收文件请求原始数据:{}", restDataString);
                attendanceBusinessLogicService.upgradeTransferData(channel, completeDataPojo);
                break;
            //升级文件发送完毕 主动下发指令  不做处理 （√）
            case "0007":
                logger.info("设备回复中心升级文件接收完毕.");
                break;
            //设备上传异常情况（报警）
            case "0008":
                logger.info("异常情况原始数据:{}", restDataString);
                attendanceBusinessLogicService.exceptionalLog(channel, completeDataPojo, restDataString);
                break;
            //0009为重启  主动下发操作
            case "0009":
                logger.info("设备回复中心重启指令收到.");
                break;
            //塔机基本信息设置上传服务器（√）
            case "000A":
                logger.info("塔机基本信息设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.basicInformation(channel, completeDataPojo);
                break;
            //设备回复服务器下传塔机基本设置信息
            case "000B":
                break;
            //塔机幅度校准信息设置上传服务器
            case "000C":
                logger.info("塔机幅度校准信息设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.amplitudeCorrect(channel, completeDataPojo);
                break;
            //塔机高度校准信息设置上传服务器
            case "000D":
                logger.info("塔机高度校准信息设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.heightCorrect(channel, completeDataPojo);
                break;
            //塔机角度校准信息设置上传服务器
            case "000E":
                logger.info("塔机角度校准信息设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.angleCorrect(channel, completeDataPojo);
                break;
            //塔机起重量校准信息设置上传服务器
            case "000F":
                logger.info("塔机起重量校准信息设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.elevatingCapacityCorrect(channel, completeDataPojo);
                break;
            //单机防碰撞区域设置上传服务器
            case "0010":
                logger.info("单机防碰撞区域设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.antiCollisionCorrect(channel, completeDataPojo);
                break;
            case "0015":
                logger.info("多机防碰撞设置原始数据:{}", restDataString);
                attendanceBusinessLogicService.mutiCollisionCorrect(channel, completeDataPojo);
                break;
            default:
                break;
        }
    }
}
