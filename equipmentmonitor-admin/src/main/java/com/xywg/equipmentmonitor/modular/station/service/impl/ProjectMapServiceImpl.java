package com.xywg.equipmentmonitor.modular.station.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.KeyUtil;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.BindDTO;
import com.xywg.equipmentmonitor.modular.station.dto.ProjectMapDTO;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDevice;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.xywg.equipmentmonitor.modular.station.dao.ProjectMapMapper;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMapFloor;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMapStation;
import com.xywg.equipmentmonitor.modular.station.service.IProjectDeviceService;
import com.xywg.equipmentmonitor.modular.station.service.IProjectMapFloorService;
import com.xywg.equipmentmonitor.modular.station.service.IProjectMapService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.modular.station.service.IProjectMapStationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
@Service
public class ProjectMapServiceImpl extends ServiceImpl<ProjectMapMapper, ProjectMap> implements IProjectMapService {
     @Autowired
    IProjectMapFloorService  projectMapFloorService;
    @Autowired
    IProjectMapService projectMapService;
    @Autowired
    IProjectInfoService projectInfoService;
    @Autowired
    IProjectMapStationService projectMapStationService;
    @Autowired
    IProjectDeviceService projectDeviceService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertProjectMap(ProjectMapDTO mapDTO) {

        ProjectMap inObj = new ProjectMap();
        BeanUtils.copyProperties(mapDTO,inObj);
        this.insert(inObj);
        List<ProjectMapFloor> mapFloors =new LinkedList<>();
        Integer lc = 2 ;
        if(lc.equals(inObj.getType())){
            //todo 需要楼号楼层
            List<Integer> floors = new LinkedList<>();
            mapDTO.getFloors().forEach(item->{
                floors.add(item.getId());
            });
            Wrapper<ProjectMapFloor> mapFloorWrapper = new EntityWrapper<>();
            mapFloorWrapper.in("floor",floors);
            projectMapFloorService.delete(mapFloorWrapper);
            mapDTO.getFloors().forEach(item->{
                ProjectMapFloor projectMapFloor = new ProjectMapFloor();
                projectMapFloor.setMapId(inObj.getId());
                projectMapFloor.setFloor(item.getId());
                mapFloors.add(projectMapFloor)  ;
            });

            if(mapFloors.size()>0){
                projectMapFloorService.insertBatch(mapFloors);
            }

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO bind(BindDTO bindDTO) {
        String uuid = bindDTO.getUuid();
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }

        // 查询 与项目有关的地图 及 基站关系
        Wrapper<ProjectMap> mapWrapper = new EntityWrapper<>();
        mapWrapper.eq("project_id",projectInfo.getId());
        List<ProjectMap> mapList = projectMapService.selectList(mapWrapper);
        mapList.forEach(item->{
            projectMapStationService.deleteByMapId(item.getId());
        });
        this.deleteByProject(projectInfo.getId());

        ProjectMap projectMap = bindDTO.getProjectMap();
        projectMap.setProjectId(projectInfo.getId());
        projectMap.setCreateTime(new Date());
        projectMap.setCreateUser(projectInfo.getCreateUser());
        // 新增地图
        projectMapService.insert(projectMap);

        //  将基站设为未启用
        ProjectDevice projectDevice = new ProjectDevice();
        projectDevice.setStatus(0);
        projectDevice.setModifyTime(new Date());
        projectDevice.setModifyUser(projectInfo.getCreateUser());
        Wrapper<ProjectDevice> deviceWrapper = new EntityWrapper<>();
        deviceWrapper.eq("project_id",projectInfo.getId());
        deviceWrapper.eq("current_flag",0);
        deviceWrapper.eq("type",2);
        deviceWrapper.eq("is_del",0);
        // 等下用来删除设备缓存
        List<ProjectDevice> deviceList = projectDeviceService.selectList(deviceWrapper);

        projectDeviceService.update(projectDevice,deviceWrapper);

        List<ProjectMapStation> mapStations =  bindDTO.getMapStations();
        if(CollectionUtils.isEmpty(mapStations)){
            // 说明只是添加一张图，安全帽的场景
            return new ResultDTO<>(true,null,"成功");
        }
        // 将基站设备启用
        List<ProjectDevice> deviceIds = new LinkedList<>();
        mapStations.forEach(item->{
            ProjectDevice  device = new ProjectDevice();
            device.setId(item.getStationId());
            device.setStatus(1);
            device.setModifyTime(new Date());
            device.setModifyUser(projectInfo.getCreateUser());
            deviceIds.add(device);
        });
            projectDeviceService.updateBatchById(deviceIds);
        // 新增关系
        if(mapStations.size()>0) {
            Map<String,String> map = new HashMap();
            for (int i = 0, l = mapStations.size(); i < l; i++) {
                if(map.containsKey(""+mapStations.get(i).getStationId())){
                    return new ResultDTO<>(false,null,"不要重复选择同一个设备");
                }
                mapStations.get(i).setMapId(projectMap.getId());
                mapStations.get(i).setCreateTime(new Date());
                mapStations.get(i).setCreateUser(projectInfo.getCreateUser());
                map.put(""+mapStations.get(i).getStationId(),"1");
            }
            projectMapStationService.insertBatch(mapStations);

            deviceList.forEach(device->{
                // 把缓存清清
                String deviceNo = device.getDeviceNo();
                String masterkey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_master,"sn",deviceNo);
                String slavekey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_slave,"sn",deviceNo);
                String mapKey =  KeyUtil.key(KeyUtil.database, KeyUtil.table_map, "sn", deviceNo);
                 redisUtil.remove(mapKey,masterkey);
                 redisUtil.removePattern(slavekey+"*");
            });
        }

        return new ResultDTO<>(true,null,"成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByProject(Integer projectId) {

        baseMapper.deleteByProject(projectId);

    }
}
