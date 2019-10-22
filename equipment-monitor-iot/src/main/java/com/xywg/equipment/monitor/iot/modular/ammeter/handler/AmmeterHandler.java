package com.xywg.equipment.monitor.iot.modular.ammeter.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.core.util.OriDataUtil;
import com.xywg.equipment.monitor.iot.modular.ammeter.dao.ProjectElectricPowerAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.dao.ProjectElectricPowerDetailMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.*;
import com.xywg.equipment.monitor.iot.modular.ammeter.service.*;
import com.xywg.equipment.monitor.iot.modular.base.handler.HexBaseDevice;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;

import com.xywg.equipment.monitor.iot.netty.aop.HexPowerHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:54 2018/9/11
 * Modified By : wangyifei
 */
@Component
public class AmmeterHandler extends HexBaseDevice {

    //0x33 数据加减量
    private final int CHANGE_NUM = 51;
    //预警系数
    private final float RATIO = 0.9f;
    //告警 即实时电表度数大于阈值
    private final int CODE_ALERM_OVERFLOW = 1;
    //预警 即实时电表度数大于阈值90%
    private final int CODE_ALERM_EARLY = 2;
    //告警的中文
    private final String TRANS_ALERM_OVERFLOW = "用电报警";
    //预警的中文
    private final String TRANS_ALERM_EARLY = "用电预警";
    //设备类型
    private final String DEVICE_TYPE = "ammeter";
    //设备名称
    private final String DEVICE_NAME = "电表设备";


    @Autowired
    IProjectElectricPowerDetailService ipepdService;

    @Autowired
    IProjectElectricPowerService ipepService;

    @Autowired
    IProjectElectricPowerAlarmService ipepaService;

    @Autowired
    IProjectInfoService ipiService;

    @Autowired
    ProjectElectricPowerDetailMapper pepdMapper;

    @Autowired
    ProjectElectricPowerAlarmMapper pepaMapper;

    @Autowired
    IProjectElectricPowerHeartbeatService ipephService;

    @Autowired
    IProjectElectricPowerOriginalService ipepoService;


    /**
     * 解析数据
     * @param message 消息
     * @param ctx
     * @return
     */
    @Override
    protected Object resolver(String message, ChannelHandlerContext ctx) {

        //ipepdService.getYearOnYear();

        String regData =  "(FE)*68(\\w{12})68(\\w{22})16(FE)*";
       // String regData =  "(FE)*68(\\w{12})68910833333333(\\w{8})(\\w{2})16(FE)*";
        String regHead =  "(\\w{12})+";
        Pattern pattern = Pattern.compile(regData);
        Matcher matcher  =pattern.matcher(message);

        Map<String,String> dataMap = new HashMap<String,String>();
        System.out.println("#######################################");
        System.out.println("###############电表原始："+message);
        System.out.println("#######################################");

        //根据正则匹配整条message数据
        if(matcher.find()){

            OriDataUtil.DB_LOGGER.info(message);
            System.out.println("===数据处理=============================================================");
            System.out.println("进入数据处理");
            //匹配到则取组中地址域和数据域数据
            String addr = matcher.group(2).trim();

            //取反
            addr = doPathSort(addr);
            System.out.println(addr);
            String data = matcher.group(3).trim();
            System.out.println(data);
            dataMap.put("addr",addr);
            dataMap.put("data",data);

            //将流程控制设置为true
            setTrue(true);

        }else{
            // FA 07 13 02 FA 02 28 4E 68 09 6A 48 FA FF
            //Pattern pattern1 = Pattern.compile(regHead);
            //Matcher matcher1 = pattern1.matcher(message);
            if(Pattern.matches(regHead,message)){

                int length = message.length();
                int count  =  length/12;
                for(int  i = 0 ; i < count ; i++){
                    String addr=   StrUtil.sub(message,i*12,(i+1)*12);
                    System.out.println(addr+"已加入");
                    HexPowerHandler.ameterChannel.put(addr,ctx.channel());
                }

            }
        }
        return dataMap;

    }

