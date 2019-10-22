package com.xywg.iot.netty.server.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.common.utils.KeyUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.modular.station.model.ProjectDevice;
import com.xywg.iot.modular.station.model.ProjectDeviceStock;
import com.xywg.iot.modular.station.model.ProjectMap;
import com.xywg.iot.modular.station.model.ProjectMapStation;
import com.xywg.iot.modular.station.service.IProjectDeviceService;
import com.xywg.iot.modular.station.service.IProjectDeviceStockService;
import com.xywg.iot.modular.station.service.IProjectMapService;
import com.xywg.iot.modular.station.service.IProjectMapStationService;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.xywg.iot.netty.handler.CommonStaticMethod.decode;

/**
 * @author : wangyifei
 * Description 登录
 * Date: Created in 9:40 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class LoginAction extends BaseAction {

    private final IProjectDeviceStockService projectDeviceStockService;
    private final IProjectMapStationService projectMapStationService;
    private final IProjectMapService  projectMapService;
    private final IProjectDeviceService projectDeviceService;
    private final RedisUtil redisUtil;
    @Autowired
    public LoginAction(IProjectDeviceStockService projectDeviceStockService, IProjectMapStationService projectMapStationService, IProjectMapService projectMapService, IProjectDeviceService projectDeviceService, RedisUtil redisUtil) {
        this.projectDeviceStockService = projectDeviceStockService;
        this.projectMapStationService = projectMapStationService;
        this.projectMapService = projectMapService;
        this.projectDeviceService = projectDeviceService;
        this.redisUtil = redisUtil;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        ProjectMapStation projectMapStation ;
        ProjectMap projectMap ;

        Map<String,Object> map =  new HashMap<>();
        //设备序列号
        String sn = decode(dataDomain.getOri().replaceAll(" ", "").substring(30, 62));

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        DeviceConnectInfo deviceConnectInfo = new DeviceConnectInfo(uuid, sn);
        deviceConnectInfo.setStationId(Integer.parseInt(dataDomain.getDataMap().get("02"),16));
        LOGGER.info("功能<{}> , 设备号<{}> , 基站号<{}>","登录",sn,deviceConnectInfo.getStationId());
        // 基站


        // 2. 缓存子站信息
        validStation(sn,map);
        projectMap = (ProjectMap) map.get("projectMap");
        projectMapStation = (ProjectMapStation) map.get("projectMapStation");

        // 1. 缓存主站信息
        String masterkey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_master,"sn",sn);
        redisUtil.set(masterkey,JSONUtil.toJsonStr(projectMapStation));
        // 2.缓存地图信息
        String mapkey = KeyUtil.key(KeyUtil.database, KeyUtil.table_map,"sn",sn);
        redisUtil.set(mapkey, JSONUtil.toJsonStr(projectMap));

        ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceConnectInfo);
        //更新map有效的链接
        NettyChannelManage.CHANNEL_MAP.put(sn, ctx.channel());


        PositionHandler.responseErrorMessageHandle(ctx,dataDomain.getOri(),"00","00");


        // 下发坐标

        String xHex = doCoordinate(projectMapStation.getxZhou());
        String yHex =  doCoordinate(projectMapStation.getyZhou());
        Integer id = deviceConnectInfo.getStationId();
        String data =   StationCoordinatesAction.packageData(sn,StrUtil.fillBefore(Integer.toHexString(id),'0',2),xHex.substring(0,2)+" "+xHex.substring(2,4)+" "+xHex.substring(4,6)+" "+xHex.substring(6,8),yHex.substring(0,2)+" "+yHex.substring(2,4)+" "+yHex.substring(4,6)+" "+yHex.substring(6,8));
        PositionHandler.responseMessage(ctx,data);
    }

    @Override
    public boolean supports(String code) {
        return "0001".equals(code);
    }

    /**
     *  将坐标*100 并 转换为 16进制
     * @param n
     * @return
     */
    private String doCoordinate(BigDecimal n){
        String hex = Integer.toHexString(n.setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue());
        hex = StrUtil.fillBefore(hex,'0',8);
        return hex;

    }



    public void validStation(String sn,Map<String,Object> map){
        ProjectDevice projectDevice;
        ProjectMap projectMap;
        ProjectMapStation projectMapStation;

        Wrapper<ProjectDevice> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del",0);
        wrapper.eq("current_flag",0);
        wrapper.eq("type",2);
        wrapper.eq("device_no",sn);
        projectDevice = projectDeviceService.selectOne(wrapper);
        if(projectDevice == null){
            String errorMsg =  "不存在 基站设备号<"+sn+">";
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        if(1!=projectDevice.getStatus()){
            String errorMsg =  "基站未启用<"+sn+">";
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        // 基站 平面图关系
        projectMapStation =  projectMapStationService.selectByDeviceId(projectDevice.getId());

        if(projectMapStation==null){
            String errorMsg =  "未绑定平面图 基站设备号<"+sn+"> ";
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        // 平面图
        projectMap =  projectMapService.selectById(projectMapStation.getMapId());
        if(projectMap==null){
            String errorMsg =  "不存在 平面图id <"+projectMapStation.getMapId()+"> ";
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        map.put("projectMapStation",projectMapStation);
        map.put("projectMap",projectMap);
        map.put("projectDevice",projectDevice);

    }

}
