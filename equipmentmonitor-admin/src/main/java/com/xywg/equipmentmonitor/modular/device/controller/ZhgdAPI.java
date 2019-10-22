package com.xywg.equipmentmonitor.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.service.*;
import com.xywg.equipmentmonitor.modular.infromation.service.IProjectCraneDataModelService;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.Alarms2YjgjVO;
import com.xywg.equipmentmonitor.modular.station.dto.LiftDetailWeightVO;
import com.xywg.equipmentmonitor.modular.station.dto.WeightAndMomentVO;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:49 2019/6/12
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/ssdevice/zhgd/api/")
public class ZhgdAPI {

    private final IProjectInfoService projectInfoService;
    private final ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService;
    @Autowired
    private IProjectCraneDataModelService iProjectCraneDataModelService;
    @Autowired
    private ProjectCraneService projectCraneService;
    @Autowired
    private ProjectCraneDetailService projectCraneDetailService;

    @Autowired
    private IProjectLiftService iProjectLiftService;
    @Autowired
    private ProjectLiftDetailService projectLiftDetailService;

    public ZhgdAPI(IProjectInfoService projectInfoService, IProjectLiftService projectLiftService, ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService) {
        this.projectInfoService = projectInfoService;
//        this.projectLiftService = projectLiftService;
        this.projectEnvironmentMonitorAlarmService = projectEnvironmentMonitorAlarmService;
    }

    @GetMapping("lift/alarms")
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
        List<ProjectEnvironmentMonitorAlarmVO> monitorList = projectEnvironmentMonitorAlarmService.getLiftAlarms2Zhgd(month,projectInfo.getId(),time,deviceNo,status);
        monitorList.forEach(item->{
            Alarms2YjgjVO alarmsVO =Alarms2YjgjVO.convert(item);
            if(alarmsVO!=null){
                results.add(alarmsVO);
            }
        });
        return new ResultDTO<>(true,results,"成功");
    }

    /**
     *  获取实力吊重和力矩数据
     *  参数 uuid +  deviceNO
     *  1. 判断项目存不存在  t_project_info  IProjectInfoServcie
     *  2.判断设备存不存在  t_project_crane  IProjectCraneService
     *  3. 查数据   t_project_crane_detail_month IprojectCraneDetailService
     *
     *
     * @return
     */
    @GetMapping(value="crane/trend")
    public ResultDTO trend(@RequestParam(value="uuid",required = false) String uuid,
                           @RequestParam(value="deviceNo",required = false)String deviceNo)
    {

        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(deviceNo)){
            return new ResultDTO<>(false,null,"缺少参数 deviceNo");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectinfo=projectInfoService.selectOne(wrapperInfo);
        if(projectinfo!=null)
        {
            Wrapper<ProjectCrane> wrappercrane  = new EntityWrapper<>();
            wrappercrane.eq("device_no",deviceNo);
            wrappercrane.eq("is_del",0);
            wrappercrane.eq("project_id",projectinfo.getId());
            ProjectCrane projectCrane=projectCraneService.selectOne(wrappercrane);
            if(wrappercrane!=null)
            {
                Date endTime = new Date();
                Date startTime = DateUtil.offsetHour(endTime, -7);
                //获取相关表所在月份
                List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
                Map<String, Object> map = new HashMap<>();
                map.put("crane_id", projectCrane.getId());
                map.put("months", months);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                List<WeightAndMomentVO> list = projectCraneDetailService.getWeightAndMomentVO(map);
                return new ResultDTO<>(false,list,"成功");

            }
            else{
                return new ResultDTO<>(false,null,"设备不存在");
            }
        }
        else{
            return new ResultDTO<>(false,null,"项目不存在");
        }

    }

    /**
     *  获取升降机载重
     *  参数 uuid +  deviceNO
     *  1. 判断项目存不存在  t_project_info  IProjectInfoServcie
     *  2.判断设备存不存在  t_project_lift  IProjectLiftService
     *  3. 查数据   t_project_lift_detail_month IprojectLiftDetail_Service
     *
     *
     * @return
     */
    @GetMapping(value="lift/trend")
    public ResultDTO liftTrend(@RequestParam(value="uuid",required = false) String uuid,
                           @RequestParam(value="deviceNo",required = false)String deviceNo)
    {

        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(deviceNo)){
            return new ResultDTO<>(false,null,"缺少参数 deviceNo");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectinfo=projectInfoService.selectOne(wrapperInfo);
        if(projectinfo!=null)
        {
            Wrapper<ProjectLift> wrapperlift  = new EntityWrapper<>();
            wrapperlift.eq("device_no",deviceNo);
            wrapperlift.eq("is_del",0);
            wrapperlift.eq("project_id",projectinfo.getId());
            ProjectLift projectLift=iProjectLiftService.selectOne(wrapperlift);
            if(projectLift!=null)
            {

                Date endTime = new Date();
                Date startTime = DateUtil.offsetHour(endTime, -7);
                //获取相关表所在月份
                List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
                Map<String, Object> map = new HashMap<>();
                map.put("lift_id", projectLift.getId());
                map.put("months", months);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                List<LiftDetailWeightVO> list = projectLiftDetailService.getWeight(map);
                return new ResultDTO<>(false,list,"成功");

            }
            else{
                return new ResultDTO<>(false,null,"设备不存在");
            }
        }
        else{
            return new ResultDTO<>(false,null,"项目不存在");
        }

    }
}
