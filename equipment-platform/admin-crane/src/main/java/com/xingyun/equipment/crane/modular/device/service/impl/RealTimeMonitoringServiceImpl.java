package com.xingyun.equipment.crane.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.DateUtils;
import com.xingyun.equipment.cache.RedisUtil;
import com.xingyun.equipment.crane.core.util.StringCompress;
import com.xingyun.equipment.crane.core.vo.CurrentCraneDataVO;
import com.xingyun.equipment.crane.modular.device.dao.RealTimeMonitoringTowerMapper;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneCyclicWorkDuration;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneDetail;
import com.xingyun.equipment.crane.modular.device.service.IProjectCraneCyclicWorkDurationService;
import com.xingyun.equipment.crane.modular.device.service.ProjectCraneDetailService;
import com.xingyun.equipment.crane.modular.device.service.RealTimeMonitoringService;
import com.xingyun.equipment.crane.modular.device.vo.OfflineReasonPieVO;
import com.xingyun.equipment.crane.modular.device.vo.RealTimeMonitoringTowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.EscapedErrors;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.xingyun.equipment.core.enums.FlagEnum.CRANE_7_HOURS_DATA;

/***
 *@author:jixiaojun
 *DATE:2018/8/23
 *TIME:9:30
 */
@Service
public class RealTimeMonitoringServiceImpl extends ServiceImpl<RealTimeMonitoringTowerMapper,RealTimeMonitoringTowerVo> implements RealTimeMonitoringService {
    @Autowired
    private   RedisUtil redisUtil;
    @Autowired
    private ProjectCraneDetailService craneDetailService;
    @Autowired
    private IProjectCraneCyclicWorkDurationService craneCyclicWorkDurationService;


    @Override
    public List<RealTimeMonitoringTowerVo> selectCrane(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        // 1 违章 2报警 3提醒 4 在线 5离线
        Integer status = null;
        if(requestDTO.getBody().getAlarmStatus()!=null) {
            status=requestDTO.getBody().getAlarmStatus();
        }else{
            status = 0;
        }
        Integer isOnline = null;
        Integer alarmStatus = null;
        switch (status){
            case 1:
                alarmStatus = 3;
                break;
            case 2:
                alarmStatus = 2;
                break;
            case 3:
                alarmStatus = 1;
                break;
            case 4:
                isOnline = 1;
                break;
            case 5:
                isOnline = 0;
                break;
                default:
                break;
        }

        Map<String,Object> map = new HashMap(16);
        map.put("orgIds",requestDTO.getOrgIds());
        map.put("projectId",requestDTO.getProjectId());
        map.put("deviceNo",requestDTO.getBody().getDeviceNo());
        map.put("isOnline",isOnline);
        map.put("alarmStatus",alarmStatus);
        if(StrUtil.isNotBlank(requestDTO.getBody().getStatus())) {
            map.put("status",Integer.parseInt(requestDTO.getBody().getStatus()));
        }
        return baseMapper.selectCrane(page,map);
    }


    @Override
    public List<ProjectCraneDetail> selectCraneData(Page<ProjectCraneDetail> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Integer percentage = requestDTO.getBody().getPercentage();
        Date begin = DateUtil.beginOfDay(DateUtil.parse(requestDTO.getStartTime()));
        Date end = DateUtil.endOfDay(DateUtil.parse(requestDTO.getEndTime()));

        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("crane_id",requestDTO.getId());
        wrapper.between("create_time",begin,end);
        // 小于90
        wrapper.lt(percentage!=null&&percentage.intValue()==1,"moment_percentage",90);
        // 》=90 《110
        wrapper.ge(percentage!=null&&percentage.intValue()==2,"moment_percentage",90);
        wrapper.lt(percentage!=null&&percentage.intValue()==2,"moment_percentage",110);

        wrapper.ge(percentage!=null&&percentage.intValue()==3,"moment_percentage",110);
        wrapper.lt(percentage!=null&&percentage.intValue()==3,"moment_percentage",120);

        wrapper.ge(percentage!=null&&percentage.intValue()==4,"moment_percentage",120);
        wrapper.orderBy("create_time",false);
        craneDetailService.selectPage(page,wrapper);
       List<ProjectCraneDetail> details = page.getRecords();

        return details;
    }

    @Override
    public List<ProjectCraneCyclicWorkDuration> selectWeightData(Page<ProjectCraneCyclicWorkDuration> page, RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Integer percentage = requestDTO.getBody().getPercentage();
        Wrapper wrapper =new EntityWrapper();


        wrapper.eq("crane_id",requestDTO.getId());
        if(requestDTO.getStartTime()!=null && requestDTO.getEndTime()!=null){
            Date begin = DateUtil.beginOfDay(DateUtil.parse(requestDTO.getStartTime()));
            Date end = DateUtil.endOfDay(DateUtil.parse(requestDTO.getEndTime()));
            wrapper.between("create_time",begin,end);
        }

        // 小于90
        wrapper.lt(percentage!=null&&percentage.intValue()==1,"moment_percentage",90);
        // 》=90 《110
        wrapper.ge(percentage!=null&&percentage.intValue()==2,"moment_percentage",90);
        wrapper.lt(percentage!=null&&percentage.intValue()==2,"moment_percentage",110);

        wrapper.ge(percentage!=null&&percentage.intValue()==3,"moment_percentage",110);
        wrapper.lt(percentage!=null&&percentage.intValue()==3,"moment_percentage",120);

        wrapper.ge(percentage!=null&&percentage.intValue()==4,"moment_percentage",120);
        wrapper.orderBy("create_time",false);
        craneCyclicWorkDurationService.selectPage(page,wrapper);
        List<ProjectCraneCyclicWorkDuration> details = page.getRecords();

         return details;
    }


