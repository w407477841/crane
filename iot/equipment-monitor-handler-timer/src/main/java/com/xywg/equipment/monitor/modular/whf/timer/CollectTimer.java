package com.xywg.equipment.monitor.modular.whf.timer;

import cn.hutool.core.date.DateUtil;
import com.xywg.equipment.monitor.core.util.WebserviceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 采集任务
 * Date: Created in 15:21 2018/8/27
 * Modified By : wangyifei
 */

  @Component
  @ConditionalOnProperty(prefix = "timer",name = "collect",havingValue = "true")
public class CollectTimer{

    @Autowired
    WebserviceUtil webserviceUtil;

    @Scheduled(cron = "0/30 * * * * ?")
    public void execute()  {
        System.out.println("当前时间:"+DateUtil.now());
        webserviceUtil.selectDevices();
    }
}
