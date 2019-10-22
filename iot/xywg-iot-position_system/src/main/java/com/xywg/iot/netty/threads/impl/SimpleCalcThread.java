package com.xywg.iot.netty.threads.impl;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.common.utils.KeyUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.common.utils.arithmetic.BaseArithmetic;
import com.xywg.iot.common.utils.arithmetic.CentroidArithmetic;
import com.xywg.iot.modular.station.model.*;
import com.xywg.iot.modular.station.service.IProjectAccuratePositionDataService;
import com.xywg.iot.modular.station.service.IProjectDeviceStockService;
import com.xywg.iot.modular.station.service.IProjectDeviceWorkerRecordService;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.Round;
import com.xywg.iot.netty.models.StationDTO;
import com.xywg.iot.netty.server.impl.LoginAction;
import com.xywg.iot.netty.threads.BaseCalcThread;
import com.xywg.iot.netty.zbus.WebsocketDTO;
import com.xywg.iot.netty.zbus.ZbusServer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:40 2019/2/28
 * Modified By : wangyifei
 */
public class SimpleCalcThread extends BaseCalcThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCalcThread.class);

    private final RedisUtil redisUtil;
    private final IProjectAccuratePositionDataService  projectAccuratePositionDataService;
    private final DeviceConnectInfo deviceConnectInfo;
    private final IProjectDeviceStockService deviceStockService;
    private final IProjectDeviceWorkerRecordService projectDeviceWorkerRecordService;
    private final LoginAction loginAction;
    private final ZbusServer zbusServer;

    public SimpleCalcThread(String key, RedisUtil redisUtil, IProjectAccuratePositionDataService projectAccuratePositionDataService, DeviceConnectInfo deviceConnectInfo, IProjectDeviceStockService deviceStockService, IProjectDeviceWorkerRecordService projectDeviceWorkerRecordService, LoginAction loginAction, ZbusServer zbusServer) {
        super(key);
        this.redisUtil = redisUtil;
        this.projectAccuratePositionDataService = projectAccuratePositionDataService;
        this.deviceConnectInfo = deviceConnectInfo;
        this.deviceStockService = deviceStockService;
        this.projectDeviceWorkerRecordService = projectDeviceWorkerRecordService;
        this.loginAction = loginAction;
        this.zbusServer = zbusServer;
    }

    @Override
    protected void run(String key) {
        try {
            // 休眠2秒 保证数据全部接受完毕
            Thread.sleep(14000);
            // 查询 缓存内 包含 参数开头的
            Set<String> keys =  redisUtil.keys(key+":*");
            List<Round> rounds = new ArrayList<>();
            Iterator<String> t  = keys.iterator();
            ProjectMap projectMap = null;
            while(t.hasNext()){
                String tempKey = t.next();
                String stationNO = tempKey.split(":")[3];
                String []   data = ((String) redisUtil.get(tempKey)).split(",");
                DeviceConnectInfo deviceConnectInfo  = NettyChannelManage.getDeviceCodeByChannel(NettyChannelManage.getChannel(data[1])) ;
                ProjectMapStation projectMapStation;
                // 1. 缓存子站信息
                String slavekey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_slave,"sn",stationNO);
                slavekey+= (":master:"+deviceConnectInfo.getSn());
                String mapKey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_map, "sn", deviceConnectInfo.getSn());
                if(!redisUtil.exists(slavekey)){
                    Map<String,Object> map1 =  new HashMap<>();
                    try {
                        loginAction.validStation(
                                stationNO,
                                map1);
                    } catch (Exception e) {
                        LOGGER.info(e.getMessage());
                        continue;
                    }
                    projectMapStation = (ProjectMapStation) map1.get("projectMapStation");
                    projectMap = (ProjectMap) map1.get("projectMap");
                    redisUtil.set(slavekey, JSONUtil.toJsonStr(projectMapStation));
                    redisUtil.set(mapKey, JSONUtil.toJsonStr(projectMap));
                }
                projectMapStation = JSONUtil.toBean((String) redisUtil.get(slavekey), ProjectMapStation.class);
                projectMap = JSONUtil.toBean((String) redisUtil.get(mapKey), ProjectMap.class);
                Round round  =new Round(projectMapStation.getxZhou().doubleValue(),projectMapStation.getyZhou().doubleValue(),Double.parseDouble(data[0]));
                LOGGER.info("key:{}  data:{}",tempKey,round.toString());
                rounds.add(round);

            }
            //使用质心法计算
            BaseArithmetic arithmetic = null;
            try {

                arithmetic = BaseArithmetic.factory(CentroidArithmetic.class,false);
                Coordinate avgZxl1 = null;
                try {
                    avgZxl1  = arithmetic.exe(rounds);
                   }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                if(avgZxl1==null){
                    LOGGER.error("非斜边 计算失败");
                    return;
                }else{
                    LOGGER.info("非斜边 质心法 key:{} 点位信息:{}",key, avgZxl1.toString());
                    ProjectMap map =projectMap;
                    BigDecimal minX  = new BigDecimal(0);
                    BigDecimal maxX  = map.getxZhou();
                    BigDecimal minY  = new BigDecimal(0);
                    BigDecimal maxY = map.getyZhou();
                    if(avgZxl1.getX()>minX.doubleValue()
                            &&avgZxl1.getX()<maxX.doubleValue()
                            &&avgZxl1.getY()>minY.doubleValue()
                            &&avgZxl1.getY()<maxY.doubleValue()
                            ){

                    }else{
                        LOGGER.info("点位超出边界 X{}->{},Y{}->{} 当前数据:{}",minX,maxX,minY,maxY, avgZxl1.toString());
                        return ;
                    }

                }
                String tagNo =  key.split(":")[1];
                ProjectDevice device =  deviceStockService.selectTagByDeviceNo(tagNo);
                String firstKey = KeyUtil.key(KeyUtil.database,KeyUtil.table_data_first,"tag",tagNo) ;
                String secondKey = KeyUtil.key(KeyUtil.database,KeyUtil.table_data_second,"tag",tagNo) ;
                if(redisUtil.exists(firstKey)&&redisUtil.exists(secondKey)){
                    //至少已近取了3个点以上了

                    Coordinate   first  = JSONUtil.toBean((String)redisUtil.get(firstKey),Coordinate.class);

                    Coordinate   second  = JSONUtil.toBean((String)redisUtil.get(secondKey),Coordinate.class);
                    //当前点与前前个点的距离
                    Double length1 = length(avgZxl1,first);
                    //当前点与前个点的距离
                    Double length2 =  length(avgZxl1,second);

                    //  前前个点用前一个点覆盖
                    redisUtil.set(firstKey,JSONUtil.toJsonStr(second),60L);
                    //  前一个点用当前点覆盖
                    // 异常 java.lang.IllegalArgumentException: Number is non-finite! ， 由于 出现了 NAN
                    redisUtil.set(secondKey,JSONUtil.toJsonStr(avgZxl1),60L);

                    /*需要保存到库的数据使用距离当前点近的点*/
                    if(length1>= length2){
                        LOGGER.info("当前点{} 距离近的点{}",avgZxl1.toString(),second.toString());
                        avgZxl1 = second;
                    }else {
                        LOGGER.info("当前点{} 距离近的点{}",avgZxl1.toString(),first.toString());
                        avgZxl1 = first;
                    }
                }else if(redisUtil.exists(firstKey)){
                    // 说明 第二包数据
                    redisUtil.set(secondKey,JSONUtil.toJsonStr(avgZxl1),60L);
                }else{
                    redisUtil.set(firstKey,JSONUtil.toJsonStr(avgZxl1),60L);
                    // 说明是第一包数据
                }
                insertData(tagNo,device.getId(),avgZxl1,projectMap);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     *  插入数据
     * @param tagNo
     * @param deviccId
     * @param coordinate
     */
    public void insertData(String tagNo,Integer deviccId,Coordinate coordinate,ProjectMap map){

            ProjectDeviceWorkerRecord workerRecord = projectDeviceWorkerRecordService.selectByTagId(deviccId,tagNo,map.getProjectId());
            Date now =  new Date();
            ProjectAccuratePositionData positionData =new ProjectAccuratePositionData();
            positionData.setCollectTime(now);
            positionData.setMapId(map.getId());
            positionData.setIsDel(0);
            positionData.setLabelNo(tagNo);
            positionData.setIdentityCode(workerRecord.getIdCardNumber());
            positionData.setxZhou(new BigDecimal(coordinate.getX()));
            positionData.setyZhou(new BigDecimal(coordinate.getY()));

            String  topciIdCardNumber = "/topic/positiondata/identitycode/"+workerRecord.getIdCardNumber();
            String data = JSONUtil.toJsonStr(coordinate);
            zbusServer.send(new WebsocketDTO(data,topciIdCardNumber));

            projectAccuratePositionDataService.insert(positionData);

    }

    public double  length(Coordinate x1,Coordinate x2){
       return  Math.sqrt(Math.pow(x1.getX() - x2.getX(), 2) + Math.pow(x1.getY() - x2.getY(), 2));
    }
}
