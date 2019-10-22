package com.xingyun.equipment.simpleequipment.receive.dust;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xingyun.equipment.simpleequipment.core.pojo.CompleteDataPojo;
import com.xingyun.equipment.simpleequipment.core.pojo.DuctDataPojoVo;
import com.xingyun.equipment.simpleequipment.core.properties.XywgProerties;
import com.xingyun.equipment.simpleequipment.receive.handler.CommonMethod;
import com.xingyun.equipment.simpleequipment.receive.modular.envmon.factory.MonitorAlarmFactory;
import com.xingyun.equipment.simpleequipment.receive.modular.envmon.model.ProjectEnvironmentMonitorAlarm;
import com.xingyun.equipment.simpleequipment.receive.modular.envmon.model.ProjectEnvironmentMonitorDetail;
import com.xingyun.equipment.simpleequipment.receive.modular.envmon.service.IProjectEnvironmentMonitorAlarmService;
import com.xingyun.equipment.simpleequipment.receive.modular.envmon.service.IProjectEnvironmentMonitorDetailService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.xingyun.equipment.simpleequipment.core.Const.DATA_PACKAGE_RESPONSE;


/**
 * @author hjy
 * @date 2018/9/20
 * 环境监控 业务逻辑处理服务类(扬尘)
 */
@Service
public class DustBusinessLogicService {

    @Autowired
    private DustHandlerService monitorHandlerService;
    @Autowired
    private IProjectEnvironmentMonitorDetailService monitorDetailService;
    @Autowired
    private IProjectEnvironmentMonitorAlarmService monitorAlarmService;


    @Autowired
    protected XywgProerties xywgProerties;

    @Autowired
    private CommonMethod commonMethod;

    private Logger logger = LoggerFactory.getLogger(DustBusinessLogicService.class);

