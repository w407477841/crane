package com.xywg.equipmentmonitor.modular.device.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPower;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;
import com.xywg.equipmentmonitor.modular.device.service.impl.ProjectHelmetStockServiceImpl;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectDeviceStockServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectCraneDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneDetailVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneOrgVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneVO;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 * 塔吊
 * @author xss
 * @since 2018-08-22
 */
@RestController
@RequestMapping("/ssdevice/device/projectCrane")
public class ProjectCraneController extends BaseController<ProjectCrane, ProjectCraneService> {


    @Autowired
    private ProjectDeviceStockServiceImpl projectDeviceStockService;

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

    @ApiOperation("条件分页查询")
    @RequestMapping("/selectPageList")
    public ResultDTO<DataDTO<List<ProjectCraneVO>>> selectPageList(@RequestBody RequestDTO<ProjectCraneVO> request) {
        return service.getPageList(request);
    }

    @ApiOperation("判重")
    @PostMapping("/checkByDeviceNoAndProjectId")
    public ResultDTO<DataDTO<List<ProjectCrane>>> checkByDeviceNoAndProjectId(@RequestBody RequestDTO<ProjectCrane> request) {
        return service.checkByDeviceNoAndProjectId(request);
    }

    @ApiOperation("新增")
    @PostMapping("insertInfo")
    public ResultDTO<Object> insertInfo(@RequestBody RequestDTO<ProjectCraneDTO> request) {
        try {
            return service.insertInfo(request);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("查询单条")
    @PostMapping("selectInfo")
    public ResultDTO<ProjectCraneDTO> selectInfo(@RequestBody RequestDTO<ProjectCrane> request) {
        return service.selectInfo(request);
    }

    @ApiOperation("编辑")
    @PostMapping("updateInfo")
    public ResultDTO<Object> updateInfo(@RequestBody RequestDTO<ProjectCraneDTO> request) {
        try {
            return service.updateInfo(request);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("启用")
    @PostMapping("updateStatus")
    public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectCrane> request) {
        try {
            return service.updateStatus(request);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("地图不分页查询")
    @PostMapping("/selectListMap")
    public ResultDTO<List<ProjectCraneVO>> selectListMap(@RequestBody RequestDTO<ProjectCraneVO> res){
        return service.selectListMap(res);
    }
    @ApiOperation("查询集团下所有塔吊(接口)")
    @PostMapping("/getCraneList")
    public ResultDTO<ProjectCraneOrgVO> getCraneList(@RequestBody RequestDTO<ProjectCraneVO> request) {
        return service.selectCraneList(request);
    }
    @ApiOperation("查询塔吊最近一条数据(接口)")
    @PostMapping("/getCraneDetail")
    public ResultDTO<ProjectCraneDetailVO> getCraneDetail(@RequestBody RequestDTO<ProjectCraneVO> request) {
        return service.selectCraneDetail(request);
    }

    /**
     *
     * @param uuid
     * @param deviceNo
     * @return
     */
    @ApiOperation("查询塔吊模拟数据")
    @GetMapping("/getCraneDetails")
    public ResultDTO<ProjectCraneDetailVO> getTop100CraneDetails(String uuid,String deviceNo){


        return   service.selectTop100CraneDetails(uuid,deviceNo);

    }

    @PostMapping("deletes")
    @Override
    public ResultDTO<ProjectCrane>  deletes(@RequestBody  RequestDTO<ProjectCrane> t){
        hasPermission(deleteRole());
        try {
            List<ProjectDeviceStock> list= new ArrayList<>();
            for(int i=0;i<t.getIds().size();i++){
                Integer id= (Integer) t.getIds().get(i);
                EntityWrapper<ProjectCrane> wrapper =new EntityWrapper<>();
                wrapper.eq("id",id);
                ProjectCrane crane=  service.selectOne(wrapper);
                if(crane !=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("device_no",crane.getDeviceNo());
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

    /**
     * 转发
     * @param res
     * @return
     */
    @ApiOperation("转发")
    @PostMapping("updateDispatch")
    public ResultDTO<Object> updateDispatch(@RequestBody RequestDTO<ProjectCrane> res) {
        try {
            return service.updateDispatch(res);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }
}