    /**
     * 获取项目信息
     * @param data
     * @return
     */
    @Override
    protected ProjectInfo project(Object data) {
        ProjectInfo pi = null;

        //如果解析到数据
        if(null != data){
            ProjectElectricPower pep = (ProjectElectricPower)data;
            pi = ipiService.selectById(pep.getProjectId());
        }
        return pi;
    }

    /**
     * 获取设备信息
     * @param data
     * @return
     */
    @Override
    protected Object config(Object data) {

        Map<String,String> dataMap = (Map<String,String>) data;

        String deviceNo = dataMap.get("addr");

        ProjectElectricPower pep = ipepService.getBaseInfo(deviceNo);

        return pep;
    }

    /**
     * 进行心跳操作
     * @param config
     */
    @Override
    protected void insertHeartbeat(Object config) {
        System.out.println("有设备吗?"+(null!=config));
        if(null != config){

            ProjectElectricPower pep = (ProjectElectricPower)config;
            System.out.println("heartBeat设备eleId:"+pep.getId());
            String keyHeartBeatRedis = "device_platform:head:dbht:"+pep.getDeviceNo();
            if(redisUtil.exists(keyHeartBeatRedis)){
                //更新心跳老数据的结束时间
                //ProjectElectricPowerHeartbeat pephOri = ipephService.getLastInfo(pep.getId());
                ProjectElectricPowerHeartbeat pephOri = (ProjectElectricPowerHeartbeat)redisUtil.get(keyHeartBeatRedis);
                if(null != pephOri){
                    pephOri.setEndTime(new Date());
                    pephOri.setStatus(1);
                    ipephService.updateById(pephOri);
                    redisUtil.setSec(keyHeartBeatRedis,pephOri,45L);
                }
            }else{
                Date tempDate = new Date();
                //更新心跳老数据的结束时间
                ProjectElectricPowerHeartbeat pephOri = ipephService.getLastInfo(pep.getId());
                if(null != pephOri){

                    ProjectElectricPowerHeartbeat pephOffLine = new ProjectElectricPowerHeartbeat();
                    pephOffLine.setStatus(0);
                    pephOffLine.setEndTime(tempDate);
                    pephOffLine.setIsDel(0);
                    pephOffLine.setElectricId(pep.getId());
                    pephOffLine.setCreateTime(pephOri.getEndTime());

                    ipephService.insert(pephOffLine);
                }

                //新增心跳数据
                ProjectElectricPowerHeartbeat peph = new ProjectElectricPowerHeartbeat();
                peph.setCreateTime(tempDate);
                peph.setEndTime(tempDate);
                peph.setElectricId(pep.getId());
                peph.setIsDel(0);
                peph.setStatus(1);
                ipephService.insert(peph);
                //更新至redis
                redisUtil.setSec(keyHeartBeatRedis,peph,45L);
            }
        }else{
            System.out.println("config:没有设备");
        }

    }

    /**
     * 插入数据域信息
     * @param config
     * @param data
     * @return
     */
    @Override
    protected Object insertData(Object config, Object data) {
        if(null != config){
            Map<String,String> dataMap = (Map<String,String>) data;


            ProjectElectricPower pep = (ProjectElectricPower) config;
            System.out.println("项目ID:"+pep.getProjectId());

            System.out.println("获取设备地址："+dataMap.get("addr"));

            //电表当前数据
            ProjectElectricPowerDetail pepdNow = doDataPrising(dataMap.get("data"));
            pepdNow.setIsDel(0);
            pepdNow.setElectricId(pep.getId());
            pepdNow.setActualDegree(pepdNow.getCurrent().multiply(pep.getRatio()));
            //插入detail表
            //ipepdService.insert(pepdNow);

            pepdMapper.create(pepdNow, BaseFactory.getTableName(ProjectElectricPowerDetail.class));

            doInsertOriData(dataMap);

            return pepdNow;
        }else{
            System.out.println("没有设备数据");
            return null;
        }
    }

