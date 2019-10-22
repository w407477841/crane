package com.xywg.equipmentmonitor.modular.station.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.BindMapDTO;
import com.xywg.equipmentmonitor.modular.station.dto.ProjectMapDTO;
import com.xywg.equipmentmonitor.modular.station.model.*;
import com.xywg.equipmentmonitor.modular.station.service.*;
import com.xywg.equipmentmonitor.modular.station.vo.FloorVO;
import com.xywg.equipmentmonitor.modular.station.vo.LouVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : wangyifei
 * Description 设备平台API
 * Date: Created in 8:51 2019/3/22
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/station2sbpt")
public class Station2sbptAPI {

    @Autowired
     private  IProjectInfoService  projectInfoService;
    @Autowired
     private  IProjectMapService projectMapService;
    @Autowired
     private  IProjectMapStationService  projectMapStationService;
    @Autowired
     private  IProjectDeviceStockService  projectDeviceStockService;
    @Autowired
     private  IProjectFloorService  projectFloorService;
        @Autowired
    private IProjectDeviceService  projectDeviceService;

    /**
     *  查询当前用户的项目列表
      * @return
     */
    @GetMapping("getProjects")
    public ResultDTO<ProjectInfo> getProjects(){
        List<Integer> orgIds  =  Const.orgIds.get();
        Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
        wrapper.in("org_id",orgIds);
        wrapper.orderBy("create_time",false);
        return new ResultDTO(true,projectInfoService.selectList(wrapper),"成功");
    }
    /**
    *  由于基站跟项目没有关系，所以只好通过限制参数来过滤数据，否则太多不好找展示
     *  限制每次只返回10条，根据 create_time 倒叙
     * 查询基站 根据deviceNo
     *
     * @param deviceNo ?
     */
    @GetMapping("getStations")
    public ResultDTO<Object> getStations(@RequestParam(value="deviceNo",required = false) String deviceNo,@RequestParam(value="projectId",required = false) Integer projectId){

        Wrapper<ProjectDevice> wrapper  = new EntityWrapper<>();
        wrapper.eq("project_id",projectId);
        wrapper.eq("type",2);
        wrapper.like(StrUtil.isNotBlank(deviceNo),"deviceNo",deviceNo);
        return new ResultDTO(true, projectDeviceService.selectList(wrapper),"成功");
    }

    /**
     *  查询楼号楼层
     * @param projectId
     * @return
     */
    @GetMapping("getFloors")
    public ResultDTO<Object> getFloors(@RequestParam(value="projectId") String projectId){
        Wrapper<ProjectFloor> wrapper =  new EntityWrapper<>();
        wrapper.setSqlSelect("id","pid","name","if((select count(1) from  t_project_map_floor where is_del=0 and  t_project_map_floor.floor = t_project_floor.id)>0,1,0) `status`");
        wrapper.eq("project_id",projectId);
        // 判断是否已被选中
        List<ProjectFloor> lists = projectFloorService.selectList(wrapper);
        List<LouVO> louhao = new  LinkedList();
        projectFloorService.packageFloors(lists,louhao);
    return new ResultDTO(true,louhao,"成功");
    }




    /**
     * 新增地图
     * @param projectMapVO ?
     * @return
     */
    @PostMapping("addMap")
    public ResultDTO<Object> addMap(@RequestBody ProjectMapDTO projectMapVO){
        try {
            projectMapService.insertProjectMap(projectMapVO);
            return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
        } catch (Exception e) {e.printStackTrace(); }
            return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    /**
     * 根据项目查地图
     * @param projectId ?
     * @return
     */
    @GetMapping("getMapsByProjectId")
    public ResultDTO<List<ProjectMap>> getMapsByProjectId(@RequestParam(value="projectId") Integer projectId){
        Wrapper<ProjectMap> wrapper = new EntityWrapper<>();
        wrapper.where("(select count(1) from t_project_map_floor where t_project_map_floor.map_id=t_project_map.id and  t_project_map_floor.is_del = 0)>0");
        wrapper.eq("project_id",projectId);
        wrapper.orderBy("create_time",false);
        return new ResultDTO(true,projectMapService.selectList(wrapper),"成功");
    }

    @GetMapping("getFloorsByMapId")
    public ResultDTO<Object> getFloorsByMapId(@RequestParam(value="mapId")Integer mapId){
        ProjectMap projectMap =   projectMapService.selectById(mapId);
        Integer lc = 2 ;
         if(projectMap.getType().equals(lc)){
             Wrapper<ProjectFloor> wrapper = new EntityWrapper<>();
             wrapper.eq("map_id",mapId);
             wrapper.eq("is_del",0);
             wrapper.orderBy("create_time",false);
             List<ProjectFloor> lists = projectFloorService.selectList(wrapper);
             List<LouVO> louhao = new  LinkedList();
             projectFloorService.packageFloors(lists,louhao);
            return    new ResultDTO(true,louhao,"成功");
         }else{
             return   new ResultDTO(false,null,"该图属于项目图，无楼号楼层");
         }
    }


    /**
     * 基站绑定展示图，并标注位置
     * @param bindMapDTO ?
     * @return
     */
    @PostMapping("bindMap")
    public ResultDTO<Object> bindMap(@RequestBody BindMapDTO bindMapDTO){
        try {

            projectMapStationService.insertBatch(bindMapDTO.getMapStations());
            return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }











}
