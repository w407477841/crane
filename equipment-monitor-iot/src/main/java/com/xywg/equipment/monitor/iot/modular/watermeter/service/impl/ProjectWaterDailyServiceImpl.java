package com.xywg.equipment.monitor.iot.modular.watermeter.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectAmmeterDaily;
import com.xywg.equipment.monitor.iot.modular.watermeter.dao.ProjectWaterMeterDetailMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterDaily;
import com.xywg.equipment.monitor.iot.modular.watermeter.dao.ProjectWaterDailyMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.IProjectWaterDailyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电表每日统计
正常情况   读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 每日统计最后一条）
第一次      读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 明细当日第一条数据）
没有数据  读数=（用电量为每日统计最后一个读数） ，用电量 = 0 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
@Service
public class ProjectWaterDailyServiceImpl extends ServiceImpl<ProjectWaterDailyMapper, ProjectWaterDaily> implements IProjectWaterDailyService {
@Autowired
    ProjectWaterMeterDetailMapper   projectWaterMeterDetailMapper;

    private Integer times = 6;
    @Override
    public void daily() {


        Date now = new Date();
        DateTime yestday = DateUtil.offsetDay(now,-1);
        DateTime yesdyestday = DateUtil.offsetDay(now,-2);
        String month = yestday.toString("yyyyMM");
        String daily = yestday.toString("yyyy-MM-dd");
        String daily1 = yesdyestday.toString("yyyy-MM-dd");
        List<Map<String,Object>> list =  baseMapper.last(month,daily,daily1);

        List<ProjectWaterDaily> projectAmmeterDailies = new ArrayList<>();
        for( Map<String,Object> map :  list){
            Integer deviceId = (Integer) map.get("id");
            BigDecimal  degree = (BigDecimal) map.get("degree");
            BigDecimal  lastDegree = (BigDecimal) map.get("lastDegree");
            BigDecimal  amountUsed = (BigDecimal) map.get("amountUsed");
            ProjectWaterDaily projectAmmeterDaily = new ProjectWaterDaily();
            projectAmmeterDaily.setDeviceId(deviceId);
            projectAmmeterDaily.setStatisticsDate(yestday.toJdkDate());
            if(degree == null){
                // 当天没有数据
                if(lastDegree == null){
                    //昨天也没数据
                    projectAmmeterDaily.setAmountUsed(new BigDecimal(0));
                    projectAmmeterDaily.setDegree(new BigDecimal(0));
                    dailyDevice(DateUtil.offsetDay(now,-1),deviceId,0);
                }else{
                    //用电量设置为0
                    projectAmmeterDaily.setAmountUsed(new BigDecimal(0));
                    //读数设为昨日读数
                    projectAmmeterDaily.setDegree(lastDegree);
                }
                projectAmmeterDailies.add(projectAmmeterDaily);
            }else{
                if(lastDegree == null){
                    //第一次 当日最后最大-当日最小
                    BigDecimal  min =  projectWaterMeterDetailMapper.getFirstInfo(month,daily,deviceId);
                    projectAmmeterDaily.setAmountUsed(degree.subtract(min));
                    projectAmmeterDaily.setDegree(degree);
                    dailyDevice(DateUtil.offsetDay(now,-1),deviceId,0);
                }else{
                    //用电量设置为0
                    projectAmmeterDaily.setAmountUsed(degree.subtract(lastDegree));
                    //读数设为昨日读数
                    projectAmmeterDaily.setDegree(degree);
                }
                projectAmmeterDailies.add(projectAmmeterDaily);
            }
        }
        this.insertBatch(projectAmmeterDailies);
    }

    public void dailyDevice(Date now,Integer id,Integer times) {
        times++;
        DateTime yestday = DateUtil.offsetDay(now,-1);
        DateTime yesdyestday = DateUtil.offsetDay(now,-2);
        String month = yestday.toString("yyyyMM");
        String daily = yestday.toString("yyyy-MM-dd");
        String daily1 = yesdyestday.toString("yyyy-MM-dd");
        Map<String,Object> map =  baseMapper.lastDevice(month,daily,daily1,id).get(0);

        List<ProjectWaterDaily> projectAmmeterDailies = new ArrayList<>();
        Integer deviceId = (Integer) map.get("id");
        BigDecimal  degree = (BigDecimal) map.get("degree");
        BigDecimal  lastDegree = (BigDecimal) map.get("lastDegree");
        BigDecimal  amountUsed = (BigDecimal) map.get("amountUsed");
        ProjectWaterDaily projectAmmeterDaily = new ProjectWaterDaily();
        projectAmmeterDaily.setDeviceId(deviceId);
        projectAmmeterDaily.setStatisticsDate(yestday.toJdkDate());
        if(degree == null){
            // 当天没有数据
            if(lastDegree == null){
                //昨天也没数据
                projectAmmeterDaily.setAmountUsed(new BigDecimal(0));
                projectAmmeterDaily.setDegree(new BigDecimal(0));
                if(times < this.times) {
                    dailyDevice(DateUtil.offsetDay(now,-1),deviceId,times);
                }
            }else{
                //用电量设置为0
                projectAmmeterDaily.setAmountUsed(new BigDecimal(0));
                //读数设为昨日读数
                projectAmmeterDaily.setDegree(lastDegree);
            }
            projectAmmeterDailies.add(projectAmmeterDaily);
        }else{
            if(lastDegree == null){
                //第一次 当日最后最大-当日最小
                BigDecimal  min =  projectWaterMeterDetailMapper.getFirstInfo(month,daily,deviceId);
                projectAmmeterDaily.setAmountUsed(degree.subtract(min));
                projectAmmeterDaily.setDegree(degree);
                if(times < this.times) {
                    dailyDevice(DateUtil.offsetDay(now,-1),deviceId,times);
                }
            }else{
                //用电量设置为0
                projectAmmeterDaily.setAmountUsed(degree.subtract(lastDegree));
                //读数设为昨日读数
                projectAmmeterDaily.setDegree(degree);
            }
            projectAmmeterDailies.add(projectAmmeterDaily);
        }
        this.insertBatch(projectAmmeterDailies);
    }
}
