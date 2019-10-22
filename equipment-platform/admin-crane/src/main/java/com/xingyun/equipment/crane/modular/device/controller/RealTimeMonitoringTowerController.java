package com.xingyun.equipment.crane.modular.device.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.StringCompress;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneCyclicWorkDuration;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneDetail;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.crane.modular.device.service.RealTimeMonitoringService;
import com.xingyun.equipment.crane.modular.device.vo.OfflineReasonPieVO;
import com.xingyun.equipment.crane.modular.device.vo.RealTimeMonitoringTowerVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xingyun.equipment.core.enums.FlagEnum.CRANE_7_HOURS_DATA;

/***
 *@author:jixiaojun
 *DATE:2018/8/23
 *TIME:9:27
 */
@RestController
@RequestMapping(value = "/admin-crane/project/realTimeMonitoringTower")
public class RealTimeMonitoringTowerController extends BaseController<RealTimeMonitoringTowerVo,RealTimeMonitoringService> {
    @Autowired
    private ProjectCraneService craneService;

    @Override
    public String insertRole() {
        return null;
    }

    @Override
    public String updateRole() {
        return null;
    }

    @Override
    public String deleteRole() {
        return null;
    }

    @Override
    public String viewRole() {
        return "information:realTower:view";
    }

    @ApiOperation("获取塔吊列表")
    @PostMapping(value = "/selectCrane")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectCrane(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> ts = service.selectCrane(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    /**
     *  设备状态
     * @param requestDTO
     * @return
     */

    @PostMapping("/selectCraneStatus")
    public ResultDTO<Map<String,Integer>> deviceStatusList(@RequestBody RequestDTO<RealTimeMonitoringTowerVo > requestDTO) {
        try {
            // 1 违章 2报警 3提醒 4 在线 5离线
            Map<String,Integer> map  =new HashMap<>(8);
            int weizhang = 0 ;
            int baojing = 0 ;
            int tixing = 0 ;
            int zaixian = 0 ;
            int lixian = 0 ;

            Wrapper wrapper =new EntityWrapper();
            wrapper.eq(StrUtil.isNotBlank(requestDTO.getBody().getDeviceNo()),"device_no",requestDTO.getBody().getDeviceNo());
            wrapper.in("org_id",Const.orgIds.get());
            wrapper.eq(requestDTO.getProjectId()!=null,"project_id",requestDTO.getProjectId());
            wrapper.eq("is_del",0);
            List<ProjectCrane> cranes =  craneService.selectList(wrapper);
            for(ProjectCrane crane:cranes){
                if(1==crane.getAlarmStatus()){
                    tixing++;
                }else if(2==crane.getAlarmStatus()){
                    baojing++;
                }else if(3==crane.getAlarmStatus()){
                    weizhang++;
                }
                if(0==crane.getIsOnline()){
                    lixian++;
                }else{
                    zaixian++;
                }

            }
            map.put("weizhang",weizhang);
            map.put("baojing",baojing);
            map.put("tixing",tixing);
            map.put("zaixian",zaixian);
            map.put("lixian",lixian);



            return new ResultDTO(true,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }


    @ApiOperation("获取运行数据及吊重数据")
    @PostMapping(value = "/selectCraneData")
    public ResultDTO<DataDTO<List<ProjectCraneDetail>>> selectCraneData(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<ProjectCraneDetail> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<ProjectCraneDetail> ts = service.selectCraneData(page,requestDTO);
            return new ResultDTO<DataDTO<List<ProjectCraneDetail>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("吊重数据")
    @PostMapping(value = "/selectWeightData")
    public ResultDTO<DataDTO<List<ProjectCraneCyclicWorkDuration>>> selectWeightData(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<ProjectCraneCyclicWorkDuration> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<ProjectCraneCyclicWorkDuration> ts = service.selectWeightData(page,requestDTO);
            return new ResultDTO<DataDTO<List<ProjectCraneCyclicWorkDuration>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }


    @ApiOperation("获取预警信息")
    @PostMapping(value = "/selectPromptingAlarm")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectPromptingAlarm(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectPromptingAlarm(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取报警信息")
    @PostMapping(value = "/selectWarningAlarm")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectWarningAlarm(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectWarningAlarm(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取违章信息")
    @PostMapping(value = "/selectViolationAlarm")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectViolationAlarm(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectViolationAlarm(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取报警详情")
    @PostMapping(value = "/selectAlarmData")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectAlarmData(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo>   ts=   service.selectAlarmData(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(ts,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("发送短信")
    @PostMapping(value = "/insertMessage")
    public ResultDTO<RealTimeMonitoringTowerVo> insertMessage(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> t) {
        try {
            if(service.insertMessage(t.getBody())) {
                return ResultDTO.resultFactory(OperationEnum.SEND_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.SEND_ERROR);
    }

    @ApiOperation("获取监控状态")
    @PostMapping(value = "/selectMonitorStatus")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectMonitorStatus(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> list = service.selectMonitorStatus(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取运行时间")
    @PostMapping(value = "/selectRunTime")
    public ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>> selectRunTime(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO) {
        try {
            Page<RealTimeMonitoringTowerVo> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            List<RealTimeMonitoringTowerVo> list = service.selectRunTime(page,requestDTO);
            return new ResultDTO<DataDTO<List<RealTimeMonitoringTowerVo>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }
    @ApiOperation("离线原因饼图")
    @PostMapping(value = "/selectOfflineReasonPie")
    public ResultDTO selectOfflineReasonPie(@RequestBody RequestDTO<RealTimeMonitoringTowerVo> requestDTO){

        try {
            List<OfflineReasonPieVO> list = service.selectOfflineReasonPie(requestDTO);
            return new ResultDTO(true,list);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);

    }


    @ApiOperation("近期最后一条数据")
    @GetMapping("getMonitorData")
    public Map<String,Object> getMonitorData(@RequestParam() Map<String, Object> param) {
        String uuid= (String)param.get("uuid");
        String  deviceNo = (String)param.get("deviceNo");
        Map<String,Object> result = new HashMap<>();

        result.put("data",service.getMonitorData(deviceNo,uuid));
        return result;
    }

}
