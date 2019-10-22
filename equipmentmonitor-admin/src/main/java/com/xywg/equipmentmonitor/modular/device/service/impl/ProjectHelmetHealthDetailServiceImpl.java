package com.xywg.equipmentmonitor.modular.device.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI3DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI4DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthInfo;
import com.xywg.equipmentmonitor.modular.device.dto.HealthLocation;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHealthDetail;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectHelmetHealthDetailMapper;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetHealthDetailService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 健康信息(采集数据) 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@Service
public class ProjectHelmetHealthDetailServiceImpl extends ServiceImpl<ProjectHelmetHealthDetailMapper, ProjectHelmetHealthDetail> implements IProjectHelmetHealthDetailService {

    @Override
    public List<HealthLocation> selectLocations( int helmetId) {

        Date now = new Date();
        String month = DateUtil.format(now,"yyyyMM");
        String daily = DateUtil.format(now,"yyyy-MM-dd");
        List<HealthLocation>  locations= baseMapper.selectLocations(month,daily,helmetId);
        return locations;
    }


    @Override
    public List<HealthLocation> selectLocationByTime( int helmetId,String startTime,String endTime) {

        Date now = new Date();
        String month = "";
        String daily = "";
        if(StrUtil.isEmpty(startTime)){
            startTime =null;
            endTime =null;
            month = DateUtil.format(now,"yyyyMM");
            daily = DateUtil.format(now,"yyyy-MM-dd");
        }else{
            month =startTime.substring(0,7).replaceAll("-","");

        }
        List<HealthLocation>  locations = new ArrayList<>();
        try{
              locations= baseMapper.selectLocationByTime(month,daily,helmetId,startTime,endTime);
        }catch(Exception e){

            return locations;
        }

        return locations;
    }

    @Override
    public HealthInfo selectLastInfo(int helmetId) {
        Date now = new Date();
        DateTime time30MinAgo = DateUtil.offsetMinute(now,-30);
        String month = DateUtil.format(now,"yyyyMM");
        String daily = DateUtil.format(now,"yyyy-MM-dd");
        String time = time30MinAgo.toString("yyyy-MM-dd HH:mm:ss");
        return baseMapper.selectLastInfo(month,daily,time,helmetId);
    }

    @Override
    public List<ProjectHelmetHealthDetail> selectDetails(int helmetId) {
        Date now = new Date();
        String month = DateUtil.format(now,"yyyyMM");
        String daily = DateUtil.format(now,"yyyy-MM-dd");
        return baseMapper.selectdetails(month,daily,helmetId);
    }

    @Override
    public List<HealthAPI3DTO> selectAlarms(List<Integer> helmetIds) {
        Date now = new Date();
        String month = DateUtil.format(now,"yyyyMM");
        String daily = DateUtil.format(now,"yyyy-MM-dd");
        String time =  DateUtil.offsetMinute(now,-30).toString("yyyy-MM-dd HH:mm:ss");
        return baseMapper.selectAlarms(month,daily,time,helmetIds);
    }

    /**
     * 微信端用接口
     * @param helmetIds
     * @return
     */
    @Override
    public List<HealthAPI3DTO> selectAlarmsToWechat(List<Integer> helmetIds,String startTime,String endTime) {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        if(StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            try {
                startTime = DateUtils.get7DaysBefore(now).get(0);

                endTime = DateUtils.formatDate(now,"yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String month = DateUtil.format(DateUtils.parseDate(endTime),"yyyyMM");


        return baseMapper.selectAlarmsToWechat(month,startTime,endTime,helmetIds);
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectHelmetHealthDetail>>> getDetailsById(RequestDTO requestDTO) throws Exception{
        List<ProjectHelmetHealthDetail> list=new ArrayList<>();
        Integer pageSize=requestDTO.getPageSize();
        Integer pageIndex=requestDTO.getPageNum();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        List<String> monthList=new ArrayList<>();
        if(requestDTO.getBeginDate().isEmpty()  && requestDTO.getEndDate().isEmpty()){
            monthList=DateUtils.getMonthsBetween(new Date(),new Date());
        }else{
            monthList=DateUtils.getMonthsBetween(sdf.parse(requestDTO.getBeginDate()),sdf.parse(requestDTO.getEndDate()));
        }
        Integer id=Integer .parseInt(requestDTO.getId().toString());
        String beginDate="";
        String endDate="";
        if(!requestDTO.getBeginDate().isEmpty() && !requestDTO.getEndDate().isEmpty()){
            beginDate=requestDTO.getBeginDate().substring(0,10);
            endDate=requestDTO.getEndDate().substring(0,10);
        }
        list=baseMapper.getDetailsById(id,beginDate,endDate,monthList);
        List<ProjectHelmetHealthDetail> resultList=new ArrayList<>();
        Integer last=0;
        if(pageSize*pageIndex>list.size()){
            last=list.size();
        }else{
            last=pageSize*pageIndex;
        }
        for(int i=(pageIndex-1)*pageSize;i<last;i++){
            resultList.add(list.get(i));
        }
        return new ResultDTO<>(true,DataDTO.factory(resultList,list.size()));
    }

    @Override
    public List<HealthAPI4DTO> userlocation(String uuid) {
        Date now = new Date();
        String month = DateUtil.format(now,"yyyyMM");
        String time =  DateUtil.offsetMinute(now,-30).toString("yyyy-MM-dd HH:mm:ss");
        return baseMapper.userlocations(month,time,uuid);
    }


}
