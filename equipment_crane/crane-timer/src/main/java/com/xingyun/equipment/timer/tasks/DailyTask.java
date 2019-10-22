package com.xingyun.equipment.timer.tasks;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.timer.config.properties.XywgProerties;
import com.xingyun.equipment.timer.model.ProjectCrane;
import com.xingyun.equipment.timer.model.ProjectCraneAlarm;
import com.xingyun.equipment.timer.model.ProjectCraneCyclicWorkDuration;
import com.xingyun.equipment.timer.model.ProjectCraneStatisticsDaily;
import com.xingyun.equipment.timer.service.IProjectCraneAlarmService;
import com.xingyun.equipment.timer.service.IProjectCraneCyclicWorkDurationService;
import com.xingyun.equipment.timer.service.IProjectCraneService;
import com.xingyun.equipment.timer.service.IProjectCraneStatisticsDailyService;
import com.xingyun.equipment.timer.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:02 2019/6/19
 * Modified By : wangyifei
 */
@Component
@Slf4j
public class DailyTask {
    @Autowired
    private IProjectCraneService projectCraneService;
    @Autowired
    private IProjectCraneAlarmService alarmService;
    @Autowired
    private IProjectCraneStatisticsDailyService dailyService;
    @Autowired
    private IProjectCraneCyclicWorkDurationService craneCyclicWorkDurationService;
    @Autowired
    private XywgProerties xywgProerties;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 当天统计缓存 key
     * @param header
     * @param sn
     * @return
     */
    public static String getStatisticsKey(String header,String sn){
        return header+":crane:statistics:"+sn;
    }

    /**
     * 每天凌晨1点统计前一天所有的设备信息，归总到 t_project_crane_statistics_daily
     *
     * 1.查询所有设备
     * 2.查询项目
     * 3.查询统计
     *
     */
   // @Scheduled(cron = "0 0 1 * * ?")
   //@Scheduled(cron = "0 * * * * ?")
    public void t_project_crane_statistics_daily(){
        log.info("***************每日统计开始执行***************");
        Date now = new Date();
        Date startDate = DateUtil.beginOfDay(DateUtil.offsetDay(now,-1)).toJdkDate();
        Date endDate = DateUtil.beginOfDay(now).toJdkDate();
        log.info("统计开始日期:{}",DateUtil.format(startDate,"yyyy-MM-dd HH:mm:ss"));
        log.info("统计结束日期:{}",DateUtil.format(endDate,"yyyy-MM-dd HH:mm:ss"));
        Wrapper<ProjectCrane> craneWrapper = new EntityWrapper();
        List<ProjectCraneStatisticsDaily> dailyList = new LinkedList();
        craneWrapper.setSqlSelect("id","device_no as deviceNo","crane_no as craneNo",
                "identifier","owner","project_id as projectId",
                "(select name from t_project_info where t_project_info.id = t_project_crane.project_id) as projectName",
                "(select builder from t_project_info where t_project_info.id = t_project_crane.project_id) as builder"
                );
        craneWrapper.eq("is_del",0);
        List<ProjectCrane> craneList = projectCraneService.selectList(craneWrapper);
        if(craneList==null&&craneList.size()==0){
            log.info("很好！！,没有设备");
            log.info("***************每日统计执行完毕***************");
            return ;
        }
        log.info("总计有{}个设备",craneList.size());
        // 查询昨天的工作循环数据
        craneList.forEach(item->{
            //组装基本信息
            ProjectCraneStatisticsDaily daily = new ProjectCraneStatisticsDaily(
                    item.getProjectId(),
                    item.getProjectName(),
                    item.getBuilder(),
                    item.getOwner(),
                    item.getIdentifier(),
                    item.getCraneNo(),
                    item.getDeviceNo(),
                    startDate
            );

              // 查询报警数据
            Wrapper<ProjectCraneAlarm> alarmWrapper = new EntityWrapper<>();
            alarmWrapper.setSqlSelect("alarm_id","count(1) as amount");
            alarmWrapper.eq("crane_id",item.getId());
            alarmWrapper.eq("device_no",item.getDeviceNo());
            alarmWrapper.between("create_time",startDate,endDate);
            alarmWrapper.groupBy("alarm_id");
            List< Map<String,Object>>  alarmGroup =  alarmService.selectMaps(alarmWrapper);

            List<Map<String,Integer>> listmap = new LinkedList<>();
            alarmGroup.forEach(amount->{
               Map<String,Integer> map = new HashMap<>();
                map.put("alarm_id", (Integer) amount.get("alarm_id"));
                Long amountLong  = (Long) amount.get("amount");
                if(amountLong!=null){
                    map.put("amount",amountLong.intValue());
                }else {
                    map.put("amount",0);
                }

            });




            // 组装报警数据
            daily = ProjectCraneStatisticsDaily.packageAlarmAmout(daily,listmap);
            // 查询 工作循环

            Wrapper<ProjectCraneCyclicWorkDuration> cyclicWorkDurationWrapper = new  EntityWrapper();
            cyclicWorkDurationWrapper.eq("crane_id",item.getId());
            cyclicWorkDurationWrapper.eq("device_no",item.getDeviceNo());
            cyclicWorkDurationWrapper.between("create_time",startDate,endDate);

            List<ProjectCraneCyclicWorkDuration> cyclicWorkDurationList = craneCyclicWorkDurationService.selectList(cyclicWorkDurationWrapper);
            // 组装工作循环
            daily = ProjectCraneStatisticsDaily.packageCyclic(daily,cyclicWorkDurationList);
            log.info("每日统计设备:"+item.getDeviceNo()+" "+daily.toString());
            dailyList.add(daily);
        });


        if(dailyList.size()>0){
            dailyService.insertBatch(dailyList);
        }
        log.info("***************每日统计执行完毕***************");
    }

