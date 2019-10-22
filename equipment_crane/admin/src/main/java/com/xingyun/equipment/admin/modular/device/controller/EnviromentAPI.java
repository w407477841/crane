package com.xingyun.equipment.admin.modular.device.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.RedisUtil;
import com.xingyun.equipment.admin.core.vo.CurrentMonitorDataVO;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.admin.modular.device.service.ProjectEnvironmentMonitorService;
import com.xingyun.equipment.admin.modular.device.vo.EnviromentAPIVO;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import com.xingyun.equipment.admin.modular.projectmanagement.vo.ProjectDeviceInfoVO;
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

import static com.xingyun.equipment.admin.core.common.constant.FlagEnum.MONITOR_7_HOURS_DATA;

/**
 * @author : wangyifei
 * Description 环境监测
 * Date: Created in 13:08 2019/3/20
 * Modified By : wangyifei
 *
 */
@RestController
@RequestMapping("ssdevice/enviroment")
public class EnviromentAPI {
    @Autowired
    private ProjectEnvironmentMonitorService environmentMonitorService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviromentAPI.class);

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

        Wrapper<ProjectEnvironmentMonitor> wrapperMonitor = new EntityWrapper<>();
        wrapperMonitor.setSqlSelect("device_no as deviceNo","name as name","is_online as state","'"+projectInfo.getName()+"' as projectName");
        wrapperMonitor.eq("project_id",projectInfo.getId());
        wrapperMonitor.eq("is_del",0);
        List<Map<String,Object>>  mps =  environmentMonitorService.selectMaps(wrapperMonitor);

        return new ResultDTO<>(true,mps,"请求成功");

    }

    @GetMapping("getInfoDose")
    public ResultDTO<List<EnviromentAPIVO>> getInfoDose(@RequestParam(value="uuid",required = false) String uuid, @RequestParam(value="projectName",required = false) String projectName, @RequestParam(value="deviceNo",required = false) String deviceNo){

        List<EnviromentAPIVO> results = new LinkedList<>();

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
            String key ="device_platform:current:"+projectInfo.getId()+":monitor:"+deviceNo;
            if(!redisUtil.exists(key)){
                //不存在最近一条数据
                EnviromentAPIVO result = new EnviromentAPIVO();
                result.setDeviceNo(deviceNo);
                result.setDataStatus(0);
                results.add(result);
            }else{
                String json = (String) redisUtil.get(key);
                EnviromentAPIVO result = JSONUtil.toBean(json,EnviromentAPIVO.class) ;
                result.setDataStatus(1);
                results.add(result);

            }

        }else{
            // 需要查项目下所有设备
            Wrapper<ProjectEnvironmentMonitor> wrapperMonitor = new EntityWrapper<>();
            wrapper.setSqlSelect("device_no as deviceNo","name as name","is_online as state","'"+projectInfo.getName()+"' as projectName");
            wrapperMonitor.eq("project_id",projectInfo.getId());
            wrapperMonitor.eq("is_del",0);
            List<Map<String,Object>>  mps =  environmentMonitorService.selectMaps(wrapperMonitor);
            for(Map<String,Object> map: mps){
                String dn = (String) map.get("deviceNo");
                String key ="device_platform:current:"+projectInfo.getId()+":monitor:"+dn;
                if(!redisUtil.exists(key)){
                    //不存在最近一条数据
                    EnviromentAPIVO result = new EnviromentAPIVO();
                    result.setDeviceNo(dn);
                    result.setDataStatus(0);
                    results.add(result);
                }else{
                    String json = (String) redisUtil.get(key);
                    EnviromentAPIVO result = JSONUtil.toBean(json,EnviromentAPIVO.class) ;
                    result.setDataStatus(1);
                    results.add(result);
                }

            }

        }



        return new ResultDTO<>(true,results,"请求成功");


    }


}
