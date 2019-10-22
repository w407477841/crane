package com.xywg.equipment.monitor.iot.modular.ammeter.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.modular.ammeter.dao.ProjectAmmeterDailyMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.dao.ProjectElectricPowerDetailMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectAmmeterDaily;
import com.xywg.equipment.monitor.iot.modular.ammeter.service.IProjectAmmeterDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 电表每日统计 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
@Service
public class ProjectAmmeterDailyServiceImpl extends ServiceImpl<ProjectAmmeterDailyMapper, ProjectAmmeterDaily> implements IProjectAmmeterDailyService {

    @Autowired
    ProjectElectricPowerDetailMapper projectElectricPowerDetailMapper;

    private Integer times = 6;
    @Override
    public void daily() {
        // 查询所有电表

        Date now = new Date();
        daily(now);
    }

    public void daily(Date now){
        DateTime yestday = DateUtil.offsetDay(now,-1);
        DateTime yesdyestday = DateUtil.offsetDay(now,-2);
        String month = yestday.toString("yyyyMM");
        String daily = yestday.toString("yyyy-MM-dd");
        String daily1 = yesdyestday.toString("yyyy-MM-dd");
        List<Map<String,Object>> list =  baseMapper.last(month,daily,daily1);

        List<ProjectAmmeterDaily> projectAmmeterDailies = new ArrayList<>();
        for( Map<String,Object> map :  list){
            Integer deviceId = (Integer) map.get("id");
            BigDecimal  degree = (BigDecimal) map.get("degree");
            BigDecimal  lastDegree = (BigDecimal) map.get("lastDegree");
            BigDecimal  amountUsed = (BigDecimal) map.get("amountUsed");
            ProjectAmmeterDaily projectAmmeterDaily = new ProjectAmmeterDaily();
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
                    BigDecimal  min =  projectElectricPowerDetailMapper.getFirstInfo(month,daily,deviceId);
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

    public void dailyDevice(Date now,Integer id,Integer times){
        times++;
        DateTime yestday = DateUtil.offsetDay(now,-1);
        DateTime yesdyestday = DateUtil.offsetDay(now,-2);
        String month = yestday.toString("yyyyMM");
        String daily = yestday.toString("yyyy-MM-dd");
        String daily1 = yesdyestday.toString("yyyy-MM-dd");
        Map<String,Object> map =  baseMapper.lastDevice(month,daily,daily1,id).get(0);

        List<ProjectAmmeterDaily> projectAmmeterDailies = new ArrayList<>();
        Integer deviceId = (Integer) map.get("id");
        BigDecimal  degree = (BigDecimal) map.get("degree");
        BigDecimal  lastDegree = (BigDecimal) map.get("lastDegree");
        BigDecimal  amountUsed = (BigDecimal) map.get("amountUsed");
        ProjectAmmeterDaily projectAmmeterDaily = new ProjectAmmeterDaily();
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
                BigDecimal  min =  projectElectricPowerDetailMapper.getFirstInfo(month,daily,deviceId);
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
