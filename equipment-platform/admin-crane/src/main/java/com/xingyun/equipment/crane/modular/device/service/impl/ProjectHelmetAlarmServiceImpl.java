package com.xingyun.equipment.crane.modular.device.service.impl;

import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.DateUtils;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetAlarm;
import com.xingyun.equipment.crane.modular.device.dao.ProjectHelmetAlarmMapper;
import com.xingyun.equipment.crane.modular.device.service.IProjectHelmetAlarmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 人员健康异常报警信息 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@Service
public class ProjectHelmetAlarmServiceImpl extends ServiceImpl<ProjectHelmetAlarmMapper, ProjectHelmetAlarm> implements IProjectHelmetAlarmService{

    @Override
    public ResultDTO<DataDTO<List<ProjectHelmetAlarm>>> getAlarmMessage(RequestDTO request) {
        Integer helmetId=Integer.parseInt(request.getId().toString());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<ProjectHelmetAlarm> list=new ArrayList<>();
        if(!request.getBeginDate().isEmpty() && !request.getEndDate().isEmpty()){
            List<String> monthList= null;
            try {
                monthList = DateUtils.getMonthsBetween(sdf.parse(request.getBeginDate()),sdf.parse(request.getEndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for(String month:monthList){
                list.addAll(baseMapper.getAlarmMessage(month,helmetId,request.getBeginDate().substring(0,10)+" 00:00:00",request.getEndDate().substring(0,10)+" 00:00:00"));
            }
        }else{
            SimpleDateFormat sdfd=new SimpleDateFormat("yyyyMM");
            list=baseMapper.getAlarmMessage(sdfd.format(new Date()),helmetId,null,null);
        }
        List<Map<String,Object>> resultList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        Integer alarmSixAxis=0;
        Integer alarmTemperature=0;
        Integer alarmBloodOxygen=0;
        Integer alarmHeartRate=0;
        for (ProjectHelmetAlarm p:list){
            if(p.getAlarmType()==1){
                alarmSixAxis++;
            }else if (p.getAlarmType()==2){
                alarmHeartRate++;
            }else if (p.getAlarmType()==3){
                alarmBloodOxygen++;
            }else if (p.getAlarmType()==4){
                alarmTemperature++;
            }
        }
        map.put("helmetId",helmetId);
        map.put("alarmSixAxis",alarmSixAxis);
        map.put("alarmHeartRate",alarmHeartRate);
        map.put("alarmBloodOxygen",alarmBloodOxygen);
        map.put("alarmTemperature",alarmTemperature);
        map.put("alarmAll",alarmSixAxis+alarmTemperature+alarmBloodOxygen+alarmHeartRate);
        resultList.add(map);
        return new ResultDTO(true,DataDTO.factory(resultList,resultList.size()));
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectHelmetAlarm>>> getListByHelmetId(RequestDTO request){
        Integer helmetId=Integer.parseInt(request.getId().toString());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<ProjectHelmetAlarm> list=new ArrayList<>();
        if(!request.getBeginDate().isEmpty() && !request.getEndDate().isEmpty()){
            List<String> monthList= null;
            try {
                monthList = DateUtils.getMonthsBetween(sdf.parse(request.getBeginDate()),sdf.parse(request.getEndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for(String month:monthList){
                list.addAll(baseMapper.getAlarmMessage(month,helmetId,request.getBeginDate().substring(0,10)+" 00:00:00",request.getEndDate().substring(0,10)+" 00:00:00"));
            }
        }else{
            SimpleDateFormat sdfd=new SimpleDateFormat("yyyyMM");
            list=baseMapper.getAlarmMessage(sdfd.format(new Date()),helmetId,null,null);
        }
        List<ProjectHelmetAlarm> resultList=new ArrayList<>();
        Integer last=0;
        Integer pageIndex=request.getPageNum();
        Integer pageSize=request.getPageSize();
        if(pageSize*pageIndex>list.size()){
            last=list.size();
        }else{
            last=pageSize*pageIndex;
        }
        for(int i=(pageIndex-1)*pageSize;i<last;i++){
            resultList.add(list.get(i));
        }
        return new ResultDTO(true,DataDTO.factory(resultList,list.size()));
    }
}
