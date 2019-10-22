package com.xingyun.equipment.crane.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.crane.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import com.xingyun.equipment.crane.modular.device.service.ProjectEnvironmentMonitorService;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectInfoService;
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
@RequestMapping("/admin-crane/yjgj")
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


        return new ResultDTO<>(true,null,"成功");
    }


    @GetMapping("/monitor/alarms")
    @SuppressWarnings("all")
    public ResultDTO monitorAlarms(@RequestParam(value="uuid",required = false) String uuid,
                            @RequestParam(value="time",required = false)String time
    ){


        return new ResultDTO<>(true,null,"成功");
    }


}