    /**
     * 将缓存直接放入数据库
     */
    @Scheduled(cron = "3 0 0 * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    public void cacheDaily(){
        log.info("***************每日统计开始执行***************");
        Date startDate = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-1)).toJdkDate();
        Wrapper<ProjectCrane> craneWrapper = new EntityWrapper();
        List<ProjectCraneStatisticsDaily> dailyList = new LinkedList();
        craneWrapper.setSqlSelect("id","device_no as deviceNo","crane_no as craneNo",
                "identifier","owner","project_id as projectId",
                "(select name from t_project_info where t_project_info.id = t_project_crane.project_id) as projectName",
                "(select builder from t_project_info where t_project_info.id = t_project_crane.project_id) as builder"
        );
        craneWrapper.eq("is_del",0);
        List<ProjectCrane> craneList = projectCraneService.selectList(craneWrapper);
        craneList.forEach(crane->{
          String key =  getStatisticsKey(xywgProerties.getRedisHead(),crane.getDeviceNo());
            ProjectCraneStatisticsDaily newDaily =  new ProjectCraneStatisticsDaily(
                    crane.getProjectId(),
                    crane.getProjectName(),
                    crane.getBuilder(),
                    crane.getOwner(),
                    crane.getIdentifier(),
                    crane.getCraneNo(),
                    crane.getDeviceNo(),
                    startDate );
          if(redisUtil.exists(key)){
              ProjectCraneStatisticsDaily cache = JSONUtil.toBean((String)redisUtil.get(key),ProjectCraneStatisticsDaily.class);
              cache.setWorkDate(startDate);
              dailyList.add(cache);
          }else{
              //组装基本信息
              dailyList.add(newDaily);
          }
            redisUtil.set(key,JSONUtil.toJsonStr(newDaily));
        });

        if(dailyList.size()>0){
            dailyService.insertBatch(dailyList);
        }
        log.info("***************每日统计执行完毕***************");
    }

}
