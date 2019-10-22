package com.xywg.iot.netty.server.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.KeyUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.modular.station.model.ProjectDevice;
import com.xywg.iot.modular.station.model.ProjectMap;
import com.xywg.iot.modular.station.model.ProjectMapStation;
import com.xywg.iot.modular.station.service.IProjectAccuratePositionDataService;
import com.xywg.iot.modular.station.service.IProjectDeviceStockService;
import com.xywg.iot.modular.station.service.IProjectDeviceWorkerRecordService;
import com.xywg.iot.netty.NettyServer;
import com.xywg.iot.netty.handler.CommonStaticMethod;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.CompleteDataPojo;
import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.StationDTO;
import com.xywg.iot.netty.server.BaseAction;
import com.xywg.iot.netty.threads.impl.SimpleCalcThread;
import com.xywg.iot.netty.zbus.ZbusServer;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description 节点信息(精确定位)
 * Date: Created in 14:27 2019/2/25
 * Modified By : wangyifei
 */
@Component
public class NodePrecisionAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodePrecisionAction.class);

    private final  RedisUtil redisUtil;
    private final IProjectAccuratePositionDataService projectAccuratePositionDataService;
    private final IProjectDeviceStockService deviceStockService;
    private final IProjectDeviceWorkerRecordService projectDeviceWorkerRecordService;
    private final LoginAction loginAction;
    private final ZbusServer zbusServer;
    @Autowired
    public NodePrecisionAction(RedisUtil redisUtil, IProjectAccuratePositionDataService projectAccuratePositionDataService, IProjectDeviceStockService deviceStockService, IProjectDeviceWorkerRecordService projectDeviceWorkerRecordService, LoginAction loginAction, ZbusServer zbusServer) {
        this.redisUtil = redisUtil;
        this.projectAccuratePositionDataService = projectAccuratePositionDataService;
        this.deviceStockService = deviceStockService;
        this.projectDeviceWorkerRecordService = projectDeviceWorkerRecordService;
        this.loginAction = loginAction;
        this.zbusServer = zbusServer;
    }

    @Override
    public void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        LOGGER.info("节点信息(精确定位)");
        print(dataDomain);

       Map<String,String> map = dataDomain.getDataMap();

        //TODO 业务逻辑
        // 基站ID
        int stationId = Integer.parseInt(map.get("02"),16);
        // 标签序列号
        String tagNo  = CommonStaticMethod.decode(map.get("04"));
        // 数据序号
        int seqNum  = Integer.parseInt(map.get("05"),16);
        validLogin(ctx);
        // 子站编号
        String childNo = CommonStaticMethod.decode(map.get("03"));
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(ctx.channel());
        ProjectMapStation projectMapStation;
        ProjectMap projectMap;
        // 1. 缓存子站信息
        String slavekey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_slave,"sn",childNo);
        slavekey+= (":master:"+deviceConnectInfo.getSn());

        String mapKey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_map, "sn", deviceConnectInfo.getSn());


        if(!redisUtil.exists(slavekey)||!redisUtil.exists(mapKey)){
            Map<String,Object> map1 =  new HashMap<>(64);
            try {
                loginAction.validStation(
                        childNo,
                        map1);
                projectMapStation = (ProjectMapStation) map1.get("projectMapStation");
                projectMap = (ProjectMap) map1.get("projectMap");
                redisUtil.set(slavekey, JSONUtil.toJsonStr(projectMapStation));
                redisUtil.set(mapKey, JSONUtil.toJsonStr(projectMap));
            }catch (Exception ex){
                // 出异常就结束
                LOGGER.error(ex.getMessage());
                return ;
            }

        }
        projectMapStation = JSONUtil.toBean((String) redisUtil.get(slavekey), ProjectMapStation.class);
        projectMap = JSONUtil.toBean((String) redisUtil.get(mapKey), ProjectMap.class);

        //距离
        double length  =   Double.parseDouble(Integer.parseInt(map.get("06"),16)+"")/100;


        String key =  projectMap.getProjectId()+":"+tagNo+":"+seqNum;

                // 开启线程

                if(!BaseAction.exists(redisUtil,key,childNo,length,deviceConnectInfo.getSn())){
                    LOGGER.info(" 开启线程 key：{}" ,key);
                    NettyServer.threadPoolExecutor.execute(new SimpleCalcThread(key,redisUtil,projectAccuratePositionDataService,deviceConnectInfo,deviceStockService,projectDeviceWorkerRecordService,loginAction,zbusServer));
                }else{
                 key+=(":"+childNo);
            //存放30秒
                    LOGGER.info(" 存放 key：{}" ,key);
            redisUtil.setSec(key,length+","+deviceConnectInfo.getSn(),30L);
        }


        /**返回*/
        PositionHandler.responseErrorMessageHandle(ctx,packageData(deviceConnectInfo.getSn(),deviceConnectInfo.getStationId()),"00","00");
    }

    @Override
    public boolean supports(String code) {
        return "0005".equals(code);
    }


    /**
     *  回复同步时间
     * @param sn
     * @param stationId
     * @return
     */
    public static String  packageData(String sn,int stationId){
        StringBuffer sb =new StringBuffer();
        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 04 ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKAGE_RESPONSE+" ");

        // data length
        sb.append("00 23 ")
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
            String data   = sb.toString() ;
        String crc = doCrc(data);


        return changeCrc(sb.toString(),crc);
    }




    public static void main(String args[]){
        String d ="FF FF FE 01 00 07 00 01 ed 00 ## ## 00 13 01 58 59 32 30 31 39 30 32 31 32 30 32 30 30 30 31 00 04 02 03 00 05 03 01c2 01 c5 03 f0 fe 01 20 7d 44 04 08 31 05 04 08 33 05 04 08 37 05 04 08 3b 05 04 08 3f 05 04 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 43 05 04 08 45 05 04 08 00 00 00 00 35 a6 04 08 c1 40 04 08 97 44 04 08 97 44 04 08 97 44 04 08 71 0a 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 39 a9 04 08 b1 bb 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 b9 42 04 08 a1 b0 04 08 a5 01 05 08 97 44 04 08 4d 0a 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 e5 3e 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 97 44 04 08 00 f0 02 f8 00 f0 79 f8 0a a0 90 e8 00 0c 82 44 83 44 aa f1 01 07 da 45 01 d1 00 f0 6e f8 af f2 09 0e ba e8 0f 00 13 f0 01 0f 18 bf fb 1a 43 f0 01 03 18 47 dc de 03 00 fc de ";
        System.out.println( StrUtil.sub("XY20180c12020001","XY20180c12020001".length()-4,"XY20180c12020001".length()));
//        d= d.substring(36).replaceAll(" ","");
//
//        System.out.println(d);
        System.out.println(doCrc(d));
       // System.out.println(NodePrecisionAction.doCrc(d));

    }
}
