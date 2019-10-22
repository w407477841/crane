package com.xywg.equipmentmonitor.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.modular.device.dto.*;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmet;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHealthDetail;
import com.xywg.equipmentmonitor.modular.device.service.IHealthAPIService;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetHealthDetailService;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetService;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectArea;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectAreaService;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import lombok.experimental.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

    Double PI = 3.1415926535897932384626;
    Double a = 6378245.0;
    Double ee = 0.00669342162296594323;


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
        healthAPI1DTO.setId(helmetId);
       //查询最后一条健康数据
       HealthInfo info = projectHelmetHealthDetailService.selectLastInfo(helmetId);
       if(info !=null){
           info.setName(projectHelmet.getUserName());
           info.setPhone(projectHelmet.getPhone());
           Long diff=DateUtils.pastMinutes(DateUtils.parseDate(info.getCollectTime()));
           if(diff>10){
               info.setMaozi(0);
           }else{
               info.setMaozi(1);
           }
       }else{
           info = new HealthInfo();
           info.setName(projectHelmet.getUserName());
           info.setPhone(projectHelmet.getPhone());
           info.setMaozi(0);
       }

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
    public ResultDTO<HealthAPI1DTO> getUserinfoTX(String uuid, String code,String startTime,String endTime) {
        ResultDTO<HealthAPI1DTO> result= getUserinfo(uuid,code);

        //查询当前坐标数据
        List<HealthLocation> healthLocations = projectHelmetHealthDetailService.selectLocationByTime(result.getData().getId(),startTime,endTime);
        if(healthLocations.size() ==0){
            List<HealthLocation> locations = new ArrayList<>();
            result.getData().setLocations(locations);
        }
        for(HealthLocation location:healthLocations){
            String[] items=location.getItem().split(",");
            Double lng = Double.parseDouble(items[0]);
            Double lat = Double.parseDouble(items[1]);
            location.setItem(getLocation(lng,lat));

        }
        return result;
    }


    /**
     * 纬度转换
     * @param lng
     * @param lat
     * @return
     */
    public Double  transformlat(Double lng,Double  lat) {
        Double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换
     * @param lng
     * @param lat
     * @return
     */
    public Double transformlng(Double lng,Double  lat) {
        Double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     *
     * @param lng
     * @param lat
     * @return
     */
    public String getLocation(Double lng,Double  lat){
        Double dlat = transformlat(lng - 105.0, lat - 35.0);
        Double dlng = transformlng(lng - 105.0, lat - 35.0);
        Double radlat = lat / 180.0 * PI;
        Double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        Double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        Double mglat = lat + dlat;
        Double mglng = lng + dlng;

        String location = mglng+","+mglat;
        return location;
    }

    @Override
    public ResultDTO<HealthAPI3DTO> getAlarminfo(String uuid,String wechat) {
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
        if(wechat !=null){
            projectHelmetWrapper.eq("wechat",wechat);
        }

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

    /**
     * 微信端用,根据日期查询数据
     * @param uuid
     * @param wechat
     * @return
     */
    @Override
    public ResultDTO<HealthAPI3DTO> getAlarminfoToWechat(String uuid,String wechat,String startTime,String endTime) {
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
        if(wechat !=null){
            projectHelmetWrapper.like("wechat",wechat);
        }

        List<ProjectHelmet> projectHelmet =   projectHelmetService.selectList(projectHelmetWrapper) ;
        if(projectHelmet ==null||projectHelmet.size()==0){
            return new ResultDTO<>(false,null,"未找到绑定设备");
        }
        List<Integer> helmetIds  = new ArrayList<>();
        projectHelmet.forEach(item->{
            helmetIds.add(item.getId());
        });

        return new ResultDTO(true, projectHelmetHealthDetailService.selectAlarmsToWechat(helmetIds,startTime,endTime));
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

    @Override
    public ResultDTO<List<Map<String,Object>>> getUserListInfo(String wechat) {
       List<Map<String,Object>> list = new ArrayList<>();
        Double x_pi = 3.14159265358979324 * 3000.0 / 180.0;


        //2 查询微信绑定的设备
        Wrapper<ProjectHelmet> projectHelmetWrapper = new EntityWrapper<>();
        projectHelmetWrapper.and("instr(wechat,'"+wechat+"') >0");
        projectHelmetWrapper.eq("is_del",0);
        List<ProjectHelmet> projectHelmets =   projectHelmetService.selectList(projectHelmetWrapper) ;
        for(ProjectHelmet projectHelmet:projectHelmets){
            Map<String,Object> map = new HashMap<>();
            List<HealthLocation> healthLocations = projectHelmetHealthDetailService.selectLocations(projectHelmet.getId());
            map.put("userName",projectHelmet.getUserName());
            map.put("phone",projectHelmet.getPhone());
            map.put("idCardNumber",projectHelmet.getIdCardNumber());
            map.put("imei","XYWGAQM"+projectHelmet.getImei());
            if(healthLocations.size()>0){
                String[] item =healthLocations.get(healthLocations.size()-1).getItem().split(",");
                Double lng=Double.parseDouble(item[0]);
                Double lat=Double.parseDouble(item[1]);

                map.put("location",getLocation(lng,lat));
            }else{
                map.put("location","");
            }

            list.add(map);
        }

        return new ResultDTO(true,list);
    }
}
