package com.xingyun.equipment.crane.modular.device.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.xingyun.equipment.crane.core.util.ProducerUtil;
import com.xingyun.equipment.crane.modular.device.dao.ProjectCraneMapper;
import com.xingyun.equipment.crane.modular.device.dao.ProjectLiftMapper;
import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.model.ProjectLift;
import com.xingyun.equipment.crane.modular.device.vo.ProjectEnvironmentMonitorVO;
import com.xingyun.equipment.crane.modular.projectmanagement.dao.ProjectInfoMapper;
import com.xingyun.equipment.crane.modular.projectmanagement.model.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.crane.core.util.StringCompress;
import com.xingyun.equipment.crane.modular.device.dao.ProjectEnvironmentMonitorMapper;
import com.xingyun.equipment.crane.modular.device.dao.ProjectEnvironmentMonitorVideoMapper;
import com.xingyun.equipment.crane.modular.device.dto.ProjectEnvironmentMonitorDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitorVideo;
import com.xingyun.equipment.crane.modular.device.service.ProjectEnvironmentMonitorService;
import com.xingyun.equipment.crane.modular.device.vo.MonitorAlarmVO;
import com.xingyun.equipment.crane.modular.projectmanagement.service.IProjectInfoService;

import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public class ProjectEnvironmentMonitorServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorMapper, ProjectEnvironmentMonitor> implements ProjectEnvironmentMonitorService {

    @Autowired
    private ProjectEnvironmentMonitorVideoMapper videoMapper;
    @Autowired
    private ProjectCraneMapper projectCraneMapper;
    @Autowired
    private ProjectLiftMapper projectLiftMapper;

    @Autowired
    ZbusProducerHolder  zbusProducerHolder;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProducerUtil producerUtil;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 分页查询列表
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectEnvironmentMonitor>>> selectPageList(RequestDTO<ProjectEnvironmentMonitor> res) {

        Page<ProjectEnvironmentMonitor> page = new Page<>(res.getPageNum(), res.getPageSize());
        EntityWrapper<RequestDTO<ProjectEnvironmentMonitor>> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        if(res.getId()!=null && !"".equals(res.getId())) {
//        	ew.like("b.name", res.getKey());
        	 ew.eq("a.project_id", res.getId());
        }

        if (res.getBody() != null) {
            if (res.getBody().getProjectId() != null) {
                ew.eq("a.project_id", res.getBody().getProjectId());
            }
            if (res.getBody().getIsOnline() != null) {
                ew.eq("a.is_online", res.getBody().getIsOnline());
            }
        }
        List<Integer>   orgIds=Const.orgIds.get();
        ew.in("a.org_id",orgIds);
        List<ProjectEnvironmentMonitor> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     * 设备编号重复性验证
     */
    @Override
    public ResultDTO<Object> countDevice(String deviceNo, Integer projectId) {
        Long count = baseMapper.countDevice(deviceNo, projectId);

        return new ResultDTO<>(true, count);

    }

    /**
     * 查询单条详情
     */
    @Override
    public ResultDTO<ProjectEnvironmentMonitorDTO> selectInfo(RequestDTO<ProjectEnvironmentMonitor> res) {

        ProjectEnvironmentMonitor monitor = baseMapper.selectById(res.getId());
        ProjectInfo projectInfo = projectInfoMapper.selectById(monitor.getProjectId());
        ProjectEnvironmentMonitorVO projectEnvironmentMonitorVO = new ProjectEnvironmentMonitorVO();
        BeanUtil.copyProperties(monitor,projectEnvironmentMonitorVO);
        projectEnvironmentMonitorVO.setProjectName(projectInfo.getName());
        EntityWrapper<ProjectEnvironmentMonitorVideo> ew = new EntityWrapper<ProjectEnvironmentMonitorVideo>();
        ew.eq("monitor_id", res.getId());
        List<ProjectEnvironmentMonitorVideo> videos = videoMapper.selectList(ew);
        ProjectEnvironmentMonitorDTO monitorDTO = new ProjectEnvironmentMonitorDTO();
        monitorDTO.setMonitor(projectEnvironmentMonitorVO);
        monitorDTO.setVideos(videos);
        return new ResultDTO<ProjectEnvironmentMonitorDTO>(true, monitorDTO);
    }

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<Object> insertInfo(RequestDTO<ProjectEnvironmentMonitorDTO> res) {
        ProjectEnvironmentMonitor monitor = new ProjectEnvironmentMonitor();
        ProjectEnvironmentMonitorVO projectEnvironmentMonitorVO = res.getBody().getMonitor();
        BeanUtil.copyProperties(projectEnvironmentMonitorVO,monitor);
        List<ProjectEnvironmentMonitorVideo> videos = res.getBody().getVideos();
        monitor.setStatus(0);
        monitor.setIsOnline(0);

        baseMapper.plusCallTimes(monitor.getSpecification(), monitor.getManufactor());
        baseMapper.insert(monitor);
        String uuid=projectInfoService.selectById(monitor.getProjectId()).getUuid();
        try {
			zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(monitor),"add","monitor");
		} catch (Exception e) {
			e.printStackTrace();
		}
        Integer monitorId = monitor.getId();
        for (int i = 0; i < videos.size(); i++) {
            ProjectEnvironmentMonitorVideo video = videos.get(i);
            video.setMonitorId(monitorId);

            videoMapper.insert(video);
        }
        return new ResultDTO<>(true,null,"新增成功");
    }

    /**
     * 编辑
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<Object> updateInfo(RequestDTO<ProjectEnvironmentMonitorDTO> res) {
        String key = "device_platform:deviceinfo:monitor:" + res.getBody().getMonitor().getDeviceNo();
        if(redisUtil.exists(key)) {
            redisUtil.remove(key);
        }
        ProjectEnvironmentMonitor monitor = res.getBody().getMonitor();
        List<ProjectEnvironmentMonitorVideo> videos = res.getBody().getVideos();
        ProjectEnvironmentMonitor monitorTemp = baseMapper.selectById(monitor.getId());
        baseMapper.minusCallTimes(monitorTemp.getSpecification(), monitorTemp.getManufactor());
        baseMapper.plusCallTimes(monitor.getSpecification(), monitor.getManufactor());
        baseMapper.updateById(monitor);
        String uuid=projectInfoService.selectById(monitor.getProjectId()).getUuid();
        try {
			zbusProducerHolder.modifyDevice(uuid, JSONUtil.toJsonStr(res.getBody()),"edit","monitor");
		} catch (Exception e) {
			e.printStackTrace();
		}
        EntityWrapper<ProjectEnvironmentMonitorVideo> ew = new EntityWrapper<ProjectEnvironmentMonitorVideo>();
        ew.eq("monitor_id", monitor.getId());
        videoMapper.delete(ew);

        Integer monitorId = monitor.getId();
        for (int i = 0; i < videos.size(); i++) {
            ProjectEnvironmentMonitorVideo video = videos.get(i);
            video.setMonitorId(monitorId);
            videoMapper.insert(video);
        }
        return new ResultDTO<>(true,null,"编辑成功");
    }


    /**
     * 启用
     *
     * @param res
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> updateStatus(RequestDTO<ProjectEnvironmentMonitor> res) {

        String message="";
        Boolean flag=true;
        for (int i = 0; i < res.getIds().size(); i++) {
            Integer id = Integer.valueOf(res.getIds().get(i).toString());
            ProjectEnvironmentMonitor monitor = new ProjectEnvironmentMonitor();
            if("stop".equals(res.getKey())){
                String deviceNum = baseMapper.selectById(id).getDeviceNo();
                String redisKey = Const.DEVICE_PLATFORM + "monitor:" + deviceNum;
                if(redisUtil.exists(redisKey)) {
                    String value = (String)redisUtil.get(redisKey);
                    String topic = value.split("#")[0];
                    String data = "{\"sn\":\"" + deviceNum + "\",\"type\":\"monitor\",\"cmd\":\"01\"}";
                    producerUtil.sendCtrlMessage(topic,data);
                }
                monitor.setStatus(0);
                monitor.setIsOnline(0);
                message="停用成功";
            }else{
                ProjectEnvironmentMonitor monitor1=baseMapper.selectById(id);
                String deviceNo=monitor1.getDeviceNo();
                List<ProjectEnvironmentMonitor> list=baseMapper.checkByDeviceNo(id,deviceNo);
                if(list.size()>0){
                    message="相同的设备编号不能同时启用";
                    flag=false;
                    break;
                }else{
                    monitor.setStatus(1);
                    message="启用成功";
                }

            }
            monitor.setId(id);
            baseMapper.updateById(monitor);
        }

        return new ResultDTO<>(flag, null, message);

    }
   /**
    * 智慧工地用
    */
	@Override
	public byte[] getEnvironmentInfo(RequestDTO<MonitorAlarmVO> res) {

		Page<MonitorAlarmVO> page = new Page<>(res.getPageNum(),res.getPageSize());
		HashMap<String, Object> map = new HashMap<>();
        map.put("tableName","t_project_environment_monitor_detail_" + res.getYearMonth());
        map.put("deviceNo", res.getDeviceNo());
        map.put("uuid", res.getUuid());

       System.out.println(res.getDeviceNo());
        List<MonitorAlarmVO> list =  baseMapper.getEnvironmentInfo(page,map);
        System.out.println("时间"+list);
        Map<String,Object> resultMap = new HashMap<>(10);
        resultMap.put("environmentList",list);
        resultMap.put("total",page.getTotal());
        ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);

	}
    /**
     * 智慧工地用
     */
	@Override
	public byte[] getAlarmInfo(RequestDTO<MonitorAlarmVO> res) {
		 Page<MonitorAlarmVO> page = new Page<>(res.getPageNum(),res.getPageSize());
		 HashMap<String, Object> map = new HashMap<>();
	        map.put("tableName","t_project_environment_monitor_alarm_" + res.getYearMonth());

	        String[] uuids = res.getUuids().toString().split(",");
	       map.put("alarmId", res.getAlarmId());
	       map.put("deviceNo", res.getDeviceNo());

	        map.put("uuids",uuids);
	        List<MonitorAlarmVO> alarmList = baseMapper.getAlarmInfo(page,map);
	        Map<String,Object> resultMap = new HashMap<>(10);
	        resultMap.put("total",page.getTotal());
	       
	        resultMap.put("alarmList",alarmList);
	        ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
	        String resultStr = JSONUtil.toJsonStr(resultDTO);
	        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
	        System.out.println(resultStr);
	        return StringCompress.compress(resultStr);
	}
	
	/**
	 * 智慧工地拉取报警明细信息
	 */
	@Override
    public byte[] getAlarmDetail(RequestDTO<MonitorAlarmVO> res)  {
		Page<MonitorAlarmVO> page = new Page<>(res.getPageNum(),res.getPageSize());
		HashMap<String, Object> map = new HashMap<>();
		 map.put("tableName","t_project_environment_monitor_alarm_" + res.getYearMonth());
		 map.put("alarmId", res.getAlarmId());
	     map.put("deviceNo", res.getDeviceNo());
        List<MonitorAlarmVO> monitors = baseMapper.getAlarmDetail(page,map);
        Map<String,Object> resultMap = new HashMap<>(10);
        resultMap.put("total",page.getTotal());
        resultMap.put("infoList",monitors);
        ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

    @Override
    public List<Map<String, Object>> getDeviceOnlineStatus(String uuid,int type) {
        List<Map<String, Object>> mapList  = new ArrayList<>();
        if(type == 1){
            Wrapper<ProjectEnvironmentMonitor> wrapper  =new EntityWrapper<>();
            wrapper.where("project_id = (select id from t_project_info where uuid = {0} and is_del = 0) ",uuid);
            List<ProjectEnvironmentMonitor> list =  baseMapper.selectList(wrapper);
            list.forEach(monitor->{
                Map<String, Object> map   = new HashMap<>(2);
                map.put("deviceNo", monitor.getDeviceNo());
                map.put("status",1==monitor.getIsOnline()?"在线":"离线");
                mapList.add(map);
            });
        }else if(type == 2){
            Wrapper<ProjectCrane> wrapper  =new EntityWrapper<>();
            wrapper.where("project_id = (select id from t_project_info where uuid = {0} and is_del = 0) ",uuid);
            List<ProjectCrane> list =  projectCraneMapper.selectList(wrapper);
            list.forEach(crane->{
                Map<String, Object> map   = new HashMap<>(2);
                map.put("deviceNo", crane.getDeviceNo());
                map.put("status",1==crane.getIsOnline()?"在线":"离线");
                mapList.add(map);
            });
        }else if (type == 3){
            Wrapper<ProjectLift> wrapper  =new EntityWrapper<>();
            wrapper.where("project_id = (select id from t_project_info where uuid = {0} and is_del = 0) ",uuid);
            List<ProjectLift> list =  projectLiftMapper.selectList(wrapper);
            list.forEach(lift->{
                Map<String, Object> map   = new HashMap<>(2);
                map.put("deviceNo", lift.getDeviceNo());
                map.put("status",1==lift.getIsOnline()?"在线":"离线");
                mapList.add(map);
            });
        }

        return mapList;
    }




    @Override
    public List<ProjectEnvironmentMonitor> selectByOrgId(List<Integer> orgIds) throws Exception {
	    Map<String,Object> map = new HashMap<>(10);
	    map.put("orgIds",orgIds);
        return baseMapper.selectByOrgId(map);
    }

    /**
	 * 重写删除，去除占用
	 */
	 @Transactional(rollbackFor = Exception.class)
	    @Override
	    public boolean deleteBatchIds(List<? extends Serializable> idList) {
		    for(int i=0;i<idList.size();i++){
	            Object id=idList.get(i);
	            String key = "device_platform:deviceinfo:monitor:" + baseMapper.selectById(idList.get(i)).getDeviceNo();
	            if(redisUtil.exists(key)) {
	                redisUtil.remove(key);
	            }
	            ProjectEnvironmentMonitor monitor = baseMapper.selectById(Integer.parseInt(id.toString()));
                RequestDTO<ProjectEnvironmentMonitor> requestDTO=new RequestDTO<>();
                requestDTO.setId(Integer.parseInt(id.toString()));
                selectInfo(requestDTO);
                String  uuid =projectInfoService.selectById(selectInfo(requestDTO).getData().getMonitor().getProjectId()).getUuid();
                try {
                    zbusProducerHolder.modifyDevice(uuid,JSONUtil.toJsonStr(selectInfo(requestDTO).getData()),"delete","monitor");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                baseMapper.minusCallTimes(monitor.getSpecification(), monitor.getManufactor());
		    }


	        return retBool(baseMapper.deleteBatchIds(idList));
	    }

	 /**
	  * 地图用接口
	  */
	@Override
	public ResultDTO<List<ProjectEnvironmentMonitor>> selectListMap(
			RequestDTO<ProjectEnvironmentMonitor> res) {
		 EntityWrapper<RequestDTO<ProjectEnvironmentMonitor>> ew = new EntityWrapper<>();
	        ew.eq("a.is_del", 0).in("a.org_id", res.getOrgIds());
	        if(res.getId()!=null && !"".equals(res.getId())) {
//	        	ew.like("b.name", res.getKey());
	        	 ew.eq("a.project_id", res.getId());
	        }
	        List<ProjectEnvironmentMonitor> list = baseMapper.selectListMap(ew);
	        return new ResultDTO<>(true,list);
	}

	@Override
    public Map<String,Object> getEnvironmentInfoForScreen(String deviceNo){
        ProjectEnvironmentMonitor pem = baseMapper.getEnvByDeviceNo(deviceNo);
        //String resultStr = JSONUtil.toJsonStr(pem);
        List<ProjectEnvironmentMonitor> list = new ArrayList<ProjectEnvironmentMonitor>();
        Map<String,Object> map = new HashMap<String,Object>();

        list.add(pem);
        map.put("environmentList",list);

        return map;
    }

	
}
