package com.xywg.equipment.monitor.iot.netty.device.spray;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSpray;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayBindVo;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayDetail;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayDetailService;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayService;
import com.xywg.equipment.monitor.iot.netty.aop.pojo.CompleteDataPojo;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.NettyChannelManage;
import com.xywg.equipment.monitor.iot.netty.device.model.DeviceConnectInfo;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.xywg.equipment.global.GlobalStaticConstant.*;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.decode;
import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.stringToHexString;

/**
 * @author hjy
 * @date 2019/4/2
 */
@Service
public class SprayBusinessLogicService {
    @Value("${xywg.spray-manual-time}")
    private String sprayManualTime;

    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private ProjectSprayService projectSprayService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProjectSprayDetailService projectSprayDetailService;

    private Logger logger = LoggerFactory.getLogger(SprayBusinessLogicService.class);

    /**
     * 登录 只包含(sn,firmware)
     *
     * @param monitorPojo 数据
     */
    public void login(Channel channel, CompleteDataPojo monitorPojo) {
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01"));
        //更新map有效的链接
        logger.info("喷射--设备{}开始执行 登录", sn);
        if (NettyChannelManage.CHANNEL_MAP.containsKey(sn)) {
            logger.error("喷射--之前的链接未失效({}，提前断开)", sn);
            NettyChannelManage.CHANNEL_MAP.get(sn).close();
            throw new RuntimeException("未失效,主动断开");
        }
        NettyChannelManage.CHANNEL_MAP.put(sn, channel);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        DeviceConnectInfo deviceConnectInfo = new DeviceConnectInfo(uuid, sn);
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceConnectInfo);



        Wrapper<ProjectSpray> we = new EntityWrapper<>();
        we.eq("device_no", sn);
        we.eq("is_del", 0);
        // 获取 设备信息
        ProjectSpray projectCrane = projectSprayService.selectOne(we);
        if (projectCrane == null) {
            logger.error("Spray--Illegal Data(Device:" + sn + " Unreal)<< Device does not exist >>");
            channel.close();
            return;
        }

        //设备版本号(固件版本号)
        String deviceVersion = commonMethod.getDeviceVersion(monitorPojo.getDataMap().get("02"));
        ProjectSpray projectSpray = new ProjectSpray();
        projectSpray.setDeviceNo(sn);
        projectSpray.setIsOnline(DEVICE_STATE_ON_LINE);
        projectSpray.setModifyTime(new Date());

