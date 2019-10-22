package com.xywg.equipment.monitor.iot.modular.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: SJ
 * @Date: 2018/9/19 14:01
 * @Description:
 */
@Component
public class ParisingData {

    private static final Logger logger = LoggerFactory.getLogger(ParisingData.class);

    @Scheduled(cron = "0/15 * * * * ?")
    public void doElec(){
        //0 0 0 1/1 * ?
        //同比:取前30天的某日为数据比较对象

        logger.info("");
    }

}
