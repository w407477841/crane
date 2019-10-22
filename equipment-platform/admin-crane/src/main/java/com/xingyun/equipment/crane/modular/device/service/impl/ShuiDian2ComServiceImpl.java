package com.xingyun.equipment.crane.modular.device.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dao.*;
import com.xingyun.equipment.crane.modular.device.model.ProjectElectricPower;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeter;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeterDetail;
import com.xingyun.equipment.crane.modular.device.service.IProjectWaterDailyService;
import com.xingyun.equipment.crane.modular.device.service.IShuiDian2ComService;
import com.xingyun.equipment.crane.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import net.sf.jsqlparser.statement.select.First;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:56 2018/10/22
 * Modified By : wangyifei
 */
@Service
public class ShuiDian2ComServiceImpl implements IShuiDian2ComService {

    private static String [] WEEK = {"周日","周一","周二","周三","周四","周五","周六"};
    /**
     * 偏移量 , weeOfDay 返回的值 -1 后 = WEEK的index
     */
    private static final int OFFSET  = 1;
    /**
     * 从哪天开始算
     * -7 表示 昨天开始往前推6天
     * -6 表示 今天开始往前推6天
     */
    private static final int FIRST_DAY_OFFSET = -7;

    /**
     *  统计天数
     */
    private static final int COUNT  =  7 ;

    private static final int LIVING = 1;


    private static final int PRODUCTION = 2;


    private static final int FIRE  = 3;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShuiDian2ComServiceImpl.class);

    @Autowired
    ProjectWaterMeterMapper   waterMeterMapper;
    @Autowired
    ProjectElectricPowerMapper  electricPowerMapper;
    @Autowired
    ProjectWaterMeterAlarmMapper waterMeterAlarmMapper;
    @Autowired
    ProjectElectricPowerAlarmMapper  electricPowerAlarmMapper ;
    @Autowired
    ProjectElectricPowerDetailMapper  electricPowerDetailMapper;
    @Autowired
    ProjectWaterMeterDetailMapper  waterMeterDetailMapper;

    @Autowired
    ProjectInfoMapper  projectInfoMapper;
    @Autowired
    IProjectWaterDailyService  projectWaterDailyService;

    @Override
    public ResultDTO  shuiDianTotal(String uuid){
        Map<String,Object> data = new HashMap<>(10);
        Map<String,Object> water = new HashMap<>(10);
        Map<String,Object> power = new HashMap<>(10);

        Wrapper<ProjectInfo> projectInfoWrapper  = new EntityWrapper<>();
        projectInfoWrapper.eq("is_del",0);
        projectInfoWrapper.eq("uuid",uuid);
       List<ProjectInfo>  projectInfos =  projectInfoMapper.selectList(projectInfoWrapper);
       int projectId = 0;
       if(projectInfos!=null && projectInfos.size()>0){
            projectId = projectInfos.get(0).getId();
        }else{
           return new ResultDTO(false,null,"uuid不存在");
       }


        //水表正常数
        Wrapper<ProjectWaterMeter> waterWrapper  = new EntityWrapper();
        waterWrapper.eq("is_del",0);
        waterWrapper.eq("status",1);
        waterWrapper.eq("project_id",projectId);
        int on =   waterMeterMapper.selectCount(waterWrapper);
        waterWrapper  = new EntityWrapper();
        waterWrapper.eq("is_del",0);
        waterWrapper.eq("status",0);
        waterWrapper.eq("project_id",projectId);
        //未启用水表
        int off =  waterMeterMapper.selectCount(waterWrapper);
        water.put("on",on);
        water.put("off",off);

        //电表正常数
        Wrapper<ProjectElectricPower> powerWrapper = new EntityWrapper<>();
        powerWrapper.eq("is_del",0);
        powerWrapper.eq("status",1);
        powerWrapper.eq("project_id",projectId);
         on =   electricPowerMapper.selectCount(powerWrapper);
        powerWrapper  = new EntityWrapper();
        powerWrapper.eq("is_del",0);
        powerWrapper.eq("status",0);
        powerWrapper.eq("project_id",projectId);
        //未启用水表
         off =  electricPowerMapper.selectCount(powerWrapper);
        power.put("on",on);
        power.put("off",off);


        data.put("power",power);
        data.put("water",water);
        return new ResultDTO(true,data);
    }

    @Override
    public ResultDTO shuiDianEx(String uuid) {


        Map<String,Object>  data = new HashMap<>(10);
        // 构建 月份
        String month = DateUtil.format(new Date(),"yyyyMM");

        int water =  waterMeterAlarmMapper.shuiDianExs(month,uuid);

        int power = electricPowerAlarmMapper.shuiDianExs(month,uuid);

        data.put("water",water);
        data.put("power",power);

        return new ResultDTO(true,data);
    }

    @Override
    public ResultDTO getWaterTotal(String uuid) {
        Map<String,Object>  data = new HashMap<>(10);
        Date now = new Date();
            //生活用水
         Double living =  waterSevenDay(uuid,now,LIVING);
        //生产用水
        Double production =  waterSevenDay(uuid,now,PRODUCTION);
        //消防用水
        Double fire  =waterSevenDay(uuid,now,FIRE);
        data.put("living",living);
        data.put("production",production);
        data.put("fire",fire);


        return new ResultDTO(true,data);
    }

    @Override
    public ResultDTO getPowerTotal(String uuid) {
        Map<String,Object>  data = new HashMap<>(10);
        Date now = new Date();
        //生活用水
        Double living =  powerSevenDay(uuid,now,LIVING);
        //生产用水
        Double production =  powerSevenDay(uuid,now,PRODUCTION);

        data.put("living",living);
        data.put("production",production);


        return new ResultDTO(true,data);
    }

    /**
    * @author: wangyifei
    * Description: 7天用水量
    * Date: 10:33 2018/10/23
    */
    private Double waterSevenDay(String uuid,Date now, int type){
           //往前推7天
            String day7Ago = DateUtil.offsetDay(now,-7).toString("yyyy-MM-dd");
        return waterMeterDetailMapper.sum(uuid,day7Ago,type);
    }


    /**
     * @author: wangyifei
     * Description: 7天用电量
     * Date: 10:33 2018/10/23
     */
    private Double powerSevenDay(String uuid,Date now, int type){
        //往前推7天
        String day7Ago = DateUtil.offsetDay(now,-7).toString("yyyy-MM-dd");
        return electricPowerDetailMapper.sum(uuid,day7Ago,type);
    }

    @Override
    public ResultDTO getWaterLineCharts(String uuid,Integer type) {
        Date now = new Date();
        //往前推7天
        String day7Ago = DateUtil.offsetDay(now,-7).toString("yyyy-MM-dd");

        return new ResultDTO(true,waterMeterDetailMapper.eveSum(uuid,day7Ago,type));
    }

    @Override
    public ResultDTO getPowerLineCharts(String uuid, Integer type) {

        Date now = new Date();
        //往前推7天
        String day7Ago = DateUtil.offsetDay(now,-7).toString("yyyy-MM-dd");

        return new ResultDTO(true,electricPowerDetailMapper.eveSum(uuid,day7Ago,type));
    }












}

