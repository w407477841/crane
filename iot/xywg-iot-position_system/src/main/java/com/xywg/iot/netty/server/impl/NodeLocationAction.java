package com.xywg.iot.netty.server.impl;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.common.utils.KeyUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.modular.station.model.*;
import com.xywg.iot.modular.station.service.IProjectDeviceStockService;
import com.xywg.iot.modular.station.service.IProjectDeviceWorkerRecordService;
import com.xywg.iot.modular.station.service.IProjectRegionalPositionDataService;
import com.xywg.iot.netty.handler.CommonStaticMethod;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.server.BaseAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description 节点信息(区域定位)
 * Date: Created in 14:17 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class NodeLocationAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeLocationAction.class);

    private final IProjectRegionalPositionDataService projectRegionalPositionDataService;
    private final IProjectDeviceStockService deviceStockService;
    private final IProjectDeviceWorkerRecordService projectDeviceWorkerRecordService;
    private final RedisUtil redisUtil;
    private final LoginAction loginAction;
    @Autowired
    public NodeLocationAction(IProjectRegionalPositionDataService projectRegionalPositionDataService, IProjectDeviceStockService deviceStockService, IProjectDeviceWorkerRecordService projectDeviceWorkerRecordService, RedisUtil redisUtil, LoginAction loginAction) {
        this.projectRegionalPositionDataService = projectRegionalPositionDataService;
        this.deviceStockService = deviceStockService;
        this.projectDeviceWorkerRecordService = projectDeviceWorkerRecordService;
        this.redisUtil = redisUtil;
        this.loginAction = loginAction;
    }

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("节点信息(区域定位)");
        print(dataDomain);
        Map<String, String> map = dataDomain.getDataMap();
        //TODO 业务逻辑
        //  基站id
        String stationId = map.get("02");
        // 标签id  -> 代表谁
        String tagNo = CommonStaticMethod.decode(map.get("03"));
        //数据 序号
        String seqNum = map.get("04");
        //采集时间
        String callTime = map.get("05");
        validLogin(ctx);
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());
        ProjectMapStation projectMapStation;
        ProjectMap projectMap;
        String masterkey = KeyUtil.key(KeyUtil.database, KeyUtil.table_master, "sn", deviceConnectInfo.getSn());
        String mapKey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_map, "sn", deviceConnectInfo.getSn());
        if (!redisUtil.exists(masterkey)||!redisUtil.exists(mapKey)) {
            Map<String, Object> map1 = new HashMap<>();
            try {
                loginAction.validStation(
                        deviceConnectInfo.getSn(),
                        map1);
                projectMapStation = (ProjectMapStation) map1.get("projectMapStation");
                projectMap = (ProjectMap) map1.get("projectMap");
                redisUtil.set(masterkey, JSONUtil.toJsonStr(projectMapStation));
                redisUtil.set(mapKey, JSONUtil.toJsonStr(projectMap));
            } catch (Exception ex) {
                // 出异常就结束
                LOGGER.error(ex.getMessage());
                return;
            }
        }
        projectMapStation = JSONUtil.toBean((String) redisUtil.get(masterkey), ProjectMapStation.class);
        projectMap = JSONUtil.toBean((String) redisUtil.get(mapKey), ProjectMap.class);
        ProjectDevice device = deviceStockService.selectTagByDeviceNo(tagNo);
        ProjectDeviceWorkerRecord workerRecord = projectDeviceWorkerRecordService.selectByTagId(device.getId(), tagNo, projectMap.getProjectId());
        ProjectRegionalPositionData projectRegionalPositionData = new ProjectRegionalPositionData();
        projectRegionalPositionData.setLabelNo(tagNo);
        Date now  = new Date();
        projectRegionalPositionData.setCollectTime(now);
        projectRegionalPositionData.setCreateTime(now);
        projectRegionalPositionData.setStationNo(deviceConnectInfo.getSn());
        projectRegionalPositionData.setIsDel(0);

            projectRegionalPositionData.setMapId(projectMapStation.getMapId());
            projectRegionalPositionData.setIdentityCode(workerRecord.getIdCardNumber());
            projectRegionalPositionDataService.insert(projectRegionalPositionData);
            /** 返回 */

            PositionHandler.responseErrorMessageHandle(ctx, NodePrecisionAction.packageData(deviceConnectInfo.getSn(), deviceConnectInfo.getStationId()), "00", "00");

    }
    @Override
    public boolean supports(String code) {
        return "0004".equals(code);
    }
}
