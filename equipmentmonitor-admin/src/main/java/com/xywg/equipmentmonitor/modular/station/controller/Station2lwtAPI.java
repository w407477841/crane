package com.xywg.equipmentmonitor.modular.station.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.model.ProjectAccuratePositionData;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.xywg.equipmentmonitor.modular.station.service.IProjectAccuratePositionDataService;
import com.xywg.equipmentmonitor.modular.station.service.IProjectMapService;
import com.xywg.equipmentmonitor.modular.station.vo.PostionVO;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectMapVO;
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
 * Description 劳务通接口
 * Date: Created in 17:05 2019/3/20
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/station2lwt")
public class Station2lwtAPI {

    private  final IProjectInfoService projectInfoService;

    private final IProjectMapService projectMapService;

    private final IProjectAccuratePositionDataService positionDataService;
    @Autowired
    public Station2lwtAPI(IProjectInfoService projectInfoService, IProjectMapService projectMapService, IProjectAccuratePositionDataService positionDataService) {
        this.projectInfoService = projectInfoService;
        this.projectMapService = projectMapService;
        this.positionDataService = positionDataService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Station2lwtAPI.class);

    /**
     *  查询项目平面图 url
     * @param uuid
     * @return
     */
    @GetMapping("getProjectMap")
    public ResultDTO<ProjectMapVO> getProjectMap(@RequestParam(value="uuid",required = false) String uuid){
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

        Wrapper<ProjectMap> mapWrapper = new EntityWrapper<>();
        mapWrapper.eq("project_id",projectInfo.getId());
        mapWrapper.eq("is_del",0);

        List<ProjectMap> mapList=  projectMapService.selectList(mapWrapper);
        List<ProjectMapVO> results  = new LinkedList();
        mapList.forEach(item->{
            results.add(ProjectMapVO.convert(item));
        });
        return new ResultDTO(true,results,"成功");
    }
    @GetMapping("getLocaltions")
    public ResultDTO localtions(@RequestParam(value="identityCode",required = false)  String identityCode,
                              @RequestParam(value="uuid",required = false)  String uuid){
        List<PostionVO> results = new LinkedList<>();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(identityCode)){
            return new ResultDTO<>(false,null,"缺少参数 identityCode");
        }
        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);

        if(null== projectInfo){
            return new ResultDTO<>(false,null,"项目还未绑定");
        }

        String startTime  =  DateUtil.format(new Date(),"yyyy-MM-dd")+" 00:00:00";

        Page<ProjectAccuratePositionData> page = new Page<>(1,10000);
        Wrapper<ProjectAccuratePositionData> positionDataWrapper = new EntityWrapper<>();
        positionDataWrapper.setSqlSelect("x_zhou as xZhou","y_zhou as yZhou","collect_time as collectTime");
        positionDataWrapper.where("(select id from t_project_map  where project_id = {0}) = t_project_accurate_position_data.map_id",projectInfo.getId());
        positionDataWrapper.eq("identity_code",identityCode);
        positionDataWrapper.gt("collect_time",startTime);
        positionDataWrapper.orderBy("collect_time",true);

        positionDataService.selectPage(page,positionDataWrapper);
        List<ProjectAccuratePositionData> list = page.getRecords();
        Date last  = null;
        for(ProjectAccuratePositionData item:list){
            if(last ==null){
                last=item.getCollectTime();
                results.add(PostionVO.convert(item));
            }else {
                if (item.getCollectTime().getTime()-last.getTime()  > 1000 * 60 * 3) {
                    last = item.getCollectTime();
                    results.add(PostionVO.convert(item));
                }

            }
        }



        return new ResultDTO<>(true,results,"成功");
    }


}
