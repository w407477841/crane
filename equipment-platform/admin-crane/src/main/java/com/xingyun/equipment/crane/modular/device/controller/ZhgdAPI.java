package com.xingyun.equipment.crane.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.service.IProjectLiftService;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.crane.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:49 2019/6/12
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/admin-crane/zhgd/api/")
public class ZhgdAPI {

    private final IProjectInfoService projectInfoService;
    private final IProjectLiftService projectLiftService;
    private final ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService;

    private ProjectCraneService projectCraneService;
    public ZhgdAPI(IProjectInfoService projectInfoService, IProjectLiftService projectLiftService, ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService) {
        this.projectInfoService = projectInfoService;
        this.projectLiftService = projectLiftService;
        this.projectEnvironmentMonitorAlarmService = projectEnvironmentMonitorAlarmService;
    }



    @RequestMapping(value="/getDeviceFlow")
    public ResultDTO getDeviceFlow(String projectId, String gprs)
    {
        Wrapper<ProjectCrane> wrapperInfo  = new EntityWrapper<>();
        if(!StrUtil.isBlank(projectId))
        {
            wrapperInfo.eq("project_id",projectId);
            if(!StrUtil.isBlank(gprs))
            {
                wrapperInfo.eq("gprs",gprs);
            }
            List<ProjectCrane> craneList=projectCraneService.selectList(wrapperInfo);
            if(null!=craneList&&craneList.size()>0)
            {
                for(int i=0;i<craneList.size();i++)
                {
                    System.out.println(craneList.get(i));
                }
            }
        }
        else{
            return new ResultDTO<>(false,null,"项目名称为空");
        }

        return null;


    }
}
