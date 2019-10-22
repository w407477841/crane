package com.xingyun.equipment.admin.modular.device.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.common.constant.Const;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.RedisUtil;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneStatisticsDaily;
import com.xingyun.equipment.admin.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectAnnouncement;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectAnnouncementService;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:03 2019/6/27
 * Modified By : wangyifei
 */
@RestController
@Slf4j
public class HomePageAPI {
    @Autowired
    private ProjectCraneService craneService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ProjectAnnouncementService announcementService;

    @Autowired
    private RedisUtil redisUtil;

@PostMapping("ssdevice/homepage/craneAlarmCount")
    public ResultDTO craneAlarmCount(){
    List<Integer> orgIds = Const.orgIds.get() ;
    String keyPre = "device_platform:crane:statistics:";
    Wrapper wrapper =new EntityWrapper();
    wrapper.eq("is_del","0");
    wrapper.in("org_id",orgIds);
    List<ProjectCrane> craneList = craneService.selectList(wrapper);
    LongAdder adder  = new LongAdder();
    craneList.forEach(item->{
        String key = keyPre + item.getDeviceNo();
        if (redisUtil.exists(key)) {
            ProjectCraneStatisticsDaily currentDay = JSONUtil.toBean((String) redisUtil.get(key), ProjectCraneStatisticsDaily.class);
            adder.add(currentDay.getCollisionAlarm());
            adder.add(currentDay.getLimitAlarm() );
            adder.add(currentDay.getMomentAlarm() );
            adder.add(currentDay.getRangeAlarm());
            adder.add(currentDay.getWeightAlarm());
            adder.add(currentDay.getTiltAlarm());
            adder.add(currentDay.getWindSpeedAlarm());

        }
    });


        return new ResultDTO(true,adder.longValue());

    }

    /**
     * 通知
     * @return
     */
    @PostMapping("ssdevice/homepage/craneNotice")
    public ResultDTO notice(){

        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("is_del","0");
        wrapper.orderBy("create_time",false);
        List<ProjectAnnouncement > announcementList =  announcementService.selectList(wrapper);

        return new ResultDTO(true,announcementList);
    }

    /**
     * 首页通知版
     * @return
     */
    @PostMapping("ssdevice/homepage/billboard")
    public ResultDTO billboard(){
        Map<String,Integer> map =new HashMap<>();
        List<Integer> orgIds = Const.orgIds.get() ;
        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("is_del","0");
        wrapper.in("org_id",orgIds);
        map.put("craneAccount",craneService.selectCount(wrapper));
        map.put("driverAccount",0);
        map.put("projectAccount",projectInfoService.selectCount(wrapper));
        return new ResultDTO(true,map);
    }

    @ApiOperation("获取天气信息")
    @GetMapping("ssdevice/homepage/getWeatherInfo")
    public ResultDTO<Object> getWeatherInfo(String city) {
        HashMap<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("city", city);
        String result3 = HttpUtil.get("http://wthrcdn.etouch.cn/weather_mini", paramMap);
        return new ResultDTO<>(true, result3);
    }
}
