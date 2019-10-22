package com.xywg.equipment.monitor.iot.modular.timer;

import com.xywg.equipment.monitor.iot.modular.ammeter.service.IProjectAmmeterDailyService;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.IProjectWaterDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 每日统计
 * Date: Created in 10:42 2018/11/20
 * Modified By : wangyifei
 */
@Component
public class DailyTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyTimer.class);
    @Autowired
    private IProjectAmmeterDailyService   projectAmmeterDailyService;
    @Autowired
    private IProjectWaterDailyService  projectWaterDailyService;

    /**
     * 电表每日统计
     */
    @Scheduled(cron = "0 30 1 * * ?")
    public void ammeterDaily(){
        projectAmmeterDailyService.daily();

    }

    /**
     * 水表每日统计
     */
   @Scheduled(cron = "0 0 2 * * ?")
    public void waterDaily(){
        projectWaterDailyService.daily();
    }

}
