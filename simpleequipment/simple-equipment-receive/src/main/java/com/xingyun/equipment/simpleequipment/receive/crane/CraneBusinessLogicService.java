package com.xingyun.equipment.simpleequipment.receive.crane;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.simpleequipment.core.pojo.CompleteDataPojo;
import com.xingyun.equipment.simpleequipment.core.properties.XywgProerties;
import com.xingyun.equipment.simpleequipment.receive.handler.CommonMethod;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.dao.ProjectCraneDetailMapper;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.model.ProjectCraneAlarm;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.model.ProjectCraneDetail;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.service.IProjectCraneAlarmService;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.service.IProjectCraneDetailService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.xingyun.equipment.simpleequipment.core.CommonStaticMethod.decode;
import static com.xingyun.equipment.simpleequipment.core.Const.DATA_PACKAGE_RESPONSE;

/**
 * @author hjy
 * @date 2018/9/20
 * 环境监控 业务逻辑处理服务类(塔吊)
 */
@Component
@Slf4j
public class CraneBusinessLogicService {
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private IProjectCraneAlarmService projectCraneAlarmService;

    @Autowired
    private IProjectCraneDetailService projectCraneDetailService;
    /**
     * 登录 只包含(sn,firmware)
     *
     * @param monitorPojo 数据
     */
    public void login(Channel channel, CompleteDataPojo monitorPojo) {
        //序列号:

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // id
        map.put("02", "0001");
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);


    }

    /**
     * 心跳
     *
     * @param monitorPojo 数据
     */
    public void heartbeat(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
    }

    /**
     * 同步时间
     *
     * @param monitorPojo 数据
     */
    public void synchronizationTime(Channel channel, CompleteDataPojo monitorPojo) {

        //序列号 16进制的:
        String sn = monitorPojo.getDataMap().get("01");
        commonMethod.synchronizationTime(channel, sn, monitorPojo.getCommand(), monitorPojo.getVersion());
    }

    /**
     * 传输升级数据
     *
     * @param monitorPojo 数据
     */
    void upgradeTransferData(Channel channel, CompleteDataPojo monitorPojo) {


    }

    /**
     * 升级文件发送完毕回复
     *
     * @param monitorPojo 数据
     */
