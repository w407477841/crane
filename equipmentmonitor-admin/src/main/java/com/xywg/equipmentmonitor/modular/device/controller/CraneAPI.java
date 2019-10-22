package com.xywg.equipmentmonitor.modular.device.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;
import com.xywg.equipmentmonitor.modular.device.vo.CraneAPIVO;
import com.xywg.equipmentmonitor.modular.device.vo.EnviromentAPIVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:25 2019/5/6
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/crane")
public class CraneAPI {
    @Autowired
    private ProjectCraneService craneService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @GetMapping("deviceInfo")
    public ResultDTO<List<Map<String,Object>>> deviceStatus(@RequestParam(value="uuid",required = false) String uuid, @RequestParam(value="projectName",required = false) String projectName){
        if(StrUtil.isBlank(uuid)){
            return   new ResultDTO<>(false, null, "uuid 必填");
        }

        if(StrUtil.isBlank(projectName)){
            return  new ResultDTO<>(false, null, "项目名 必填");
        }

        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid",uuid);
        wrapper.eq("name",projectName);
        wrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(wrapper);
        if(projectInfo==null){
            return   new ResultDTO<>(false, null, "项目不存在");
        }

        Wrapper<ProjectCrane> wrapperMonitor = new EntityWrapper<>();
        wrapperMonitor.setSqlSelect("device_no as deviceNo","name as name","is_online as state","'"+projectInfo.getName()+"' as projectName");
        wrapperMonitor.eq("project_id",projectInfo.getId());
        wrapperMonitor.eq("is_del",0);
        List<Map<String,Object>>  mps =  craneService.selectMaps(wrapperMonitor);

        return new ResultDTO<>(true,mps,"请求成功");

    }

    @GetMapping("getInfoDose")
    public ResultDTO<List<CraneAPIVO>> getInfoDose(@RequestParam(value="uuid",required = false) String uuid, @RequestParam(value="projectName",required = false) String projectName, @RequestParam(value="deviceNo",required = false) String deviceNo){

        List<CraneAPIVO> results = new LinkedList<>();

        if(StrUtil.isBlank(uuid)){
            return   new ResultDTO<>(false, null, "uuid 必填");
        }

        if(StrUtil.isBlank(projectName)){
            return  new ResultDTO<>(false, null, "项目名 必填");
        }

        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid",uuid);
        wrapper.eq("name",projectName);
        wrapper.eq("is_del",0);
        ProjectInfo projectInfo =  projectInfoService.selectOne(wrapper);

        if(projectInfo==null){
            return   new ResultDTO<>(false, null, "项目不存在");
        }
        if(StrUtil.isNotBlank(deviceNo)){
            // 单个设备
            String key ="device_platform:current:"+projectInfo.getId()+":crane:"+deviceNo;
            if(!redisUtil.exists(key)){
                //不存在最近一条数据
                CraneAPIVO result = new CraneAPIVO();
                result.setDeviceNo(deviceNo);
                result.setDataStatus(0);
                results.add(result);
            }else{
                String json = (String) redisUtil.get(key);
                CraneAPIVO result = JSONUtil.toBean(json,CraneAPIVO.class) ;
                result.setDataStatus(1);
                results.add(result);

            }

        }else{
            // 需要查项目下所有设备
            Wrapper<ProjectCrane> wrapperMonitor = new EntityWrapper<>();
            wrapper.setSqlSelect("device_no as deviceNo","name as name","is_online as state","'"+projectInfo.getName()+"' as projectName");
            wrapperMonitor.eq("project_id",projectInfo.getId());
            wrapperMonitor.eq("is_del",0);
            List<Map<String,Object>>  mps =  craneService.selectMaps(wrapperMonitor);
            for(Map<String,Object> map: mps){
                String dn = (String) map.get("deviceNo");
                String key ="device_platform:current:"+projectInfo.getId()+":crane:"+dn;
                if(!redisUtil.exists(key)){
                    //不存在最近一条数据
                    CraneAPIVO result = new CraneAPIVO();
                    result.setDeviceNo(dn);
                    result.setDataStatus(0);
                    results.add(result);
                }else{
                    String json = (String) redisUtil.get(key);
                    System.out.println(json);
                    CraneAPIVO result = JSONUtil.toBean(json,CraneAPIVO.class) ;
                    result.setDataStatus(1);
                    results.add(result);
                }

            }

        }
        return new ResultDTO(true,results,"请求成功");
    }


}