    @Override
    public List<RealTimeMonitoringTowerVo> selectPromptingAlarm(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap(10);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        map.put("id",requestDTO.getId());
        map.put("tableName","t_project_crane_alarm_" + sd.format(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-7).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-15).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-30).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end",sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName","t_project_crane_alarm_" + sd.format(requestDTO.getBody().getBeginDate()));
            map.put("tableName","t_project_crane_alarm_" + sd.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectPromptingAlarm(page,map);
    }

    @Override
    public List<RealTimeMonitoringTowerVo> selectWarningAlarm(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap(10);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        map.put("id",requestDTO.getId());
        map.put("tableName","t_project_crane_alarm_" + sd.format(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-7).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-15).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-30).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end",sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName","t_project_crane_alarm_" + sd.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectWarningAlarm(page,map);
    }

    @Override
    public List<RealTimeMonitoringTowerVo> selectViolationAlarm(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap(10);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        map.put("id",requestDTO.getId());
        map.put("tableName","t_project_crane_alarm_" + sd.format(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-7).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-15).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-30).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end",sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName","t_project_crane_alarm_" + sd.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectViolationAlarm(page,map);
    }

    @Override
    public List<RealTimeMonitoringTowerVo> selectAlarmData(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap(10);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        map.put("id",requestDTO.getId());
        map.put("tableName","t_project_crane_alarm_" + sd.format(date));
        map.put("key",requestDTO.getKey());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(new Integer(1).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-7).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(2).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-15).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(3).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",DateUtil.offsetDay(new Date(),-30).toString("yyyy-MM-dd"));
            map.put("end",sdf.format(new Date()));
        }else if(new Integer(4).equals(requestDTO.getBody().getCheckTime())) {
            map.put("begin",sdf.format(requestDTO.getBody().getBeginDate()));
            map.put("end",sdf.format(requestDTO.getBody().getEndDate()));
            map.put("tableName","t_project_crane_alarm_" + sd.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectAlarmData(page,map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertMessage(RealTimeMonitoringTowerVo realTimeMonitoringTowerVo) throws Exception {
        if(baseMapper.insertMessage(realTimeMonitoringTowerVo) > 0) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public byte[] getCraneInfo(Map<String, Object> map) throws Exception {
        Page<RealTimeMonitoringTowerVo> page = new Page<>(Integer.parseInt((String)map.get("pageIndex")),Integer.parseInt((String)map.get("pageSize")));
        map.put("tableName","t_project_crane_detail_" + map.get("yearMonth"));
        int pageIndex = Integer.parseInt((String)map.get("pageIndex")) - 1;
        int pageSize = Integer.parseInt((String)map.get("pageSize"));
        int offSet = pageIndex * pageSize;
        map.put("offSet",offSet);
        map.put("pageSize",pageSize);
        List<RealTimeMonitoringTowerVo> realTimeMonitoringTowerVos =  baseMapper.getCraneInfo(page,map);
        for(int i = 0;i < realTimeMonitoringTowerVos.size();i++) {
            if("1".equals(realTimeMonitoringTowerVos.get(i).getStatus())) {
                realTimeMonitoringTowerVos.get(i).setStatus("在线");
            }else if("0".equals(realTimeMonitoringTowerVos.get(i).getStatus())) {
                realTimeMonitoringTowerVos.get(i).setStatus("离线");
            }
        }
        Map<String,Object> resultMap = new HashMap<>(10);
        resultMap.put("craneList",realTimeMonitoringTowerVos);
        resultMap.put("total",page.getTotal());
        ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

    @Override
    public byte[] getAlarmInfo(Map<String, Object> map) throws Exception {
        Page<RealTimeMonitoringTowerVo> page = new Page<>(Integer.parseInt((String)map.get("pageIndex")),Integer.parseInt((String)map.get("pageSize")));
        map.put("tableName","t_project_crane_alarm_" + map.get("yearMonth"));
        int pageIndex = Integer.parseInt((String)map.get("pageIndex")) - 1;
        int pageSize = Integer.parseInt((String)map.get("pageSize"));
        int offSet = pageIndex * pageSize;
        String[] uuids = map.get("uuids").toString().split(",");
        map.put("offSet",offSet);
        map.put("pageSize",pageSize);
        map.put("uuids",uuids);
        List<RealTimeMonitoringTowerVo> realTimeMonitoringTowerVos = baseMapper.getAlarmInfo(page,map);
        Map<String,Object> resultMap = new HashMap<>(10);
        resultMap.put("total",page.getTotal());
        resultMap.put("alarmList",realTimeMonitoringTowerVos);
        ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

    @Override
    public byte[] getAlarmDetail(Map<String, Object> map) throws Exception {
        Page<RealTimeMonitoringTowerVo> page = new Page<>(Integer.parseInt((String)map.get("pageIndex")),Integer.parseInt((String)map.get("pageSize")));
        map.put("tableName","t_project_crane_alarm_" + map.get("yearMonth"));
        List<RealTimeMonitoringTowerVo> realTimeMonitoringTowerVos = baseMapper.getAlarmDetail(page,map);
        Map<String,Object> resultMap = new HashMap<>(10);
        resultMap.put("total",page.getTotal());
        resultMap.put("infoList",realTimeMonitoringTowerVos);
        ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
        String resultStr = JSONUtil.toJsonStr(resultDTO);
        return StringCompress.compress(resultStr);
    }

    @Override
    public List<RealTimeMonitoringTowerVo> selectMonitorStatus(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
            map.put("status",requestDTO.getStatus());
            map.put("craneId",requestDTO.getId());
            map.put("begin",requestDTO.getStartTime());
            map.put("end",requestDTO.getEndTime());
        return baseMapper.selectMonitorStatus(page,map);
    }
    @Override
   public List<OfflineReasonPieVO> selectOfflineReasonPie(RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception{
        List<OfflineReasonPieVO> list = new ArrayList<>(10);
        list.add(new OfflineReasonPieVO("断电离线",0));
        list.add(new OfflineReasonPieVO("正常离线",0));
        list.add(new OfflineReasonPieVO("网络异常",0));
        list.add(new OfflineReasonPieVO("服务器重启",0));
        Map<String,Object> map = new HashMap<>(10);
        map.put("craneId",requestDTO.getId());
        map.put("begin",requestDTO.getStartTime());
        map.put("end",requestDTO.getEndTime());
        List<OfflineReasonPieVO> voList = baseMapper.selectOfflineReasonPie(map);
        for(OfflineReasonPieVO vo : list){
            for(OfflineReasonPieVO dbvo :voList ){
                if(vo.getReason().equals(dbvo.getReason())){
                    vo.setAccount(dbvo.getAccount());
                }
            }
        }
        return list;
    }
    @Override
    public List<RealTimeMonitoringTowerVo> selectRunTime(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        if (requestDTO.getStartTime() != null) {
            Date begin = DateUtil.beginOfDay(DateUtil.parse(requestDTO.getStartTime()));
            map.put("begin",begin);
        }
        if (requestDTO.getEndTime() != null) {
            Date end = DateUtil.endOfDay(DateUtil.parse(requestDTO.getEndTime()));
            map.put("end",end);
        }

        map.put("craneId",requestDTO.getId());
        map.put("tableName","t_project_crane_detail");

        List<RealTimeMonitoringTowerVo> list = baseMapper.selectRunTime(page,map);
        for(int i = 0;i < list.size();i++) {

            Wrapper wrapper =new EntityWrapper();
            wrapper.eq("crane_id",requestDTO.getId());

            wrapper.between("create_time",list.get(i).getDeviceTimeBegin(),list.get(i).getDeviceTimeEnd()==null?new Date():list.get(i).getDeviceTimeEnd());

            List<ProjectCraneCyclicWorkDuration> craneCyclicWorkDurations =  craneCyclicWorkDurationService.selectList(wrapper);
            long continueTime = 0L;
            for(ProjectCraneCyclicWorkDuration cyclicWorkDuration:craneCyclicWorkDurations){
                continueTime+= (cyclicWorkDuration.getEndTime().getTime()-cyclicWorkDuration.getCreateTime().getTime());
            }

            long days = continueTime / (1000 * 60 * 60 * 24);
            long hours = (continueTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (continueTime % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (continueTime % (1000 * 60)) / 1000;
            String time =  days + "天 " + hours + "小时 " + minutes + "分钟 "+ seconds + "秒 ";
            list.get(i).setDurationActual(time);
        }
        return list;
    }

    @Override
    public CurrentCraneDataVO getMonitorData(String deviceNo, String uuid) {

        String key ="device_platform:current:"+uuid+":crane:"+deviceNo;
        if(!redisUtil.exists(key)){
            //不存在最近一条数据

            return null;

        }else{
            String json = (String) redisUtil.get(key);

            CurrentCraneDataVO  result = JSONUtil.toBean(json,CurrentCraneDataVO.class) ;

             Integer online= baseMapper.getDeviceOnline(deviceNo,uuid);
             if(online == null||online.equals(new Integer(0))){
                 result.setStatus("离线");
             }else{
                 result.setStatus("在线");
             }

            result.setFlag(CRANE_7_HOURS_DATA.getFlag());
            return result;
        }

    }
}
