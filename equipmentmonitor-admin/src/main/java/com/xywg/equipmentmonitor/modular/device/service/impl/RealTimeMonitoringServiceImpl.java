package com.xywg.equipmentmonitor.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.core.util.StringCompress;
import com.xywg.equipmentmonitor.core.vo.CurrentCraneDataVO;
import com.xywg.equipmentmonitor.modular.device.dao.RealTimeMonitoringTowerMapper;
import com.xywg.equipmentmonitor.modular.device.service.RealTimeMonitoringService;
import com.xywg.equipmentmonitor.modular.device.vo.RealTimeMonitoringTowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.EscapedErrors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xywg.equipmentmonitor.core.common.constant.FlagEnum.CRANE_7_HOURS_DATA;

/***
 *@author:jixiaojun
 *DATE:2018/8/23
 *TIME:9:30
 */
@Service
public class RealTimeMonitoringServiceImpl extends ServiceImpl<RealTimeMonitoringTowerMapper,RealTimeMonitoringTowerVo> implements RealTimeMonitoringService {
    @Autowired
    private   RedisUtil redisUtil;

    @Override
    public List<RealTimeMonitoringTowerVo> selectCrane(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap(10);
        map.put("orgIds",requestDTO.getOrgIds());
        map.put("key",requestDTO.getKey());
        map.put("deviceNo","%"+requestDTO.getDeviceNo()+"%");
        if(!"".equals(requestDTO.getBody().getStatus())) {
            map.put("status",Integer.parseInt(requestDTO.getBody().getStatus()));
        }
        return baseMapper.selectCrane(page,map);
    }

    @Override
    public List<RealTimeMonitoringTowerVo> selectCraneData(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap(10);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        map.put("id",requestDTO.getId());
        map.put("key",requestDTO.getKey());
        map.put("tableName","t_project_crane_detail_" + sd.format(date));
        map.put("percentage",requestDTO.getBody().getPercentage());
        if(!"".equals(requestDTO.getBody().getStatus())) {
            map.put("status",Integer.parseInt(requestDTO.getBody().getStatus()));
        }
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
            map.put("tableName","t_project_crane_detail_" + sd.format(requestDTO.getBody().getBeginDate()));
        }
        return baseMapper.selectCraneData(page,map);
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
        String[] uuids = map.get("uuids").toString().split(",");
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
        map.put("craneId",requestDTO.getId());
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
        }
        return baseMapper.selectMonitorStatus(page,map);
    }

    @Override
    public List<RealTimeMonitoringTowerVo> selectRunTime(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
        map.put("craneId",requestDTO.getId());
        map.put("tableName","t_project_crane_detail_" + sd.format(date));
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
            map.put("tableName","t_project_crane_detail_" + sd.format(requestDTO.getBody().getBeginDate()));
        }
        List<RealTimeMonitoringTowerVo> list = baseMapper.selectRunTime(page,map);
        for(int i = 0;i < list.size();i++) {
            map.put("beginDate",list.get(i).getDeviceTimeBegin());
            map.put("endDate",list.get(i).getDeviceTimeEnd()==null?new Date():list.get(i).getDeviceTimeEnd());
            List<RealTimeMonitoringTowerVo> realTimeMonitoringTowerVos = baseMapper.getRunTimeReal(map);
            long continueTime = 0L;
            long beginTime = 0L;
            boolean isEnd = false;
            for(int j = 0;j < realTimeMonitoringTowerVos.size();j++) {
                if(isEnd){
                    //找等于0
                    if(realTimeMonitoringTowerVos.get(j).getWeight() == 0) {
                        continueTime = continueTime + (realTimeMonitoringTowerVos.get(j).getDeviceTime().getTime() - beginTime);
                        isEnd = false;
                    }
                }else{
                    // 找大于0
                    if(realTimeMonitoringTowerVos.get(j).getWeight() > 0) {
                        beginTime = realTimeMonitoringTowerVos.get(j).getDeviceTime().getTime();
                        isEnd = true;
                    }
                }
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
