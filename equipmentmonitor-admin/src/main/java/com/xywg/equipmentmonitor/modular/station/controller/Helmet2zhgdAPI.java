package com.xywg.equipmentmonitor.modular.station.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetPositionDetail;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetPositionDetailService;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.GpsCoordinate;
import com.xywg.equipmentmonitor.modular.station.model.ProjectAccuratePositionData;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.xywg.equipmentmonitor.modular.station.service.IProjectMapService;
import com.xywg.equipmentmonitor.modular.station.vo.LastLocationVO;
import com.xywg.equipmentmonitor.modular.station.vo.PostionVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Date: Created in 15:21 2019/5/20
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/ssdevice/helmet2zhgd")
@Slf4j
public class Helmet2zhgdAPI {

    private final IProjectInfoService projectInfoService;
    private final IProjectMapService projectMapService;
    private final IProjectHelmetPositionDetailService positionDetailService;


    public Helmet2zhgdAPI(IProjectInfoService projectInfoService, IProjectMapService projectMapService, IProjectHelmetPositionDetailService positionDetailService) {
        this.projectInfoService = projectInfoService;
        this.projectMapService = projectMapService;
        this.positionDetailService = positionDetailService;
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
        // 构建 数据表明
        String tableName ="t_project_helmet_position_detail_"+DateUtil.format(new Date(),"yyyyMM");
        // 30分钟内
        String time = DateUtil.offsetMinute(new Date(),-30).toString("yyyy-MM-dd HH:mm:ss");

        List<ProjectHelmetPositionDetail> details = positionDetailService.getLastLocation(tableName,time,projectInfo.getId());
        // 原点gps 经纬度
        GpsCoordinate o =new GpsCoordinate(Double.parseDouble(map.getLocation().split(",")[0]),Double.parseDouble(map.getLocation().split(",")[1]));
        details.forEach(item->{
            results.add( LastLocationVO.convert(o,item)) ;
        });
        return new ResultDTO<>(true,results,"成功");

    }

    @SuppressWarnings("all")
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

        // 查询 与项目有关的地图
        Wrapper<ProjectMap> mapWrapper = new EntityWrapper<>();
        mapWrapper.eq("project_id",projectInfo.getId());
        mapWrapper.eq("is_del",0);
        ProjectMap map= projectMapService.selectOne(mapWrapper);
        // 原点经纬度
        GpsCoordinate o =new GpsCoordinate(Double.parseDouble(map.getLocation().split(",")[0]),Double.parseDouble(map.getLocation().split(",")[1]));
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(DateUtil.parse(beginTime,"yyyy-MM-dd HH:mm:ss"),DateUtil.parse( endTime,"yyyy-MM-dd HH:mm:ss"));
        //构建设计查询的表
        List<String> tables = new LinkedList<>();
        months.forEach(item->{
            tables.add("t_project_helmet_position_detail_"+item);
        });
        List<ProjectHelmetPositionDetail> list = positionDetailService.getLocations(tables,projectInfo.getId(),identityCode,beginTime,endTime);
        for(ProjectHelmetPositionDetail item:list){
            results.add(PostionVO.convert(o,item));
        }
        return new ResultDTO<>(true,results,"成功");
    }






}