    /**
     * 存入发送接收的数据记录
     * @param dataMap
     */
    private void doInsertOriData(Map<String,String> dataMap){
        //发送数据
        String sendData = HexPowerHandler.sendAmeterMessage.get(dataMap.get("addr"));
        //接收数据
        String receiveData = dataMap.get("data");

        ProjectElectricPowerOriginal pepo = new ProjectElectricPowerOriginal();
        pepo.setCreateTime(new Date());
        pepo.setDeviceTime(new Date());
        pepo.setIsDel(0);
        pepo.setReceiveData(receiveData);
        pepo.setSendData(sendData);

        boolean isTrue = ipepoService.insert(pepo);
        if(isTrue){
            //插入成功则删除历史数据
            HexPowerHandler.sendAmeterMessage.remove(dataMap.get("addr"));
        }

    }

    /**
     * 解析数据进行告警操作
     * @param config
     * @param data
     * @param project
     * @param detail
     */
    @Override
    public ProjectElectricPowerAlarm insertAlarm(Object config, Object data,Object project,Object detail) {
        ProjectElectricPowerAlarm pepa = null;
        if(null != project && null != config){
            //数据map
            Map<String,String> dataMap = (Map<String,String>) data;
            //设备信息
            ProjectElectricPower pep = (ProjectElectricPower)config;
            //项目信息
            ProjectInfo pi = (ProjectInfo) project;

            //设置detail的redis key
            String redisDetailKey = "device_platform:current:"+pi.getUuid()+":ammeter:"+dataMap.get("addr");

            System.out.println("获取设备地址："+dataMap.get("addr"));

            //电表当前数据
            ProjectElectricPowerDetail pepdNow = (ProjectElectricPowerDetail) detail;
            Date nowCreateTime = pepdNow.getCreateTime();
            BigDecimal nowCurrent = pepdNow.getCurrent();

            System.out.println("##当前数据:"+nowCurrent);

            System.out.println("是否有上一次记录数据："+redisUtil.exists(redisDetailKey));

            //从redis中获取数据
            Object pepdReids = redisUtil.get(redisDetailKey);

            //将新数据更新至redis
            //redisUtil.set(redisDetailKey,pepdNow);

            this.pushRedis(redisUtil,dataMap.get("addr"),"ammeter",pi.getUuid(),pepdNow,""+pi.getId());

            //如果有上一次数据记录
            if(null != pepdReids){
                ProjectElectricPowerDetail pepdOri = (ProjectElectricPowerDetail)JSONUtil.toBean(""+pepdReids,ProjectElectricPowerDetail.class);

                //电表上一次数据
                Date oriCreateTime = pepdOri.getCreateTime();
                BigDecimal oriCurrent = pepdOri.getCurrent();

                System.out.println("nowCreateTime.getTime()-oriCreateTime.getTime():"+(nowCreateTime.getTime()-oriCreateTime.getTime()));

                long dateLen = nowCreateTime.getTime()-oriCreateTime.getTime();
                System.out.println("时差："+dateLen);

                //计算每秒度数
                BigDecimal perCurrent = nowCurrent.subtract(oriCurrent).divide(new BigDecimal(dateLen<1000?0:dateLen/1000),2,ROUND_HALF_UP);
                System.out.println("perCurrent:"+perCurrent+"    idssipation:"+pep.getDissipation()+"    "+perCurrent.compareTo(pep.getDissipation()));
                if(perCurrent.compareTo(pep.getDissipation())==1){
                    //当前每秒读度数大于阈值
                    //报警了
                    System.out.println("报警了");
                    pepa = new ProjectElectricPowerAlarm();
                    pepa.setAlarmId(CODE_ALERM_OVERFLOW);
                    pepa.setAlarmInfo(TRANS_ALERM_OVERFLOW);
                    pepa.setCreateTime(new Date());
                    pepa.setElectricId(pep.getId());
                    pepa.setDetailId(pepdNow.getId());
                    pepa.setIsDel(0);
                    pepa.setStatus(0);
                    //ipepaService.insert(pepa);
                    pepaMapper.create(pepa, BaseFactory.getTableName(ProjectElectricPowerAlarm.class));

                }else if(perCurrent.compareTo(pep.getDissipation().multiply(new BigDecimal(RATIO)))==1){
                    //否则当前每秒度数大于90%阈值
                    //预警了
                    System.out.println("预警了");
                    pepa = new ProjectElectricPowerAlarm();
                    pepa.setAlarmId(CODE_ALERM_EARLY);
                    pepa.setAlarmInfo(TRANS_ALERM_EARLY);
                    pepa.setCreateTime(new Date());
                    pepa.setElectricId(pep.getId());
                    pepa.setDetailId(pepdNow.getId());
                    pepa.setIsDel(0);
                    pepa.setStatus(0);
                    //ipepaService.insert(pepa);
                    pepaMapper.create(pepa, BaseFactory.getTableName(ProjectElectricPowerAlarm.class));
                }else{
                    //没事
                    System.out.println("没事了");
                }
            }else{
                System.out.println("没有上一次数据");
            }
        }
        return pepa;

    }

