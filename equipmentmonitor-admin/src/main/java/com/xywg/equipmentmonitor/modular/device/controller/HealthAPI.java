package com.xywg.equipmentmonitor.modular.device.controller;

import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI1DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI3DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI4DTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectInfoDTO;
import com.xywg.equipmentmonitor.modular.device.service.IHealthAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:11 2018/11/23
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/health")
public class HealthAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthAPI.class);
    @Autowired
    private IHealthAPIService  healthAPIService;




    @GetMapping("userinfo")
    public ResultDTO<HealthAPI1DTO> healthAPI1(@RequestParam(value = "code") String code,@RequestParam(value="uuid") String uuid){
        // todo 调用 项目的工人信息

        ResultDTO<HealthAPI1DTO>  result ;
        try {
            result =      healthAPIService.getUserinfo(uuid,code);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }

    /**
     * 百度地址转腾讯地址
     * @param code
     * @param uuid
     * @return
     */
    @GetMapping("userinfoTX")
    public ResultDTO<HealthAPI1DTO> healthAPI1TX(@RequestParam(value = "code") String code,@RequestParam(value="uuid") String uuid,@RequestParam(value="startTime",required = false)String startTime,@RequestParam(value="endTime",required = false)String endTime){
        // todo 调用 项目的工人信息

        ResultDTO<HealthAPI1DTO>  result ;
        try {
            result =      healthAPIService.getUserinfoTX(uuid,code,startTime,endTime);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }
    @GetMapping("alarmlist")
    public ResultDTO<HealthAPI3DTO> healthAPI3DTOResult(@RequestParam(value="uuid") String uuid,@RequestParam(value="wechat",required = false) String wechat){
        ResultDTO<HealthAPI3DTO>  result ;
        try {
            result =      healthAPIService.getAlarminfo(uuid,wechat);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }

    @GetMapping("alarmlistToWechat")
    public ResultDTO<HealthAPI3DTO> getAlarmListInfo(@RequestParam(value="uuid",required = true) String uuid,@RequestParam(value="wechat",required = true) String wechat,@RequestParam(value="startTime",required = false) String startTime,@RequestParam(value="endTime",required = false) String endTime){
        ResultDTO<HealthAPI3DTO>  result ;
        try {
            result =      healthAPIService.getAlarminfoToWechat(uuid,wechat,startTime,endTime);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }


    @GetMapping("projectinfo")
    public ResultDTO<ProjectInfoDTO> healthAPI4DTOResult(@RequestParam(value="uuid") String uuid){
        //TODO 返回工地平面图
        ResultDTO<ProjectInfoDTO>  result ;
        try {
            result =      healthAPIService.getProjectinfo(uuid);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }

    @GetMapping("userlocation")
    public   ResultDTO<List<HealthAPI4DTO>> userlocation(@RequestParam(value="uuid") String uuid){
        ResultDTO<List<HealthAPI4DTO>>  result = null ;
        try {
            result =  healthAPIService. userlocation(uuid);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }

    @GetMapping("userListinfo")
    public ResultDTO<List<Map<String,Object>>> userListinfo(@RequestParam(value="wechat",required = true) String wechat){
        // todo 调用 项目的所有工人信息

        ResultDTO<List<Map<String,Object>>>  result ;
        try {
            result =      healthAPIService.getUserListInfo(wechat);
        }catch (Exception ex){
            result = new ResultDTO<>(false,null,ex.getMessage());
        }
        return result;
    }



}
