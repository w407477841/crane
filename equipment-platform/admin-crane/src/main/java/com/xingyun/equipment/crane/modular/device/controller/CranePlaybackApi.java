package com.xingyun.equipment.crane.modular.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneDetail;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneDetailService;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.crane.modular.device.vo.RealTimeMonitoringTowerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:42 2019/6/21
 * Modified By : wangyifei
 */
@RestController
@Slf4j
public class CranePlaybackApi {
    @Autowired
    private ProjectCraneDetailService craneDetailService;
    @Autowired
    private ProjectCraneService craneService;

    @PostMapping("admin-crane/craneData/api/playbackData")
    public ResultDTO<List<ProjectCraneDetail>> playbackData(@RequestBody RequestDTO<RealTimeMonitoringTowerVo > requestDTO) {
        try {

            Wrapper wrapper =new EntityWrapper();
            wrapper.eq("device_no",requestDTO.getDeviceNo());
            wrapper.between("create_time",requestDTO.getStartTime(),requestDTO.getEndTime());
            wrapper.orderBy("create_time",true);
            List<ProjectCraneDetail>   craneDetailList =  craneDetailService.selectList(wrapper);
            return new ResultDTO<>(true,craneDetailList);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }


    @PostMapping("admin-crane/craneData/api/deviceList")
    public ResultDTO<List<ProjectCrane>> deviceList(@RequestBody RequestDTO<RealTimeMonitoringTowerVo > requestDTO) {
        try {
            Wrapper wrapper =new EntityWrapper();
            wrapper.in("org_id",Const.orgIds.get());
            wrapper.orderBy("create_time",false);
            List<ProjectCrane> cranes =  craneService.selectList(wrapper);
            return new ResultDTO(true,cranes);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }




}
