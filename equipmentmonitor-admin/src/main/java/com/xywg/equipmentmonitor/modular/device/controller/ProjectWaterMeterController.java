package com.xywg.equipmentmonitor.modular.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.config.RedisConfig;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.ProducerUtil;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmet;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeterAlarm;
import com.xywg.equipmentmonitor.modular.device.service.IProjectWaterMeterService;
import com.xywg.equipmentmonitor.modular.device.vo.*;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectDeviceStockServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2018/9/27
 *TIME:16:32
 */
@RestController
@RequestMapping("/ssdevice/device/projectWaterMeter")
public class ProjectWaterMeterController extends BaseController<ProjectWaterMeter,IProjectWaterMeterService> {

    @Autowired
    private ProjectDeviceStockServiceImpl projectDeviceStockService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProducerUtil producerUtil;

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
        return null;
    }
//    @ApiOperation("首页水表接口")
//    @PostMapping("/getWaterInfo")
//    public ResultDTO<ProjectWaterMeterInfoVo> getWaterInfo(@RequestBody RequestDTO<ProjectWaterMeter> request){
//        return service.getWaterInfo(request);
//    }
	@ApiOperation("首页水表接口")
	@GetMapping(value = "/getWaterInfo")
	public ResultDTO<Map<String,Object>> getWaterInfo(@RequestParam Integer orgId) {
		try {
			return service.getWaterInfo(orgId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
    @ApiOperation("获取实时监控塔吊信息")
    @PostMapping("/selectWaterData")
    public ResultDTO<DataDTO<List<ProjectWaterMeterVo>>> selectWaterData(@RequestBody RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception {
        try {
            Page<ProjectWaterMeterVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectWaterMeterVo> list = service.selectWaterData(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取监控状态")
    @PostMapping("/selectMonitorStatus")
    public ResultDTO<DataDTO<List<ProjectWaterMeterHeartbeatVo>>> selectMonitorStatus(@RequestBody RequestDTO<ProjectWaterMeterHeartbeatVo> requestDTO) throws Exception {
        try {
            Page<ProjectWaterMeterHeartbeatVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectWaterMeterHeartbeatVo> list = service.selectMonitorStatus(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取运行数据")
    @PostMapping("/selectRunData")
    public ResultDTO<DataDTO<List<ProjectWaterMeterDetailVo>>> selectRunData(@RequestBody RequestDTO<ProjectWaterMeterDetailVo> requestDTO) throws Exception {
        try {
            Page<ProjectWaterMeterDetailVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectWaterMeterDetailVo> list = service.selectRunData(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取报警数量")
    @PostMapping("/selectWarningAlarm")
    public ResultDTO<DataDTO<List<ProjectWaterMeterAlarmVo>>> selectWarningAlarm(@RequestBody RequestDTO<ProjectWaterMeterAlarmVo> requestDTO) throws Exception {
        try {
            Page<ProjectWaterMeterAlarmVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectWaterMeterAlarmVo> list = service.selectWarningAlarm(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取报警详情")
    @PostMapping("/selectAlarmData")
    public ResultDTO<DataDTO<List<ProjectWaterMeterAlarm>>> selectAlarmData(@RequestBody RequestDTO<ProjectWaterMeterAlarmVo> requestDTO) throws Exception {
        try {
            Page<ProjectWaterMeterAlarm> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectWaterMeterAlarm> list = service.selectAlarmData(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取列表")
    @PostMapping("/selectWater")
    public ResultDTO<DataDTO<List<ProjectWaterMeterVo>>> selectWater(@RequestBody RequestDTO<ProjectWaterMeterVo> requestDTO) {
        try {
            Page<ProjectWaterMeterVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectWaterMeterVo> list = service.selectWater(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("启用")
    @PostMapping("/setUse")
    public ResultDTO<ProjectWaterMeter> setUse(@RequestBody RequestDTO<ProjectWaterMeter> requestDTO) {
        try {
            if(service.setUse(requestDTO)) {
                return ResultDTO.resultFactory(OperationEnum.START_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if("有设备在其他项目中已启用".equals(e.getMessage())) {
                return ResultDTO.factory(ResultCodeEnum.PROJECT_DEVICE_STRAT_REPEAT);
            }
        }
        return ResultDTO.resultFactory(OperationEnum.START_FAIL);
    }

    @ApiOperation("停用")
    @PostMapping("/setDis")
    public ResultDTO<ProjectWaterMeter> setDis(@RequestBody RequestDTO<ProjectWaterMeter> requestDTO) {
        try {
            List<ProjectWaterMeter> list = new ArrayList<>(10);
            Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
            wrapper.in("id",requestDTO.getIds());
            List<ProjectWaterMeter> projectWaterMeters = service.selectList(wrapper);
            for(int i = 0;i < requestDTO.getIds().size();i++) {
                ProjectWaterMeter projectWaterMeter = new ProjectWaterMeter();
                projectWaterMeter.setId(Integer.parseInt(requestDTO.getIds().get(i).toString()));
                projectWaterMeter.setStatus(0);
                projectWaterMeter.setIsOnline(0);
                list.add(projectWaterMeter);
            }
            if(service.updateBatchById(list)) {
                for(int i = 0;i < projectWaterMeters.size();i++) {
                    String deviceNum = projectWaterMeters.get(i).getDeviceNo();
                    String key = RedisConfig.DEVICE_INFO_PREFIX+"water:" + deviceNum;
                    String redisKey = RedisConfig.DEVICE_PLATFORM+"water:"+deviceNum;
                    if(redisUtil.exists(redisKey)) {
                        String value = (String)redisUtil.get(redisKey);
                        String topic = value.split("#")[0];
                        String data = "{\"sn\":\"" + deviceNum + "\",\"type\":\"water\",\"cmd\":\"01\"}";
                        producerUtil.sendCtrlMessage(topic,data);
                    }
                    if(redisUtil.exists(key)) {
                        redisUtil.remove(key);
                    }
                }
                return ResultDTO.resultFactory(OperationEnum.CLOSE_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.CLOSE_FAIL);
    }

    @Override
    public ResultDTO<ProjectWaterMeter> insert(@RequestBody RequestDTO<ProjectWaterMeter> requestDTO) {
        Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no",requestDTO.getBody().getDeviceNo());
        wrapper.eq("project_id",requestDTO.getBody().getProjectId());
        if(service.selectList(wrapper).size() > 0) {
            return ResultDTO.factory(ResultCodeEnum.PROJECT_DEVICE_NO_REPEAT);
        }
        if(service.insert(requestDTO.getBody())) {
            return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    @Override
    public ResultDTO<ProjectWaterMeter> update(@RequestBody RequestDTO<ProjectWaterMeter> requestDTO) {
        Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("device_no",requestDTO.getBody().getDeviceNo());
        wrapper.eq("project_id",requestDTO.getBody().getProjectId());
        wrapper.ne("id",requestDTO.getBody().getId());
        if(service.selectList(wrapper).size() > 0) {
            return ResultDTO.factory(ResultCodeEnum.PROJECT_DEVICE_NO_REPEAT);
        }
        if(service.updateById(requestDTO.getBody())) {
            return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
        }
        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }

    @ApiOperation("获取不分页列表")
    @PostMapping("/selectWaterList")
    public ResultDTO<List<ProjectWaterMeterVo>> selectWaterList(@RequestBody RequestDTO<ProjectWaterMeterVo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectWaterList(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    /**
    * 智慧工地拉取用接口
    * @param res
    * @return
    */
    @GetMapping("/getWaterDetailInfo")
    public byte[] getWaterDetailInfo(RequestDTO<ProjectWaterMeter> res) {
        byte[] result = {};
        try {
            result =  service.getWaterDetailInfo(res);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	 
    /**
    * 智慧工地拉取报警信息
    * @param res
    * @return
    */
    @GetMapping("/getAlarmInfo")
    public byte[] getAlarmInfo(RequestDTO<WaterAlarmVO> res) {
        byte[] result = {};
        try {
            result =  service.getAlarmInfo(res);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	 
    /**
    * 智慧工地拉取报警信息明细
    * @param res
    * @return
    */
    @GetMapping("/getAlarmDetail")
    public byte[] getAlarmDetail(RequestDTO<WaterAlarmVO> res) {
        byte[] result = {};
        try {
            result =  service.getAlarmDetail(res);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("单条查询")
    @PostMapping("/selectWaterById")
    public ResultDTO<ProjectWaterMeter> selectWaterById(@RequestBody RequestDTO<ProjectWaterMeter> projectWaterMeterRequestDTO) {
	     try {
             return new ResultDTO<>(true,service.selectWaterById(projectWaterMeterRequestDTO.getId()));
         }catch (Exception e) {
	         e.printStackTrace();
         }
         return new ResultDTO<>(false);
    }

    @PostMapping("deletes")
    @Override
    public ResultDTO<ProjectWaterMeter>  deletes(@RequestBody  RequestDTO<ProjectWaterMeter> t){
        hasPermission(deleteRole());
        try {
            List<ProjectDeviceStock> list= new ArrayList<>();
            for(int i=0;i<t.getIds().size();i++){
                Integer id= (Integer) t.getIds().get(i);
                EntityWrapper<ProjectWaterMeter> wrapper =new EntityWrapper<>();
                wrapper.eq("id",id);
                ProjectWaterMeter water=  service.selectOne(wrapper);
                if(water !=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("device_no",water.getDeviceNo());
                    map.put("is_del",0);
                    List<ProjectDeviceStock> stocks= projectDeviceStockService.selectByMap(map);
                    if(stocks.size()>0){
                        ProjectDeviceStock stock = stocks.get(0);
                        stock.setStatus(0);
                        list.add(stock);
                    }
                }


            }
            if(service.deleteBatchIds(t.getIds())){
                if(list.size()>0){
                    projectDeviceStockService.updateAllColumnBatchById(list);
                    return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
        }
        return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
    }
}
