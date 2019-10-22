package com.xywg.equipment.monitor.iot.modular.timer;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xywg.equipment.monitor.iot.core.util.WebserviceUtil;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 采集任务
 * Date: Created in 15:21 2018/8/27
 * Modified By : wangyifei
 */

 // @Component
  //@JobHandler("collect-timer")
public class CollectTimer extends IJobHandler {

    @Autowired
    WebserviceUtil   webserviceUtil;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        System.out.println("当前时间:"+DateUtil.now());
        webserviceUtil.selectDevices();
        return null;
    }
}
