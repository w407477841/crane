package com.xywg.equipmentmonitor.modular.station.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.core.vo.CurrentMonitorDataVO;
import com.xywg.equipmentmonitor.modular.device.model.*;
import com.xywg.equipmentmonitor.modular.device.service.*;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.AlarmsVO;
import com.xywg.equipmentmonitor.modular.station.dto.BindDTO;
import com.xywg.equipmentmonitor.modular.station.model.ProjectAccuratePositionData;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDevice;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.xywg.equipmentmonitor.modular.station.model.ProjectRegionalPositionData;
import com.xywg.equipmentmonitor.modular.station.service.*;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectRegionalPositionDataServiceImpl;
import com.xywg.equipmentmonitor.modular.station.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author : wangyifei
 * Description 智慧工地API
 * Date: Created in 8:51 2019/3/22
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/station2zhgd")
public class Station2zhgdAPI {

    private final  IProjectInfoService  projectInfoService;
    private final IProjectMapService projectMapService;
    private final IProjectDeviceService  projectDeviceService;
    private final ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService;
    private final ProjectEnvironmentMonitorDetailService projectEnvironmentMonitorDetailService;
    private final ProjectEnvironmentMonitorService projectEnvironmentMonitorService;
    private final ProjectCraneService projectCraneService;
    private final IProjectLiftService projectLiftService;
    private final IProjectAccuratePositionDataService positionDataService;
    private final RedisUtil redisUtil;
    private final IProjectRegionalPositionDataService regionalPositionDataService;
    @Autowired
    public Station2zhgdAPI(IProjectInfoService projectInfoService, IProjectMapService projectMapService, IProjectDeviceService projectDeviceService, ProjectEnvironmentMonitorAlarmService projectEnvironmentMonitorAlarmService, ProjectEnvironmentMonitorDetailService projectEnvironmentMonitorDetailService, ProjectEnvironmentMonitorService projectEnvironmentMonitorService, ProjectCraneService projectCraneService, IProjectLiftService projectLiftService, IProjectAccuratePositionDataService positionDataService, RedisUtil redisUtil, IProjectRegionalPositionDataService regionalPositionDataService) {
        this.projectInfoService = projectInfoService;
        this.projectMapService = projectMapService;
        this.projectDeviceService = projectDeviceService;
        this.projectEnvironmentMonitorAlarmService = projectEnvironmentMonitorAlarmService;
        this.projectEnvironmentMonitorDetailService = projectEnvironmentMonitorDetailService;
        this.projectEnvironmentMonitorService = projectEnvironmentMonitorService;
        this.projectCraneService = projectCraneService;
        this.projectLiftService = projectLiftService;
        this.positionDataService = positionDataService;
        this.redisUtil = redisUtil;
        this.regionalPositionDataService = regionalPositionDataService;
    }

    /**
    *  根据uuid 查询基站列表
     *
     * @param uuid ?
     */
    @GetMapping("getStations")
    public ResultDTO<Object> getStations(@RequestParam(value="uuid",required = false) String uuid){
        List<ProjectDeviceVO> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }

        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectDevice> wrapper  = new EntityWrapper<>();
        wrapper.setSqlSelect("device_no as deviceNo","id");
        wrapper.eq("project_id",projectInfo.getId());
        wrapper.eq("current_flag",0);
        wrapper.eq("is_del",0);
        wrapper.eq("type",2);
        List<Map<String,Object>> maps =  projectDeviceService.selectMaps(wrapper);
        maps.forEach(item->{
            results.add(ProjectDeviceVO.convert(item));
        });

