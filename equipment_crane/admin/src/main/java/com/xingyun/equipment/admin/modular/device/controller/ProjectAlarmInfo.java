package com.xingyun.equipment.admin.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.xingyun.equipment.admin.core.dto.AppResultDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.shaded.javax.ws.rs.POST;

import java.util.Date;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 19:10 2018/9/2
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/project")
public class ProjectAlarmInfo {
@Autowired
    ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService;
/**  修改报警信息 */
    @PostMapping("alarmInfo/update")
    public Object alarmInfo(@RequestParam("ids") Integer[] ids,@RequestParam("userId") Integer userId,@RequestParam("userName") String userNmae,@RequestParam("flag") Integer flag){
        if(3==flag){
            return projectEnvironmentMonitorAlarmService.updateAlarm(("t_project_crane_alarm_"+ DateUtil.format(new Date(),"yyyyMM")),ids,userId,userNmae);
        }else if(2==flag){
            return projectEnvironmentMonitorAlarmService.updateAlarm(("t_project_lift_alarm_"+ DateUtil.format(new Date(),"yyyyMM")),ids,userId,userNmae);
        }else if(1==flag){
            return projectEnvironmentMonitorAlarmService.updateAlarm(("t_project_environment_monitor_alarm_"+ DateUtil.format(new Date(),"yyyyMM")),ids,userId,userNmae);
        }else{
            return new AppResultDTO(false,"flag不支持");
        }



    }

    @GetMapping("alarmInfo/getList")
    @ApiOperation("查询 当月未处理报警信息")
    public Object getAlarmInfo(@RequestParam("uuids") String [] uuids,@RequestParam("pageSize") Integer pageSize,@RequestParam("pageNum") Integer pageNum){




        return projectEnvironmentMonitorAlarmService.getAlarmInfo(uuids,pageSize,pageNum);
    }


    @GetMapping("alarmInfo/getListByFlag")
    @ApiOperation("查询 当月未处理报警信息")
    public Object getAlarmInfo(@RequestParam("flag") int  flag,@RequestParam("uuids") String [] uuids,@RequestParam("pageSize") Integer pageSize,@RequestParam("pageNum") Integer pageNum){


        return projectEnvironmentMonitorAlarmService.getAlarmInfoByFlag(flag,uuids,pageSize,pageNum);
    }


}