    @Override
    protected String getModular() {
        return DEVICE_TYPE;
    }

    @Override
    protected String getDeviceName() {
        return DEVICE_NAME;
    }

    /**
     * 对数据域数据进行校验
     * @author SJ
     * @param msg
     * @return
     */
    private ProjectElectricPowerDetail doDataPrising(String msg){
        System.out.println(msg);
        ProjectElectricPowerDetail pepd = new ProjectElectricPowerDetail();

        //接收的原始数据
        String eleValueOri = msg.substring(12,msg.length()-2);
        //接收的原始控制码
        String eleCtrlCodeOri = msg.substring(0,2);
        //接收的数据长度
        String eleValueLen = msg.substring(2,4);
        //数据类型码
        String eleValueType = msg.substring(4,12);

        //对原始数据进行解析
        String eleValue = "";
        System.out.println("数据："+eleValueOri);
        for (int i = 0; i < eleValueOri.length()/2; i++) {
            String tempHex = eleValueOri.substring((i*2),(i+1)*2);
            tempHex = reduceChangeNum(tempHex);
            eleValue = tempHex + eleValue;
            System.out.println("###:"+eleValue);
        }

        //对数据进行实例化
        Date nowDate = new Date();
        //设置当前电量
        String v1 = eleValue.substring(0,eleValue.length()-2);
        String v2 = eleValue.substring(eleValue.length()-2,eleValue.length());
        pepd.setCurrent(new BigDecimal(v1+"."+v2));
        //设置当前时间
        pepd.setCreateTime(nowDate);
        pepd.setDeviceTime(nowDate);
        //将控制码和数据类型作为备注
        pepd.setComments(eleCtrlCodeOri+eleValueType);

        return pepd;
    }


    /**
     * 减偏移量 接收数据用到
     * @param msg
     * @return
     */
    private String reduceChangeNum(String msg){
        msg = Integer.toHexString(Integer.parseInt(msg,16)-CHANGE_NUM);
        if(msg.length()<2){
            msg = "0"+msg;
        }
        return msg;
    }

    /**
     * 加偏移量 发送数据用到
     * @param msg
     * @return
     */
    private String addChangeNum(String msg){
        msg = Integer.toHexString(Integer.parseInt(msg,16)+CHANGE_NUM);
        if(msg.length()<2){
            msg = "0"+msg;
        }
        return msg;
    }

    /**
     * 地址顺序去反
     * @param str
     * @return
     */
    private String doPathSort(String str){
        String result = "";
        for (int i = 0; i < str.length()/2; i++) {
            result = str.substring(i*2,(i+1)*2) + result;
        }
        return result;

    }

    public static void main(String[] args){
        String msg = "9108333333333645333359";
        AmmeterHandler am = new AmmeterHandler();
        ProjectElectricPowerDetail pepd = am.doDataPrising(msg);
        System.out.println(pepd.getCurrent());

    }


}
