package com.xywg.equipment.monitor.iot.netty.device.crane;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneOriginalDataMapper;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneOriginalData;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.xywg.equipment.global.GlobalStaticConstant.DEVICE_STATE_ON_LINE;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.resolutionProtocol;

/**
 * @author hjy
 * @date 2018/10/25
 * 塔吊 协议解析
 */
@Component
public class CraneHandlerService {
    @Autowired
    private ProjectCraneOriginalDataMapper projectCraneOriginalDataMapper;
    @Autowired
    private ProjectCraneMapper projectCraneMapper;
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
        try {
            Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no", sn);
            wrapper.eq("is_del", 0);
            // 获取 设备信息
            ProjectCrane projectCrane = SqlHelper.getObject(projectCraneMapper.selectList(wrapper));
            //若为空..
            if (projectCrane == null) {
                logger.error("Crane--Illegal Data(Device:"+sn+" Unreal)<<" + restDataString + ">>");
                ctx.close();
                return;
            }
            //若未启用..
            if (projectCrane.getStatus() == null || projectCrane.getStatus() == 0) {
                logger.error("Crane--Error Data(Device:"+sn+" Not Enabled)<<" + restDataString + ">>");
                ctx.close();
                return;
            }
            //获取项目信息
            ProjectInfo projectInfo = projectInfoService.selectById(projectCrane.getProjectId());
            //若不在任何一个项目下
            if (projectInfo == null) {
                logger.error("Crane--Error Data(Device:"+sn+" No Project exists)<<" + restDataString + ">>");
                ctx.close();
                return;
            }
            ProjectCraneOriginalData mData = new ProjectCraneOriginalData(sn, restDataString, new Date());
            //保存有效的原始数据（设备号，原始数据，入库时间）
            projectCraneOriginalDataMapper.insert(mData);

            //设备状态处理
            // 只有登陆会改变在线状态
//            commonMethod.handleDeviceIsOnline(sn,DEVICE_STATE_ON_LINE);

            //解析
            CompleteDataPojo completeDataPojo = resolutionProtocol(restDataString);
            //业务处理
            switchOperation(ctx.channel(), restDataString, completeDataPojo,projectInfo);

        } catch (Exception e) {
            logger.info("Crane--Parsing Exception: sn:" + sn + " <<" + restDataString + ">>");
            e.printStackTrace();
        }
    }


    /**
     * 选择业务操作
     *
     * @param channel              上下文
     * @param restDataString   原始串
     * @param completeDataPojo 解析后的对象
     */
    private void switchOperation(Channel channel, String restDataString, CompleteDataPojo completeDataPojo, ProjectInfo projectInfo) {

        switch (completeDataPojo.getCommand()) {
            //登录
            case "0001":
                attendanceBusinessLogicService.login(channel, completeDataPojo);
                break;
            //心跳
            case "0002":
                attendanceBusinessLogicService.heartbeat(channel, completeDataPojo, restDataString);
                break;
            //同步时间
            case "0003":
                attendanceBusinessLogicService.synchronizationTime(channel, completeDataPojo);
                break;
            //上传监控数据
            case "0004":
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
            //发送接收文件请求
            case "0006":
                attendanceBusinessLogicService.upgradeTransferData(channel, completeDataPojo);
                break;
            //升级文件发送完毕 主动下发指令  不做处理
            case "0007":
                logger.info("设备回复中心升级文件接收完毕.");
                break;
            //设备上传异常情况（报警） 该功能暂时弃用
            case "0008":
                //attendanceBusinessLogicService.exceptionalAlarm(ctx, completeDataPojo, restDataString);
                break;
            //0009为重启  主动下发操作
            case "0009":
                logger.info("设备回复中心重启指令收到.");
                break;
            //塔机基本信息设置上传服务器
            case "000A":
                attendanceBusinessLogicService.basicInformation(channel, completeDataPojo);
                break;
           //设备回复服务器下传塔机基本设置信息
            case "000B":
                break;
            //塔机幅度校准信息设置上传服务器
            case "000C":
                attendanceBusinessLogicService.amplitudeCorrect(channel, completeDataPojo,projectInfo.getId());
                break;
            //塔机高度校准信息设置上传服务器
            case "000D":
                attendanceBusinessLogicService.heightCorrect(channel, completeDataPojo,projectInfo.getId());
                break;
            //塔机角度校准信息设置上传服务器
            case "000E":
                attendanceBusinessLogicService.angleCorrect(channel, completeDataPojo,projectInfo.getId());
                break;
            //塔机起重量校准信息设置上传服务器
            case "000F":
                attendanceBusinessLogicService.elevatingCapacityCorrect(channel, completeDataPojo,projectInfo.getId());
                break;
            //单机防碰撞区域设置上传服务器
            case "0010":
                attendanceBusinessLogicService.antiCollisionCorrect(channel, completeDataPojo,projectInfo.getId());
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
