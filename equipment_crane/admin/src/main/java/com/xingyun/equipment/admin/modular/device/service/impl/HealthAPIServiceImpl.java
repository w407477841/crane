package com.xingyun.equipment.admin.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dto.*;
import com.xingyun.equipment.admin.modular.device.model.ProjectHelmet;
import com.xingyun.equipment.admin.modular.device.model.ProjectHelmetHealthDetail;
import com.xingyun.equipment.admin.modular.device.service.IHealthAPIService;
import com.xingyun.equipment.admin.modular.device.service.IProjectHelmetHealthDetailService;
import com.xingyun.equipment.admin.modular.device.service.IProjectHelmetService;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectArea;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectAreaService;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:34 2018/11/23
 * Modified By : wangyifei
 */
@Service
public class HealthAPIServiceImpl implements IHealthAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthAPIServiceImpl.class);
    @Autowired
    private IProjectInfoService   projectInfoService;
    @Autowired
    private IProjectHelmetService  projectHelmetService;
    @Autowired
    private IProjectHelmetHealthDetailService  projectHelmetHealthDetailService;
    @Autowired
    private IProjectAreaService  projectAreaService;

    @Override
    public ResultDTO<HealthAPI1DTO> getUserinfo(String uuid, String code) {

        HealthAPI1DTO healthAPI1DTO = new HealthAPI1DTO();
        healthAPI1DTO.setCode(code);
        //1 查询项目
        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);
        if(projectInfo ==null) {
            return new ResultDTO<>(false,null,"uuid有误");
        }
        //2 查询code绑定的设备
        Wrapper<ProjectHelmet> projectHelmetWrapper = new EntityWrapper<>();
        projectHelmetWrapper.eq("id_card_number",code);
        projectHelmetWrapper.eq("project_id",projectInfo.getId());
        projectHelmetWrapper.eq("is_del",0);
        ProjectHelmet projectHelmet =   projectHelmetService.selectOne(projectHelmetWrapper) ;
        if(projectHelmet ==null){
            return new ResultDTO<>(false,null,"未找到绑定设备");
        }
        int helmetId = projectHelmet.getId();
       //查询最后一条健康数据
       HealthInfo info = projectHelmetHealthDetailService.selectLastInfo(helmetId);
        healthAPI1DTO.setInfo(info);

        //查询当前坐标数据
       List<HealthLocation> healthLocations = projectHelmetHealthDetailService.selectLocations(helmetId);
       healthAPI1DTO.setLocations(healthLocations);

       //查询当天健康数据
       List<ProjectHelmetHealthDetail>  helmetHealthDetails = projectHelmetHealthDetailService.selectDetails(helmetId);
       List<String> time = new ArrayList<>();
       List<BigDecimal> temperatures = new ArrayList<>();
        List<Integer> oxygens = new ArrayList<>();
        List<BigDecimal> pressures = new ArrayList<>();
       for(ProjectHelmetHealthDetail detail:helmetHealthDetails){
           time.add(DateUtil.format(detail.getCollectTime(),"yyyy-MM-dd HH:mm:ss"));
           temperatures.add(detail.getTemperature());
           oxygens.add(detail.getBloodOxygen());
           pressures.add(null);
       }
        healthAPI1DTO.setTime(time);
       healthAPI1DTO.setTemperatures(temperatures);
       healthAPI1DTO.setOxygen(oxygens);
       healthAPI1DTO.setPressures(pressures);
        return new ResultDTO(true,healthAPI1DTO);
    }

    @Override
    public ResultDTO<HealthAPI3DTO> getAlarminfo(String uuid) {
        //1 查询项目
        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);
        if(projectInfo ==null) {
            return new ResultDTO<>(false,null,"uuid有误");
        }
        //2 查询code绑定的设备
        Wrapper<ProjectHelmet> projectHelmetWrapper = new EntityWrapper<>();
        projectHelmetWrapper.eq("project_id",projectInfo.getId());
        projectHelmetWrapper.eq("is_del",0);
        List<ProjectHelmet> projectHelmet =   projectHelmetService.selectList(projectHelmetWrapper) ;
        if(projectHelmet ==null||projectHelmet.size()==0){
            return new ResultDTO<>(false,null,"未找到绑定设备");
        }
        List<Integer> helmetIds  = new ArrayList<>();
        projectHelmet.forEach(item->{
            helmetIds.add(item.getId());
        });

        return new ResultDTO(true, projectHelmetHealthDetailService.selectAlarms(helmetIds));
    }

    @Override
    public ResultDTO<ProjectInfoDTO> getProjectinfo(String uuid) {
        //1 查询项目
        Wrapper<ProjectInfo> projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        projectInfoWrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(projectInfoWrapper);
        if(projectInfo ==null) {
            return new ResultDTO<>(false,null,"uuid有误");
        }
        ProjectInfoDTO projectInfoDTO = new ProjectInfoDTO();
     /*   Integer areaId = projectInfo.getPosition();
        if(areaId!=null){
            ProjectArea projectArea =  projectAreaService.selectById(areaId);
            if(projectArea!=null){
            projectInfoDTO.setAddr(projectArea.getCode()+"");
            }
        }
*/
        projectInfoDTO.setName(projectInfo.getName());
        projectInfoDTO.setUuid(projectInfo.getUuid());
        projectInfoDTO.setIchnography(projectInfo.getIchnography());
        projectInfoDTO.setPlacePoint(projectInfo.getPlacePoint());
        projectInfoDTO.setProjectScope(projectInfo.getProjectScope());
        return new ResultDTO<>(true,projectInfoDTO);

    }

    @Override
    public ResultDTO<List<HealthAPI4DTO>> userlocation(String uuid) {

        return new ResultDTO<>(true,projectHelmetHealthDetailService.userlocation(uuid));
    }
}