        return new ResultDTO<>(true,results,"成功");
    }




    @GetMapping("getEnviromentDevices")
    public ResultDTO<Object> getEnviromentDevices(@RequestParam(value="uuid",required = false) String uuid){
        List<EnviromentDeviceVo> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectEnvironmentMonitor> wrapper  = new EntityWrapper<>();
        wrapper.setSqlSelect("device_no as deviceNo","id");
        wrapper.eq("project_id",projectInfo.getId());
        wrapper.eq("is_del",0);
        List<Map<String,Object>> maps =  projectEnvironmentMonitorService.selectMaps(wrapper);
        maps.forEach(item->{
            results.add(EnviromentDeviceVo.convert(item));
        });

        return new ResultDTO<>(true,results,"成功");
    }

    @GetMapping("getCraneDevices")
    public ResultDTO<Object> getCraneDevices(@RequestParam(value="uuid",required = false) String uuid){
        List<CraneDeviceVo> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectCrane> wrapper  = new EntityWrapper<>();
        wrapper.setSqlSelect("device_no as deviceNo","id");
        wrapper.eq("project_id",projectInfo.getId());
        wrapper.eq("is_del",0);
        List<Map<String,Object>> maps =  projectCraneService.selectMaps(wrapper);
        maps.forEach(item->{
            results.add(CraneDeviceVo.convert(item));
        });

        return new ResultDTO<>(true,results,"成功");
    }

    @GetMapping("getLiftDevices")
    public ResultDTO<Object> getLiftDevices(@RequestParam(value="uuid",required = false) String uuid){
        List<LiftDeviceVo> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectLift> wrapper  = new EntityWrapper<>();
        wrapper.setSqlSelect("device_no as deviceNo","id");
        wrapper.eq("project_id",projectInfo.getId());
        wrapper.eq("is_del",0);
        List<Map<String,Object>> maps =  projectLiftService.selectMaps(wrapper);
        maps.forEach(item->{
            results.add(LiftDeviceVo.convert(item));
        });

        return new ResultDTO<>(true,results,"成功");
    }

    /**
     * 绑定 地图
     * @param bindDTO
     * @return
     */
    @PostMapping("bind")
    public ResultDTO<Object> bind(@RequestBody BindDTO bindDTO){
        try {
            return projectMapService.bind(bindDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO<>(false,null,"失败");
        }
    }

    /**
     * 项目所有人员的 当前位置
     * @param uuid
     * @return
     */
    @GetMapping("lastLocations")
    public ResultDTO<Object> lastLocations(@RequestParam(value="uuid",required = false) String uuid){
        List<LastLocationVO> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }

        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectMap> mapWrapper = new EntityWrapper<>();
        mapWrapper.eq("is_del",0);
        mapWrapper.eq("project_id",projectInfo.getId());
        ProjectMap map =  projectMapService.selectOne(mapWrapper);
        Wrapper<ProjectAccuratePositionData> dataWrapper = new EntityWrapper<>();
        dataWrapper.setSqlSelect("x_zhou as xZhou","y_zhou as yZhou","identity_code as identityCode","(select name from t_project_device_worker_record   where t_project_device_worker_record.id_card_number=t_project_accurate_position_data.identity_code and t_project_device_worker_record.device_no=t_project_accurate_position_data.label_no  ) as name");
        dataWrapper.where("id in(select max(id) from t_project_accurate_position_data where collect_time > {0} and map_id = {1} GROUP BY identity_code)",DateUtil.offsetMinute(new Date(),-30).toString("yyyy-MM-dd HH:mm:ss"),map.getId());
        List<ProjectAccuratePositionData> positionDataList = positionDataService.selectList(dataWrapper);

        positionDataList.forEach(item->{
            results.add(LastLocationVO.convert(item));
        });

         return new ResultDTO<>(true,results,"成功");

    }
    /**
     * 项目所有人员的 当前位置
     * @param uuid
     * @return
     */
    @GetMapping("lastRegionalLocation")
    public ResultDTO lastRegionalLocation(@RequestParam(value="uuid",required = false) String uuid){
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Map<String,LastRegionalLocationVO> result;
        Wrapper<ProjectMap> mapWrapper = new EntityWrapper<>();
        mapWrapper.eq("is_del",0);
        mapWrapper.eq("project_id",projectInfo.getId());
        ProjectMap map =  projectMapService.selectOne(mapWrapper);
        if(null==map){
            return new ResultDTO<>(false,null,"项目图不存在");
        }

        Wrapper<ProjectRegionalPositionData> dataWrapper = new EntityWrapper<>();
        dataWrapper.setSqlSelect(
                "station_no as stationNo",
                "identity_code as identityCode",
                "(select name from t_project_device_worker_record   where  t_project_device_worker_record.device_no=t_project_regional_position_data.label_no  ) as name");
        dataWrapper.where("id in(select max(id) from t_project_regional_position_data where collect_time > {0} and map_id = {1} GROUP BY identity_code)",DateUtil.offsetMinute(new Date(),-1).toString("yyyy-MM-dd HH:mm:ss"),map.getId());
        List<ProjectRegionalPositionData> regionalPositionData = regionalPositionDataService.selectList(dataWrapper);
        result = LastRegionalLocationVO.regionalLocationVOMap(regionalPositionData);
        return new ResultDTO<>(true,result);
    }

    /**
     *  人员轨迹
     * @param identityCode 身份证
     * @param uuid uuid
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @GetMapping("getLocaltions")
    public ResultDTO localtions(@RequestParam(value="identityCode",required = false)  String identityCode,
                                @RequestParam(value="uuid",required = false)  String uuid,
                                @RequestParam(value="beginTime",required = false)String beginTime,
                                @RequestParam(value="endTime",required = false)String endTime){
        List<PostionVO> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(identityCode)){
            return new ResultDTO<>(false,null,"缺少参数 identityCode");
        }
        if(StrUtil.isBlank(beginTime)){
            return new ResultDTO<>(false,null,"缺少参数 beginTime");
        }
        if(StrUtil.isBlank(endTime)){
            return new ResultDTO<>(false,null,"缺少参数 endTime");
        }



        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);

        if(null== projectInfo){
            return new ResultDTO<>(false,null,"项目还未绑定");
        }

        // 查询 与项目有关的地图 及 基站关系
        Wrapper<ProjectMap> mapWrapper = new EntityWrapper<>();
        mapWrapper.eq("project_id",projectInfo.getId());
        mapWrapper.eq("is_del",0);
        ProjectMap map= projectMapService.selectOne(mapWrapper);


        Wrapper<ProjectAccuratePositionData> positionDataWrapper = new EntityWrapper<>();
        positionDataWrapper.setSqlSelect("x_zhou as xZhou","y_zhou as yZhou","collect_time as collectTime");
        positionDataWrapper.where("(select id from t_project_map  where project_id = {0}) = t_project_accurate_position_data.map_id",projectInfo.getId());
        positionDataWrapper.eq("identity_code",identityCode);
        positionDataWrapper.between("collect_time",beginTime,endTime);
        positionDataWrapper.orderBy("collect_time",false);
        List<ProjectAccuratePositionData> list = positionDataService.selectList(positionDataWrapper);
//        long timestamps = 0;
//        for(ProjectAccuratePositionData item:list){
//            if(timestamps == 0){
//                results.add(PostionVO.convert(item));
//                timestamps =  item.getCollectTime().getTime();
//            }else{
//                if(timestamps-item.getCollectTime().getTime() >= 5*60*1000 ){
//                    results.add(PostionVO.convert(item));
//                    timestamps-=(5*60*1000);
//                }
//            }
//        }


        for(ProjectAccuratePositionData item:list){
            results.add(PostionVO.convert(item));
        }
        return new ResultDTO<>(true,results,"成功");
    }

    @GetMapping("alarms")
    public ResultDTO alarms(@RequestParam(value="uuid",required = false) String uuid,
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
            time  =  DateUtil.format(new Date(),"yyyy-MM-dd")+" 00:00:00";
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

    @ApiOperation("近期最后一条扬尘数据")
    @GetMapping("getMonitorData")
    public ResultDTO getMonitorData(@RequestParam(value="uuid",required = false) String uuid
           ) {
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }

        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectEnvironmentMonitor> monitorWrapper = new EntityWrapper<>();
        monitorWrapper.eq("is_del",0);
        monitorWrapper.eq("project_id",projectInfo.getId());
        List<ProjectEnvironmentMonitor> monitorList = projectEnvironmentMonitorService.selectList(monitorWrapper);

        List<CurrentMonitorDataVO> results = new LinkedList<>();
        monitorList.forEach(item->{
            CurrentMonitorDataVO dataVO =projectEnvironmentMonitorDetailService.getMonitorData(uuid, item.getDeviceNo());
            if(dataVO!=null){
                results.add(dataVO);
            }

        });
        return new ResultDTO<>(true,results,"成功");
    }

    /**
     * 设备信息
     * @param uuid
     * @return
     */
    @GetMapping("getDeviceInfo")
    public ResultDTO getDeviceInfo(@RequestParam(value="uuid",required = false) String uuid
           ){

        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }

        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);

        if(null== projectInfo){
            return new ResultDTO<>(false,null,"项目还未绑定");
        }

        Wrapper<ProjectEnvironmentMonitor> monitorWrapper =  new EntityWrapper<>();
        monitorWrapper.eq("is_del",0);
        monitorWrapper.eq("project_id",projectInfo.getId());
        List<ProjectEnvironmentMonitor> environmentMonitor = projectEnvironmentMonitorService.selectList(monitorWrapper);
        return new ResultDTO<>(true,environmentMonitor.get(0),"成功");
    }

    /**
     * 设备列表
     * @param uuid
     * @return
     */
    @GetMapping("getDeviceList")
    public ResultDTO getDeviceList(@RequestParam(value="uuid",required = false) String uuid
          ){

        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }

        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);

        if(null== projectInfo){
            return new ResultDTO<>(false,null,"项目还未绑定");
        }

        Wrapper<ProjectEnvironmentMonitor> monitorWrapper =  new EntityWrapper<>();
        monitorWrapper.setSqlSelect("device_no as deviceNo");
        monitorWrapper.eq("is_del",0);
        monitorWrapper.eq("project_id",projectInfo.getId());
        List<Map<String,Object>> environmentMonitors = projectEnvironmentMonitorService.selectMaps(monitorWrapper);
        return new ResultDTO<>(true,environmentMonitors,"成功");
    }


    /**
     * 查询不同类型的扬尘属性数据
     * @param uuid  项目uuid
     * @param deviceNo 设备号
     * @param alarmId 报警类型
     * @param time 报警时间
     * @return
     */
    @GetMapping("info")
    public ResultDTO info(@RequestParam(value="uuid",required = false) String uuid
                    ,@RequestParam(value="deviceNo",required = false) String deviceNo
                    ,@RequestParam(value="alarmId",required = false) Integer alarmId
                     ,@RequestParam(value="time",required = false) String time
    ){

        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(deviceNo)){
            return new ResultDTO<>(false,null,"缺少参数 deviceNo");
        }
        if(null==alarmId){
            return new ResultDTO<>(false,null,"缺少参数 alarmId");
        }
        if(StrUtil.isBlank(time)){
            return new ResultDTO<>(false,null,"缺少参数 time");
        }
        ResultDTO resultDTO =null;
        try {
             resultDTO =    projectEnvironmentMonitorDetailService.getMonitorInfo2zhgd(uuid,deviceNo,alarmId,time);
        }catch (Exception ex ){
            resultDTO = new ResultDTO(false,null,"");
        }


        return resultDTO;
    }


    @GetMapping("trend")
    public ResultDTO trend(@RequestParam(value="uuid",required = false) String uuid,
                           @RequestParam(value="deviceNo",required = false) String deviceNo,
                           @RequestParam("columnName") String columnName,
                            @RequestParam(value="beginTime",required = false)String beginTime,
                           @RequestParam(value="endTime",required = false)String endTime,
                           @RequestParam(value="type",required = false)Integer  type
    ){
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(deviceNo)){
            return new ResultDTO<>(false,null,"缺少参数 deviceNo");
        }
        if(type==null){
            return new ResultDTO<>(false,null,"缺少参数 type");
        }
        if(StrUtil.isBlank(columnName)){
            return new ResultDTO<>(false,null,"缺少参数 columnName");
        }
        if(StrUtil.isBlank(beginTime)){
            return new ResultDTO<>(false,null,"缺少参数 beginTime");
        }
        if(StrUtil.isBlank(endTime)){
            return new ResultDTO<>(false,null,"缺少参数 endTime");
        }

        // 查下设备
        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        Wrapper<ProjectEnvironmentMonitor> monitorWrapper = new EntityWrapper<>();
        monitorWrapper.eq("is_del",0);
        monitorWrapper.eq("project_id",projectInfo.getId());
        monitorWrapper.eq("device_no",deviceNo);
        ProjectEnvironmentMonitor monitor = projectEnvironmentMonitorService.selectOne(monitorWrapper);
        if(monitor == null){
            return new ResultDTO<>(false,null,"设备不存在");
        }
        ResultDTO<DataDTO<List<Map<String,Object>>>>  resultDTO = projectEnvironmentMonitorDetailService.changeToChart(monitor.getId(),columnName,type,beginTime,endTime);
        List<Map<String,Object>> maps =  resultDTO.getData().getList();

            return new ResultDTO(true,maps);
    }



    @GetMapping("weather")
    public ResultDTO weather(@RequestParam(value="uuid",required = false) String uuid,@RequestParam(value="locations",required = false) String locations){
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        String key = "device_platform:weather:uuid:"+uuid ;
        Map<String,Object> result;
        if(redisUtil.exists(key)){
            result =  JSONUtil.toBean((String)redisUtil.get(key),Map.class);
        }else{
            String json ;
            if(StrUtil.isBlank(locations)){
                 json = HttpUtil.get("https://api.caiyunapp.com/v2/TAkhjf8d1nlSlspN/120.895267,31.987284/realtime.json");
            }else{
                 json =  HttpUtil.get("https://api.caiyunapp.com/v2/TAkhjf8d1nlSlspN/"+locations+"/realtime.json");
            }

            Map<String,Object> map = JSONUtil.toBean(json,Map.class);
            Map<String,Object>  resultMap  = (Map<String, Object>) map.get("result");
            result   = new HashMap<>();
            result.put("pm25",resultMap.get("pm25"));
            result.put("pm10",resultMap.get("pm10"));
            result.put("aqi",resultMap.get("aqi"));
            // 120分钟过期
            redisUtil.set(key,JSONUtil.toJsonStr(result),120L);
        }


       return new ResultDTO(true,result,"成功");
    }









}
