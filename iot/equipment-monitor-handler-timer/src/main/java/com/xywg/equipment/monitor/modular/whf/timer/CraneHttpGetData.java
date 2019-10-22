package com.xywg.equipment.monitor.modular.whf.timer;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.xywg.equipment.monitor.modular.whf.model.CraneTrainingData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author hjy
 * @date 2018/12/26
 * 轮训的方式拉取塔吊数据
 */
@Component
public class CraneHttpGetData {

    @Scheduled(cron = "0/30 * * * * ?")
    public void executeCraneHttpGetData() {
        String userID = "";
        String userPassword = "";
        String url = "http://www.0531yun.cn/wsjc/Device/getDeviceData.do?userID=" + userID + "&userPassword=" + userPassword;
        String jsonStr = HttpUtil.get(url);
        List<CraneTrainingData> object = JSON.parseArray(jsonStr, CraneTrainingData.class);


    }
}
