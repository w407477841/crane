package com.xywg.equipmentmonitor.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.Alarms2YjgjVO;
import com.xywg.equipmentmonitor.modular.station.dto.AlarmsVO;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:45 2019/5/10
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/ssdevice/yjgj")
public class YjgjAPI {

    private final IProjectInfoService  projectInfoService;

    private final ProjectCraneService projectCraneService;

    private final ProjectEnvironmentMonitorService projectEnvironmentMonitorService;

    private final ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService;
    @Autowired
    public YjgjAPI(IProjectInfoService projectInfoService, ProjectCraneService projectCraneService, ProjectEnvironmentMonitorService projectEnvironmentMonitorService, ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService) {
        this.projectInfoService = projectInfoService;
        this.projectCraneService = projectCraneService;
        this.projectEnvironmentMonitorService = projectEnvironmentMonitorService;
        this.projectEnvironmentMonitorAlarmService = projectEnvironmentMonitorAlarmService;
    }

    @GetMapping("alarms")
    public ResultDTO alarms(@RequestParam(value="uuid",required = false) String uuid,
                            @RequestParam(value="time",required = false)String time,
                            @RequestParam(value="deviceNo",required = false)String deviceNo,
                            @RequestParam(value="status",required = false)Integer status
    ){
        List<Alarms2YjgjVO> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }

        if(StrUtil.isBlank(time)){
            time  =  DateUtil.format(new Date(),"yyyy-MM")+"-01 00:00:00";
        }

        String month = DateUtil.format(new Date(),"yyyyMM");

        List<ProjectEnvironmentMonitorAlarmVO> monitorList = projectEnvironmentMonitorAlarmService.getAlarms2yjgj(month,projectInfo.getId(),time,deviceNo,status);

        monitorList.forEach(item->{
            Alarms2YjgjVO alarmsVO =Alarms2YjgjVO.convert(item);
            if(alarmsVO!=null){
                results.add(alarmsVO);
            }

        });


        return new ResultDTO<>(true,results,"成功");
    }


    @GetMapping("/monitor/alarms")
    @SuppressWarnings("all")
    public ResultDTO monitorAlarms(@RequestParam(value="uuid",required = false) String uuid,
                            @RequestParam(value="time",required = false)String time
    ){
        List<AlarmsVO> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }

        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }

        if(StrUtil.isBlank(time)){
            time  =  DateUtil.format(new Date(),"yyyy-MM")+"-01 00:00:00";
        }
        // 报警
        Wrapper<ProjectEnvironmentMonitor> monitorWrapper = new EntityWrapper<>();

        List<ProjectEnvironmentMonitor> monitors = projectEnvironmentMonitorService.selectList(monitorWrapper);
        Map<String ,ProjectEnvironmentMonitor> projectEnvironmentMonitorMap = new HashMap<>();

        monitors.forEach(item->{
            projectEnvironmentMonitorMap.put(item.getDeviceNo(),item);
        });


        String month = DateUtil.format(new Date(),"yyyyMM");

        List<ProjectEnvironmentMonitorAlarmVO> monitorList = projectEnvironmentMonitorAlarmService.getAlarms2zhgd(month,projectInfo.getId(),time);

        monitorList.forEach(item->{
            AlarmsVO alarmsVO =AlarmsVO.convert(item,projectEnvironmentMonitorMap.get(item.getDeviceNo()));
            if(alarmsVO!=null){
                results.add(alarmsVO);
            }

        });


        return new ResultDTO<>(true,results,"成功");
    }


}