//    private void upgradeFileSentOver(CompleteDataPojo monitorPojo) {
//        //暂时不做处理
//
//
//    }

    /**
     * 上传设备异常情况数据
     *
     * @param monitorPojo 数据
     */
    public void exceptionalLog(Channel ctx, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
    }

    /**
     * 远程重启
     *
     * @param monitorPojo
     */
    public void remoteReboot(CompleteDataPojo monitorPojo) {
        //暂时不做回复处理,只做命令下发
    }


    /**
     * 上传监控数据
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorData(Channel channel, CompleteDataPojo monitorPojo) {
        String sn = decode(monitorPojo.getDataMap().get("01"));
        log.info("塔吊--设备{}开始执行 监控数据", sn);
        // 查询设备阈值
        log.info("塔吊--设备{}回复", sn);
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        BigDecimal craneHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16)/10.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal dgHeight=new BigDecimal(Integer.parseInt(monitorPojo.getDataMap().get("02"), 16)/1000.0).setScale(2,RoundingMode.HALF_UP);
        BigDecimal height=craneHeight.subtract(dgHeight).setScale(2,RoundingMode.HALF_UP);
        ProjectCraneDetail projectCraneDetail = new ProjectCraneDetail(
                null,
                sn,
                null,
                DateUtil.parse(monitorPojo.getDataMap().get("0E")),
                null,
                Long.parseLong(monitorPojo.getDataMap().get("05"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("0A"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("06"), 16) / 1000d,
                Long.parseLong(monitorPojo.getDataMap().get("0B"), 16) / 100d,
                //height / 1d,
                Double.parseDouble(height.toString()),
                Integer.parseInt(monitorPojo.getDataMap().get("03"), 16) / 1000d,
                Integer.parseInt(monitorPojo.getDataMap().get("07"), 16) / 1d,
                Integer.parseInt(monitorPojo.getDataMap().get("04"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("08"), 16) / 10d,
                Integer.parseInt(monitorPojo.getDataMap().get("09"), 16) / 10d,
                0,
                DateUtil.parse(monitorPojo.getDataMap().get("0E")),
                Double.parseDouble(craneHeight.toString()),
                Double.parseDouble(dgHeight.toString())
                //Integer.parseInt(monitorPojo.getDataMap().get("0D"), 16) / 10d
        );
        //数据明细表
        projectCraneDetailService.insert(projectCraneDetail);
        //检查是否是报警信息  0A表示报警id
        projectCraneDetail.setAlarmInfo(monitorPojo.getDataMap().get("0C"));
        String alarmData = projectCraneDetail.getAlarmInfo();

        //00000000 表示正常数据
        String alarmTypeNormal = "00000000";
        if (StrUtil.isBlank(alarmData) || alarmTypeNormal.equals(alarmData)) {
            log.info("塔吊--设备{}正常数据", sn);
            return;
        }

        try {
             sendAlarmData(projectCraneDetail, alarmData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 上传离线监控数据
     *  1.新增监控数据
     *  2.新增报警数据
     *  3.修改设备报警状态
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadOfflineMonitorData(Channel channel, CompleteDataPojo monitorPojo) {

        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }

    /**
     * 塔机基本信息设置上传服务器
     *
     * @param channel     通道
     * @param monitorPojo 数据
     */
    public void basicInformation(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);



    }

    /**
     * 塔机幅度校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel     通道
     * @param monitorPojo 数据
     */
    public void amplitudeCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);


    }

    /**
     * 塔机角度校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     */
    public void angleCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);



    }

    /**
     * 塔机起重量校准信息设置上传服务器
     * 接收到的数据中4个字节的使用Long 接收, 2字节的使用INT接收
     * <p>
     * 由于校准设置暂时统一存到一张表中,为了以后容易扩展,将所有字段信息一一列出
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     */
    public void elevatingCapacityCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }

    public void heightCorrect(Channel channel, CompleteDataPojo monitorPojo) {
       
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
    
    }
    
    /**
     * 单机防碰撞区域设置上传服务器
     *
     * @param channel     上下文
     * @param monitorPojo 数据
     */
    public void antiCollisionCorrect(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }

    /**
     * 多机防碰撞
     *
     * @param channel     通道
     * @param monitorPojo 信息
     */

    void mutiCollisionCorrect(Channel channel, CompleteDataPojo monitorPojo) {

        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }

    /**
     * 处理报警数据
     *
     * @param projectCraneDetail 解析后的明细数据
     * @param alarmData          报警数据中的报警信息代码
     */
    private int sendAlarmData(ProjectCraneDetail projectCraneDetail, String alarmData) {
        int length = 32;
        for (int i = 0; i < length; i++) {
            //如果是1表示警告,0表示不是警告
            if (((Long.parseLong(alarmData, 16)) & (0x00000001 << i)) >> i == 1) {
                ProjectCraneAlarm alarm = new ProjectCraneAlarm();
                String warningMessage = alarmCodeToMessage(i+1,projectCraneDetail,alarm);
                alarm.setCraneId(projectCraneDetail.getCraneId());
                alarm.setDetailId(projectCraneDetail.getId());
                alarm.setAlarmId(i+1);
                alarm.setAlarmInfo(warningMessage);
                alarm.setDeviceNo(projectCraneDetail.getDeviceNo());
                alarm.setIsDel(0);
                alarm.setValue(alarm.getValue());
                alarm.setDeviceTime(projectCraneDetail.getDeviceTime());
                alarm.setCreateTime(projectCraneDetail.getDeviceTime());
                //添加报警表
                projectCraneAlarmService.insert(alarm);
            }
        }

        return 0;

    }

    /**
     * 处理离线报警数据
     *
     * @param projectCraneDetail 解析后的明细数据
     * @param alarmData          报警数据中的报警信息代码
     */
    public int sendOfflineAlarmData(ProjectCraneDetail projectCraneDetail, String alarmData) {

        return 0;
    }



    /**
     *  工作循环
     *   查询一次
     *   新增一次
     *   修改一次
     * @param channel 通道
     * @param monitorPojo 信息
     */
    public void operatingCycle(Channel channel, CompleteDataPojo monitorPojo){


        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }
    /**
     *  离线工作循环
     * @param channel 通道
     * @param monitorPojo 信息对象
     */
    public void offlineOperatingCycle(Channel channel, CompleteDataPojo monitorPojo){

        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }
    private String getAlarmInfo(List<ProjectCraneAlarm> alarms){
        StringBuffer stringBuffer =new StringBuffer();
        int status [] = new int[16];
        for(ProjectCraneAlarm alarm:alarms){
            status[alarm.getAlarmId()-1] = 1;
        }
        for(int i = 0; i<16;i++){
            if(status[i]==1){
                switch ((i+1)){
                    case 1:
                        stringBuffer.append("起重量报警;") ;
                        break;
                    case 2:
                        stringBuffer.append("起重量预警;");
                        break;
                    case 3:
                        stringBuffer.append("幅度向内报警;");
                        break;
                    case 4:
                        stringBuffer.append("幅度向内预警;");
                        break;
                    case 5:
                        stringBuffer.append("幅度向外报警;");
                        break;
                    case 6:
                        stringBuffer.append("幅度向外预警;");
                        break;
                    case 7:
                        stringBuffer.append("高度向上报警;");
                        break;
                    case 8:
                        stringBuffer.append("高度向上预警;");
                        break;
                    case 9:
                        stringBuffer.append("力矩报警;");
                        break;
                    case 10:
                        stringBuffer.append("力矩预警;");
                        break;
                    case 11:
                        stringBuffer.append("单机防碰撞报警;");
                        break;
                    case 12:
                        stringBuffer.append("单机防碰撞预警;");
                        break;
                    case 13:
                        stringBuffer.append("多机防碰撞报警;");
                        break;
                    case 14:
                        stringBuffer.append("多机防碰撞预警;");
                        break;
                    case 15:
                        stringBuffer.append("风速报警;");
                        break;
                    case 16:
                        stringBuffer.append("倾角报警;");
                        break;}

            }
        }
        return stringBuffer.toString();
    }
    /**
     * 离线原因
     * @param channel
     * @param monitorPojo
     */
    public void offlineReason(Channel channel, CompleteDataPojo monitorPojo){

        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
    }
    /**
     *  下线
     *  1. 修改设备状态为离线
     *  2. 删除设备缓存
     *  3. 添加更新受控数据
     *  4. 添加未受控数据
     *  5. 将报警状态重置，保证报警数据中含有离线时间
     *  6. 将离线报警状态重置，保证报警数据中含有离线时间
     * @param channel
     * @param sn
     * @param reason
     */
    public void offline(Channel channel,String sn,String reason){
        log.info("塔吊--设备{} 离线",sn);
    }
    //变更 报警状态
    private  void modifyOfflineAlarmStatus(String sn){

    }

    //结束报警状态
    private  void modifyAlarmStatus(String sn){

    }
    /**
     * 将报警id转换为报警信息
     *
     * @return str
     */
    private String alarmCodeToMessage(Integer alarmCode, ProjectCraneDetail detail, ProjectCraneAlarm alarm) {
        String message;
        switch (alarmCode) {
            case 1:
                message = "起重量报警";
                alarm.setValue(detail.getWeight().toString());
                break;
            case 2:
                message = "起重量预警";
                alarm.setValue(detail.getWeight().toString());
                break;
            case 3:
                message = "幅度向内报警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 4:
                message = "幅度向内预警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 5:
                message = "幅度向外报警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 6:
                message = "幅度向外预警";
                alarm.setValue(detail.getRange().toString());
                break;
            case 7:
                message = "高度向上报警";
                alarm.setValue(detail.getHeight().toString());
                break;
            case 8:
                message = "高度向上预警";
                alarm.setValue(detail.getHeight().toString());
                break;
            case 9:
                message = "力矩报警";
                alarm.setValue(detail.getMoment().toString());
                break;
            case 10:
                message = "力矩预警";
                alarm.setValue(detail.getMoment().toString());
                break;
            case 11:
                message = "单机防碰撞报警";
                break;
            case 12:
                message = "单机防碰撞预警";
                break;
            case 13:
                message = "多机防碰撞报警";
                break;
            case 14:
                message = "多机防碰撞预警";
                break;
            case 15:
                message = "风速报警";
                alarm.setValue(detail.getWindSpeed().toString());
                break;
            case 16:
                message = "倾角报警";
                alarm.setValue(detail.getTiltAngle().toString());
                break;
            default:
                message = "告警信息类型未定义";
                break;
        }
        return message;
    }


}