    /**
     * 登录 只包含(sn,firmware)
     *
     * @param monitorPojo
     */
    public void login(Channel ctx, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);



    }

    /**
     * 心跳
     *
     * @param monitorPojo
     */
    public void heartbeat(Channel ctx, CompleteDataPojo monitorPojo, String dataStrString) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }

    /**
     * 同步时间
     *
     * @param monitorPojo
     */
    public void synchronizationTime(Channel ctx, CompleteDataPojo monitorPojo) {
        String todayString = "";
        String tomorrowString = "";
        //序列号 16进制的:
        String sn = monitorPojo.getDataMap().get("01");

        //commonMethod.synchronizationTime(ctx, sn, monitorPojo.getCommand(), monitorPojo.getVersion());
        //获取服务器当前时间
        String dateTimeStr = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        //02 代表功能数据的id  即回复的时间
        linkedHashMap.put("02", dateTimeStr);
        if (StrUtil.isNotBlank(todayString)) {
            linkedHashMap.put("03", todayString);
        }
        if (StrUtil.isNotBlank(tomorrowString)) {
            linkedHashMap.put("04", tomorrowString);
        }
        commonMethod.resMessageJoint(ctx, sn, linkedHashMap, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

    }


    /**
     * 传输升级数据
     *
     * @param monitorPojo
     */
    public void upgradeTransferData(Channel ctx, CompleteDataPojo monitorPojo) {
        commonMethod.upgradeTransfer(ctx, monitorPojo);
    }

    /**
     * 升级文件发送完毕回复
     *
     * @param monitorPojo
     */
    public void upgradeFileSentOver(CompleteDataPojo monitorPojo) {
        //暂时不做处理


    }

    /**
     * 上传设备异常日志
     *
     * @param monitorPojo
     */
    public void exceptionalLog(Channel ctx, CompleteDataPojo monitorPojo) {
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
     * 上传监控数据 (sn,windSpeed,windForce,windDirection,PM2.5,PM10,temperature,humidity,noise)
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorData(Channel ctx, CompleteDataPojo monitorPojo) {
        uploadMonitorData(ctx,monitorPojo,false);
    }
    /**
     * 上传监控数据 (sn,windSpeed,windForce,windDirection,PM2.5,PM10,temperature,humidity,noise)
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorData(Channel ctx, CompleteDataPojo monitorPojo,boolean needDispatch) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(ctx, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        DuctDataPojoVo ductDataPojoVo = new DuctDataPojoVo();
        BeanUtils.copyProperties(monitorPojo, ductDataPojoVo);
        /*0x01	序列号	STRING 0x02	风速	STRING 0x03	风力	STRING  0x04	风向	STRING  0x05	PM2.5	STRING
        0x06	PM10	STRING 0x07	温度	STRING 0x08	湿度	STRING  0x09	噪声	STRING*/
        //将key  转为属性
        for (String key : monitorPojo.getDataMap().keySet()) {
            ductDataPojoVo = monitorHandlerService.getMonitorPojoData(ductDataPojoVo, key, monitorPojo.getDataMap().get(key));
        }

        ProjectEnvironmentMonitorDetail projectEnvironmentMonitorDetail = new ProjectEnvironmentMonitorDetail(
                null,
                ductDataPojoVo.getSn(),
                Double.parseDouble(ductDataPojoVo.getPm10()),
                Double.parseDouble(ductDataPojoVo.getPm25()),
                Double.parseDouble(ductDataPojoVo.getNoise()),
                Double.parseDouble(ductDataPojoVo.getWindSpeed()),
                ductDataPojoVo.getWindDirection(),
                new Date(),
                Double.parseDouble(ductDataPojoVo.getTemperature()),
                Double.parseDouble(ductDataPojoVo.getHumidity()),
                Double.parseDouble(ductDataPojoVo.getWindForce()),
                null,
                null,
                0,
                new Date(),
                null
        );



        //保存解析后的数据
        monitorDetailService.insert(projectEnvironmentMonitorDetail);
        try {

            //比较阈值看是否满足报警条件 如果满足那么存到报警信息表去
            // 比对 阈值
            List<ProjectEnvironmentMonitorAlarm> alarms = MonitorAlarmFactory.factory(xywgProerties, projectEnvironmentMonitorDetail);
            if(!CollectionUtils.isEmpty(alarms)){
                monitorAlarmService.insertBatch(alarms);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    private static String getGBK16(String gbkString) {
        StringBuilder sb = new StringBuilder();
        try {

            byte[] ssChar = gbkString.getBytes("GBK");
            for (byte b : ssChar) {
                sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
            }
            //System.out.println(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 拼接每天的天气信息
     *
     * @param map
     * @return
     */
    private String getWeatherStr(Map map) {
        String weather = map.get("weather").toString();
        String wind = map.get("wind").toString();
        String temperature = map.get("temperature").toString();
        //获得第一段的内码
        String oneString = getGBK16(weather);
        //获得第二段的内码
        String twoString = getGBK16(wind);
        //获得第三段的内码
        String threeString = getGBK16(temperature);
        // System.out.println(getSectionCode(oneString)+"---"+getSectionCode(twoString)+"---"+getSectionCode(threeString) );
        return getSectionCode(oneString) + getSectionCode(twoString) + getSectionCode(threeString);
    }

    /**
     * 组装每一段编码  每一段的字符长度为16字节  不足时 前后补空格 字符在中间
     * 字符空格对应的 16对应 为0x20
     */
    private String getSectionCode(String info) {

        Integer maxLength = 16;
        //一个字节转化字符型后占2位
        Integer k = 2;
        //一共需要拼接的空格数
        Integer number = (maxLength * k - info.length()) / 2;
        //因为文字需要居中,所以前后各拼接一半的字符串   例如一共需要拼接5个空格,这个时间就前2后3
        StringBuilder sb = new StringBuilder();
        if (number % 2 == 0) {
            for (int i = 0; i < number / 2; i++) {
                sb.append("20");
            }
            sb.append(info);
            for (int i = 0; i < number / 2; i++) {
                sb.append("20");
            }
        } else {
            for (int i = 0; i < (number - 1) / 2; i++) {
                sb.append("20");
            }
            sb.append(info);
            for (int i = 0; i < (number + 1) / 2; i++) {
                sb.append("20");
            }
        }

        return sb.toString();
    }








}