        Wrapper<ProjectSpray> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no", sn);
        wrapper.eq("is_del", 0);
        projectSprayService.update(projectSpray, wrapper);

        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }

    /**
     * 心跳
     *
     * @param monitorPojo 数据
     */
    public void heartbeat(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //序列号
        //   String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));

    }


    /**
     * 发送继电器状态
     *
     * @param monitorPojo 数据
     */
    public void sprayStatus(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //序列号
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        //成功后将保存设备状态到redis
        redisUtil.set("device_platform.spray." + sn, monitorPojo.getDataMap().get("02"));

        EntityWrapper<ProjectSpray> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("device_no", sn);

        ProjectSpray projectSpray = new ProjectSpray();
        projectSpray.setDeviceNo(sn);
        projectSpray.setStatus(Integer.parseInt(monitorPojo.getDataMap().get("02")));

        //修改数据库设备状态
        projectSprayService.update(projectSpray, ew);


    }

    /**
     * 发送手动操作结果(直接操作硬件的结果)
     *
     * @param channel       通道
     * @param monitorPojo   数据
     * @param dataStrString 原始数据
     */
    public void manualOperationResult(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //序列号
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        //人员现场操作类型 1开 0关
        Integer operationType = Integer.parseInt(monitorPojo.getDataMap().get("02"));

        //人员现场操作结果 1成功 0失败
        Integer operationResult = Integer.parseInt(monitorPojo.getDataMap().get("03"));

        if (operationResult == 1) {
            //成功后将保存设备状态到redis
            redisUtil.set("device_platform.spray." + sn, operationType);
            EntityWrapper<ProjectSpray> ew = new EntityWrapper<>();
            ew.eq("is_del", 0);
            ew.eq("device_no", sn);

            ProjectSpray projectSpray = new ProjectSpray();
            projectSpray.setDeviceNo(sn);
            projectSpray.setStatus(operationType);
            //修改数据库设备状态
            projectSprayService.update(projectSpray, ew);

            //将手动操作的记录保存到redis  下发指令时必须要参考改数据  人工干预机制以达到节约用水的目的
            redisUtil.set("device_platform.spray.manual.intervention." + sn, operationType, Long.parseLong(sprayManualTime));

        }

        //记录现场操作人员的记录


    }


    /**
     * 返回中心的操作结果
     *
     * @param channel       通道
     * @param monitorPojo   数据
     * @param dataStrString 原始数据
     */
    public void systemOperationResult(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //序列号
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        //成功后将保存设备状态到redis
        redisUtil.set("device_platform.spray." + sn, monitorPojo.getDataMap().get("02"));
        //操作结果 1成功 0失败
        Integer operationResult = Integer.parseInt(monitorPojo.getDataMap().get("02"));

        Integer detailId = Integer.parseInt(monitorPojo.getDataMap().get("03"));

        ProjectSprayDetail detail = new ProjectSprayDetail();
        detail.setId(detailId);
        detail.setDeviceNo(sn);
        detail.setOperationResult(operationResult);
        //修改数据库设备状态
        projectSprayDetailService.updateById(detail);

        if (operationResult == 1) {
            ProjectSprayDetail detailDb = projectSprayDetailService.selectById(detailId);
            if (detailDb == null) {
                return;
            }
            //操作类型: ,0关闭喷淋,1:开启喷淋2重启设备
            if (detailDb.getOperationType() == 1) {
                updateDeviceStatus(sn, 1);
            } else {
                updateDeviceStatus(sn, 0);
            }
        }


    }

    /**
     * 更新设备状态
     *
     * @param sn
     * @param status
     */
    private void updateDeviceStatus(String sn, Integer status) {
        //成功后将保存设备状态到redis
        redisUtil.set("device_platform.spray." + sn, status);
        EntityWrapper<ProjectSpray> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("device_no", sn);

        ProjectSpray projectSpray = new ProjectSpray();
        projectSpray.setDeviceNo(sn);
        projectSpray.setStatus(status);
        //修改数据库设备状态
        projectSprayService.update(projectSpray, ew);
    }

    /**
     * 收到的重启结果
     *
     * @param monitorPojo 数据
     */
    public void reboot(Channel channel, CompleteDataPojo monitorPojo, String dataStrString) {
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        if (deviceConnectInfo == null) {
            // 请求未登录
            throw new RuntimeException("设备还未登录");
        }
        //序列号
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));
        ProjectSprayDetail detail = new ProjectSprayDetail();
        detail.setId(Integer.parseInt(monitorPojo.getDataMap().get("02")));
        detail.setDeviceNo(sn);
        detail.setOperationResult(1);

        //修改数据库设备状态
        projectSprayDetailService.updateById(detail);


    }


    /**
     * 给喷淋下发指令
     *
     * @param list        设备
     * @param flag        开启 01   关闭00
     * @param detail      扬尘明细数据ID
     * @param controlMode 控制方式: 0系统自动操作,1平台手动操作
     */
    public String issueSwitchInstructions(List<ProjectSpray> list, String flag, String detail, Integer controlMode) {
        StringBuilder sb = new StringBuilder();
        for (ProjectSpray spray : list) {
            Channel channel = NettyChannelManage.getChannel(spray.getDeviceNo());
            if (channel != null) {
                //序列号转16进制字节码
                String snCode = stringToHexString(spray.getDeviceNo()).replaceAll(" ", "");

                //查询该设备在一段时间内是否被人工在现场干涉过,  人工直接干涉大于一切
                Integer deviceStatus = (Integer) redisUtil.get("device_platform.spray.manual.intervention." + spray.getDeviceNo());
                //被人工干预过的 在一段时间内系统不能再自动控制设备的开关
                if (deviceStatus != null) {
                    sb.append(spray.getDeviceNo()).append("被手动干涉过").append(",");
                    continue;
                }

                String flagString =  redisUtil.get("device_platform.spray." + spray.getDeviceNo()).toString();

                if (flagString == null || !flagString.equals(flag)) {
                    ProjectSprayDetail projectSprayDetail = new ProjectSprayDetail();
                    projectSprayDetail.setDeviceNo(spray.getDeviceNo());
                    projectSprayDetail.setSprayId(spray.getId());
                    projectSprayDetail.setDetail(detail);
                    projectSprayDetail.setControlMode(controlMode);
                    projectSprayDetail.setOperationType(Integer.parseInt(flag));
                    projectSprayDetail.setCreateTime(new Date());
                    projectSprayDetailService.insert(projectSprayDetail);
                    LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
                    linkedMap.put("02", flag);
                    linkedMap.put("03", projectSprayDetail.getId().toString());
                    commonMethod.resMessageJoint(channel, snCode, linkedMap,
                            "0005", "01", "00");

                    sb.append(spray.getDeviceNo()).append("操作成功").append(",");
                }else{
                    sb.append(spray.getDeviceNo()).append("与之前状态一致").append(",");
                }
            }
        }

        return sb.toString();
    }


}
