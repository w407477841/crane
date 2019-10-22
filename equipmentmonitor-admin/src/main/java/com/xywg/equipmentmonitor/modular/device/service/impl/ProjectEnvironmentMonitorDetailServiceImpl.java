package com.xywg.equipmentmonitor.modular.device.service.impl;

import static com.xywg.equipmentmonitor.core.common.constant.FlagEnum.MONITOR_7_HOURS_DATA;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;
import com.xywg.equipmentmonitor.modular.station.vo.MonitorALarmVO;
import com.xywg.equipmentmonitor.modular.station.vo.MonitorInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.core.vo.CurrentMonitorDataVO;
import com.xywg.equipmentmonitor.core.vo.TrendItemVO;
import com.xywg.equipmentmonitor.core.vo.WindDirectionTrendItemVO;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectEnvironmentMonitorDetailMapper;
import com.xywg.equipmentmonitor.modular.device.dto.OnlineDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorDetailService;
import com.xywg.equipmentmonitor.modular.device.vo.DeviceRankVO;
import com.xywg.equipmentmonitor.modular.device.vo.EnvironmentDeviceVO;
import com.xywg.equipmentmonitor.modular.device.vo.TrendVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
public class ProjectEnvironmentMonitorDetailServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorDetailMapper, ProjectEnvironmentMonitorDetail> implements ProjectEnvironmentMonitorDetailService {
    @Autowired
    private RedisUtil   redisUtil;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ProjectEnvironmentMonitorService projectEnvironmentMonitorService;

    @Override
    public ResultDTO<DataDTO<List<ProjectEnvironmentMonitorDetail>>> selectPageList(RequestDTO<ProjectEnvironmentMonitorDetail> res) {
        String initialDate = "2018-08-01 00:00:00";
        ProjectEnvironmentMonitorDetail detail = res.getBody();
        String beginTime = detail.getBeginTime();
        String endTime = detail.getEndTime();
        if (StringUtils.isBlank(beginTime) && StringUtils.isBlank(endTime)) {
            //默认30天的数据
            beginTime= DateUtils.formatDateTime(DateUtils.addDays(new Date(),-30));
            detail.setBeginTime(DateUtils.formatDateTime(DateUtils.addDays(new Date(),-30)));
        }
        if (StringUtils.isBlank(beginTime) || DateUtils.parseDate(beginTime).getTime() < DateUtils.parseDate(initialDate).getTime()) {
            detail.setBeginTime(initialDate);
        }
        if (StringUtils.isBlank(endTime) || DateUtils.parseDate(endTime).getTime() > System.currentTimeMillis()) {
            detail.setEndTime(DateUtils.formatDateTime(new Date()));
        } else if (DateUtils.parseDate(endTime).getTime() < DateUtils.parseDate(initialDate).getTime()) {
            detail.setEndTime(initialDate);
        }
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(
                DateUtils.parseDate(detail.getBeginTime()), DateUtils.parseDate(detail.getEndTime())
        );
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", detail.getBeginTime());
        map.put("endTime", detail.getEndTime());
        map.put("monitorId", detail.getMonitorId());
        map.put("status", detail.getStatus());
        Page<ProjectEnvironmentMonitorDetail> page = new Page<>(res.getPageNum(), res.getPageSize());
        List<ProjectEnvironmentMonitorDetail> list = baseMapper.selectPageList(page, map);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    @Override
    public List<TrendItemVO> trend(String uuid, String deviceNo, String colunmName) {
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -7);
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("uuid", uuid);
        map.put("deviceNo", deviceNo);
        map.put("columnName", colunmName);
        List<TrendItemVO> list = baseMapper.trend(map);
        return list;
    }

    @Override
    public List<TrendItemVO> trend(String uuid, String deviceNo, String columnName, String startTime, String endTime) {

        //两个时间段内的月份
        Date start = DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss");
        Date end = DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss");


        List<String> months = DateUtils.getMonthsBetween(start,end);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", start);
        map.put("endTime", end);
        map.put("uuid", uuid);
        map.put("deviceNo", deviceNo);
        map.put("columnName", columnName);
        List<TrendItemVO> list = baseMapper.trend(map);
        return list;
    }

    @Override
    public List<TrendVO> getTrend(int projectId,String deviceNo,String columnName){
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -7);
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("projectId", projectId);
        map.put("deviceNo", deviceNo);
        map.put("columnName", columnName);
        List<TrendVO> list=baseMapper.getTrend(map);
        return list;
    }

    @Override
    public List<DeviceRankVO> getRank20(Map<String,Object> param){
        String months=DateUtils.formatDate(new Date(),"yyyyMM");
       // int orgId= Integer.parseInt((String) param.get("orgId"));

        String columnName= (String) param.get("columnName");
        List<DeviceRankVO> list=baseMapper.getRank20(Const.orgIds.get(),months,columnName);
        return list;
    }

    @Override
    public List<WindDirectionTrendItemVO> windDirectionTrend(String uuid, String deviceNo, String columnName, String startTime, String endTime) {
        //两个时间段内的月份
        //两个时间段内的月份
        Date start = DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss");
        Date end = DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss");
        List<String> months = DateUtils.getMonthsBetween(start, end);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", start);
        map.put("endTime", end);
        map.put("uuid", uuid);
        map.put("deviceNo", deviceNo);
        List<WindDirectionTrendItemVO> list = baseMapper.windDirectionTrend(map);


        return list;
    }

    @Override
    public List<WindDirectionTrendItemVO> windDirectionTrend(String uuid, String deviceNo) {
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -7);
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("uuid", uuid);
        map.put("deviceNo", deviceNo);
        List<WindDirectionTrendItemVO> list = baseMapper.windDirectionTrend(map);


        return list;
    }


    @Override
    public CurrentMonitorDataVO getMonitorData(String uuid, String deviceNo) {

        String key ="device_platform:current:"+uuid+":monitor:"+deviceNo;
        if(!redisUtil.exists(key)){
            //不存在最近一条数据

            return null;

        }else{
            String json = (String) redisUtil.get(key);

            CurrentMonitorDataVO result = JSONUtil.toBean(json,CurrentMonitorDataVO.class) ;
            Integer online= baseMapper.getDeviceOnline(deviceNo,uuid);
            if(online == null||online.equals(new Integer(0))){
                result.setStatus("离线");
            }else{
                result.setStatus("在线");
            }
            result.setFlag(MONITOR_7_HOURS_DATA.getFlag());
            return result;
        }

    }
    @Override
    public CurrentMonitorDataVO getMonitorData2(int projectId, String deviceNo) {

        Wrapper<ProjectInfo> wrapper=new EntityWrapper<>();
        wrapper.eq("project_id",projectId);
        String uuid="";
        ProjectInfo projectInfo=projectInfoService.selectOne(wrapper);
        if(projectInfo!=null)
        {
            uuid=projectInfo.getUuid();
        }
        String key ="device_platform:current:"+uuid+":monitor:"+deviceNo;
        if(!redisUtil.exists(key)){
            //不存在最近一条数据

            return null;

        }else{
            String json = (String) redisUtil.get(key);

            CurrentMonitorDataVO result = JSONUtil.toBean(json,CurrentMonitorDataVO.class) ;
            Integer online= baseMapper.getDeviceOnline(deviceNo,uuid);
            if(online == null||online.equals(new Integer(0))){
                result.setStatus("离线");
            }else{
                result.setStatus("在线");
            }
            result.setFlag(MONITOR_7_HOURS_DATA.getFlag());
            return result;
        }

    }
    @Override
    public EnvironmentDeviceVO getLastOne(Map<String,Object> param){
        ProjectInfo p=projectInfoService.selectById((Serializable) param.get("projectId"));
        String uuid=p.getUuid();
        String deviceNo= (String) param.get("deviceNo");
        String key="device_platform:current:"+uuid+":monitor:"+deviceNo;
        if(!redisUtil.exists(key)){
            return null;
        }else{
            String json =(String)redisUtil.get(key);

            EnvironmentDeviceVO result=JSONUtil.toBean(json,EnvironmentDeviceVO.class);

            return  result;
        }

    }

    @Override
    public List<WindDirectionTrendItemVO> windDirectionTrendForScreen(String projectId, String deviceNo) {
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -7);
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("projectId", projectId);
        map.put("deviceNo", deviceNo);
        List<WindDirectionTrendItemVO> list = baseMapper.windDirectionTrend(map);


        return list;
    }
    @Override
    public List<WindDirectionTrendItemVO> mywindDirectionTrendForScreen(String projectId, String deviceNo) {
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -7);
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("projectId", projectId);
        map.put("deviceNo", deviceNo);
        List<WindDirectionTrendItemVO> list = baseMapper.windDirectionTrend2(map);


        return list;
    }

    @Override
    public List<TrendItemVO> trendForScreen(String projectId, String deviceNo, String colunmName) {
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -7);
        //两个时间段内的月份
        List<String> months = DateUtils.getMonthsBetween(startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("projectId", projectId);
        map.put("deviceNo", deviceNo);
        map.put("columnName", colunmName);
        List<TrendItemVO> list = baseMapper.trendForScreen(map);
        return list;
    }

    @Override
    public List<OnlineDTO> getOfflines(String[] uuids, String type, Integer online) {
        String deviceTableName;
        String heartbeatTableName;
        String heartbeatColumnName;
        if("monitor".equals(type)){
            deviceTableName = "t_project_environment_monitor";
            heartbeatTableName = "t_project_environment_heartbeat";
            heartbeatColumnName = "monitor_id";
        }else if("crane".equals(type)){
            deviceTableName = "t_project_crane";
            heartbeatTableName = "t_project_crane_heartbeat";
            heartbeatColumnName = "crane_id";
        }else if("lift".equals(type)){
            deviceTableName = "t_project_lift";
            heartbeatTableName = "t_project_lift_heartbeat";
            heartbeatColumnName = "lift_id";
        }else{
            return null;
        }
        if(uuids==null|| uuids.length==0){
            return null;
        }
        return  baseMapper.getOffline(uuids,deviceTableName,heartbeatTableName,heartbeatColumnName,online);
    }

    @Override
    public ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate){
//        List<Map<String,Object>> list=new ArrayList<>();
//        Date d=new Date();
//        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//        if(type==1){
//            beginDate=DateUtils.getBeginDate(1);
//            endDate=sdf.format(d);
//        }else if(type==2){
//            beginDate=DateUtils.getBeginDate(24);
//            endDate=sdf.format(d);
//        }else if(type==3){
//            beginDate=DateUtils.getBeginDate(24*7);
//            endDate=sdf.format(d);
//        }
//        String month=beginDate.replace("-","").substring(0,6);
//        list=baseMapper.changeToChart(id,columnName,beginDate,endDate,month);
        List<Map<String,Object>> list=new ArrayList<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        String tableName=null;
        String tableName1=null;
//        try {
//            List<String> timeList=DateUtils.getZhengShiFen();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Calendar now = Calendar.getInstance();
        Integer year= now.get(Calendar.YEAR);
        Integer month= now.get(Calendar.MONTH) + 1;
        Integer day = now.get(Calendar.DAY_OF_MONTH);
        Integer hour = now.get(Calendar.HOUR_OF_DAY);
        Integer minute = now.get(Calendar.MINUTE);
        String yearMonth="";
        if(month<10) {
        	yearMonth=year+"0"+month;
        }else {
        	yearMonth=year+""+month;
        }
         
        tableName ="t_project_environment_monitor_detail_"+yearMonth;
        System.out.println(yearMonth);
        String yearMonth1=null;
        String month1 =null;
        if(type==1){
        	if(((day-1)*24+hour) <1) {
        		
        			if(month==1) {
        				month1=Integer.toString(12);
        				year=year-1;
        				yearMonth1=year+""+month1;
        			}else {
        				month1=Integer.toString(month-1);
        				if(month1.length()<2) {
        					yearMonth1=year+"0"+month1;
        				}else {
        					yearMonth1=year+""+month1;
        				}
        				
        			}
        		
        	}
        	List<String> fenList = new ArrayList<>();
        	try {
				fenList=DateUtils.getZhengShiFen();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
        	
        	
        	 if(yearMonth1!=null) {
        		tableName1="t_project_environment_monitor_detail_"+yearMonth1;
        	 }
        	
            list=baseMapper.changeToChart(id,columnName,tableName,tableName1,10,60);
            for(String str:fenList){

            	Integer flag=0;
            for(Map<String, Object> map:list) {
            	if(map.get("time").equals(str)) {
            		flag=1;
            		dataList.add(map);
            	}
            	
            }
            if(flag==0) {
            	 Map<String, Object> map1= new HashMap<>();
                 map1.put("time", str);
                 map1.put("name", "");
                 dataList.add(map1);
            }
           
            }
            Collections.reverse(dataList);
        }
        if(type==2){
        	if(((day-2)*24+hour) <0) {
        		
        			if(month==1) {
        				month1=Integer.toString(12);
        				year=year-1;
        				yearMonth1=year+""+month1;
        			}else {
        				month1=Integer.toString(month-1);
        				if(month1.length()<2) {
        					yearMonth1=year+"0"+month1;
        				}else {
        					yearMonth1=year+""+month1;
        				}
        				
        		}
        	}
        	
           	
           	 if(yearMonth1!=null) {
           		tableName1="t_project_environment_monitor_detail_"+yearMonth1;
           	 }
           	
            list=baseMapper.changeToChart(id,columnName,tableName,tableName1,60,60*24);
          
            List<String> timeList=DateUtils.getZhengDian();
            for(String str:timeList){

            	Integer flag=0;
            for(Map<String, Object> map:list) {
            	if(map.get("time").equals(str)) {
            		flag=1;
            		dataList.add(map);
            	}
            	
            }
            if(flag==0) {
            	 Map<String, Object> map1= new HashMap<>();
                 map1.put("time", str);
                 map1.put("name", "");
                 dataList.add(map1);
            }
           
            }
            Collections.reverse(dataList);
        }
        if(type==3){
        	if(((day-8)*24+hour) <0) {
        		
        			if(month==1) {
        				month1=Integer.toString(12);
        				year=year-1;
        			}else {
        				month1=Integer.toString(month-1);
        			}
        			if(month1.length()<2) {
    					yearMonth1=year+"0"+month1;
    				}else {
    					yearMonth1=year+""+month1;
    				}
        			
        	}
        	
              	 
              	 if(yearMonth1!=null) {
              		tableName1="t_project_environment_monitor_detail_"+yearMonth1;
              	 }
              	
        	list=baseMapper.changeToChart(id,columnName,tableName,tableName1,60,60*24*7);
            Date d=new Date();
            List<String> timeList=new ArrayList<>();
            List<String> allTimes = new ArrayList<>();
            try {
                timeList=DateUtils.get7DaysBefore(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(String str:timeList){
            	List<String> list1 = new ArrayList<>();
            	try {
            		list1 = DateUtils.getZhengDianByDate(str);
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
            	allTimes.addAll(list1);

            }
           
            System.out.println(allTimes);
            for(String time:allTimes) {
            	Integer flag=0;
             for(Map<String, Object> data:list) {
            	 
            	if(data.get("time").equals(time)) {
            		flag=1;
            		dataList.add(data);
            	}
             }
             if(flag ==0) {
            	 Map<String, Object> map = new HashMap<>();
            	 map.put("time", time);
            	 map.put("name", "");
            	 dataList.add(map);
             }
            }
          
        }
        if(type==4){
        	List<String> timeList=new ArrayList<>();
        	List<String> allTimes = new ArrayList<>();
        	if(beginDate.length()>0 && endDate.length()>0) {
        		String bYear=beginDate.substring(0,4);
            	String bMonth =beginDate.substring(5,7);
            	String bDay = beginDate.substring(8,10);
            	String eYear=endDate.substring(0, 4);
            	String eMonth=endDate.substring(5,7);
            	String eDay = endDate.substring(8,10);
            	
            	if(Integer.valueOf(bYear+bMonth).equals(Integer.valueOf(eYear+eMonth))) {
            		tableName1="t_project_environment_monitor_detail_"+eYear+eMonth;
            		if(Integer.valueOf(eDay)-Integer.valueOf(bDay)==0) {
            			try {
    						timeList= DateUtils.getZhengDianByDate(beginDate);
    					} catch (ParseException e) {
    						
    						e.printStackTrace();
    					}
            			list=baseMapper.changeToChartAuto(id,columnName,tableName1,null,beginDate,endDate,60);
            			 for(String str:timeList){

            	            	Integer flag=0;
            	            for(Map<String, Object> map:list) {
            	            	if(map.get("time").equals(str)) {
            	            		flag=1;
            	            		dataList.add(map);
            	            	}
            	            	
            	            }
            	            if(flag==0) {
            	            	 Map<String, Object> map1= new HashMap<>();
            	                 map1.put("time", str);
            	                 map1.put("name", "");
            	                 dataList.add(map1);
            	            }
            	           
            	            }
            	            
            		}else {
            			tableName1="t_project_environment_monitor_detail_"+eYear+eMonth;
            			SimpleDateFormat ssd = new SimpleDateFormat("yyyy-MM-dd");
//            			SimpleDateFormat ssd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            			Date autoBeginDate =null;
            			Date autoEndDate =null;
            			Double count=0.0;
            			
            			try {
    						 autoBeginDate=ssd.parse(bYear+"-"+bMonth+"-"+bDay);
    						 autoEndDate=ssd.parse(eYear+"-"+eMonth+"-"+eDay);
    						 count= DateUtils.getDistanceOfTwoDate(autoBeginDate,autoEndDate);
    						 Calendar cal = Calendar.getInstance();
    							cal.setTime(autoBeginDate);
    							timeList.add(bYear+"-"+bMonth+"-"+bDay);
    							int days=new Double(count).intValue();
    							System.out.println(days);
    							list=baseMapper.changeToChartAuto(id,columnName,tableName1,null,beginDate,endDate,60);
    							for(int i=1;i<days+1;i++) {
    								cal.add(Calendar.DATE, 1);
    								timeList.add(ssd.format(cal.getTime()));
    							}
    							for(String str:timeList){
    				            	List<String> list1 = new ArrayList<>();
    				            	try {
    				            		list1 = DateUtils.getZhengDianByDate(str);
    								} catch (ParseException e) {
    									
    									e.printStackTrace();
    								}
    				            	allTimes.addAll(list1);

    				            }
    				           
    				            System.out.println(allTimes);
    				            for(String time:allTimes) {
    				            	Integer flag=0;
    				             for(Map<String, Object> data:list) {
    				            	 
    				            	if(data.get("time").equals(time)) {
    				            		flag=1;
    				            		dataList.add(data);
    				            	}
    				             }
    				             if(flag ==0) {
    				            	 Map<String, Object> map = new HashMap<>();
    				            	 map.put("time", time);
    				            	 map.put("name", "");
    				            	 dataList.add(map);
    				             }
    				            }
    				           
    					} catch (ParseException e) {
    						
    						e.printStackTrace();
    					}
            		}
            	}
        	}
        	
//        	if(bYear+bMonth ==eYear+eMonth) {
//        		dataList=baseMapper.changeToChartAuto(id,columnName,eYear+eMonth,null,beginDate,endDate,10);
//        	}else {
//        		dataList=baseMapper.changeToChartAuto(id,columnName,eYear+eMonth,bYear+bMonth,beginDate,endDate,10);
//        	}
        }
        return new ResultDTO(true,DataDTO.factory(dataList,dataList.size()));
    }


    private String getBeginDate(Integer hour){
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date d=new Date();
        //开始时间
        Long time=d.getTime()-(1000*3600*hour);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,0);
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        Long deadLine=c.getTimeInMillis();
        if(time<deadLine){
            return sdf.format(deadLine);
        }else{
            return sdf.format(time);
        }
    }

	@Override
	public ResultDTO<Map<String, Object>> getMonitorInfo(String uuid, String deviceNo) {
		Map<String, Object> map = new HashMap<>();

		Date date = new Date();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMM");
		String yearMonth=simpleDateFormat.format(date);
//		map.put("tableName", "t_project_environment_monitor_detail_"+yearMonth);
//		map.put("uuid", uuid);
//		map.put("deviceNo", deviceNo);
		Integer year =Integer.valueOf(yearMonth.substring(0, 4));
		Integer month = Integer.valueOf(yearMonth.substring(4,6));
		SimpleDateFormat sdf2=new SimpleDateFormat("dd");
		Integer day = Integer.valueOf(sdf2.format(date));
		map.put("tableName", "t_project_environment_monitor_detail_"+yearMonth);
		map.put("uuid", uuid);
		map.put("deviceNo", deviceNo);
		if(day==1) {
			if(month==1) {
				year =year-1;
				month=12;
				map.put("tableName1", "t_project_environment_monitor_detail_"+year+month);
			}else {
				month=month-1;
				map.put("tableName1", "t_project_environment_monitor_detail_"+year+month);
			}
			
		}
		
		
		List<Map<String, Object>> mapList= baseMapper.getMonitorInfo(map);
		
		Map<String, Object> result = new HashMap<>();
		result.put("list", mapList);
		return new  ResultDTO<>(true,result);
	}

    @Override
    public ResultDTO<Object> getMonitorInfo2zhgd(String uuid, String deviceNo,Integer alarmId, String time) {
        String type ;
        String unit ;
        if(StrUtil.isBlank(uuid)){
            return new ResultDTO<>(false,null,"缺少参数 uuid");
        }
        if(StrUtil.isBlank(deviceNo)){
            return new ResultDTO<>(false,null,"缺少参数 deviceNo");
        }
        if(alarmId==null){
            return new ResultDTO<>(false,null,"缺少参数 alarmId");
        }
        if(StrUtil.isBlank(time)){
            return new ResultDTO<>(false,null,"缺少参数 time");
        }

        Wrapper<ProjectInfo> wrapperInfo  = new EntityWrapper<>();
        wrapperInfo.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(wrapperInfo);
        if(null==projectInfo){
            return new ResultDTO<>(false,null,"项目不存在");
        }
        String curr = DateUtil.format(DateUtil.parse(time,"yyyy-MM-dd HH:mm"),"yyyy-MM-dd HH:mm:ss");
        String start = DateUtil.format(DateUtil.offsetHour(DateUtil.parse(time,"yyyy-MM-dd HH:mm"),-1),"yyyy-MM-dd HH:mm:ss");
        String end = DateUtil.format(DateUtil.offsetHour(DateUtil.parse(time,"yyyy-MM-dd HH:mm"),1),"yyyy-MM-dd HH:mm:ss");
        List<String> month  = new LinkedList<>();
        if(!curr.split(" ")[0].equals(end.split(" ")[0])){
            //  截止时间不会到下面一天,将 截止时间设成当天23:59:59
            end =   curr.split(" ")[0]+" 23:59:59" ;
        }
        if(!curr.substring(0,7).equals(start.substring(0,7))){
            // 跨月了
            month .add(start.substring(0,7).replace("-",""));
        }
        month .add(curr.substring(0,7).replace("-",""));

        Wrapper<ProjectEnvironmentMonitor> projectEnvironmentMonitorWrapper = new EntityWrapper<>();
        projectEnvironmentMonitorWrapper.eq("project_id",projectInfo.getId());
        projectEnvironmentMonitorWrapper.eq("device_no",deviceNo);
        ProjectEnvironmentMonitor projectEnvironmentMonitor = projectEnvironmentMonitorService.selectOne(projectEnvironmentMonitorWrapper);
        if(projectEnvironmentMonitorWrapper==null){
            return new ResultDTO<>(false,null,"设备不存在/设备不在该项目下");
        }
        switch (alarmId){
            case 1:
            case 2:
                type = "pm25" ;
                unit = "ug/m3";
                break;
            case 3:
            case 4:
                type = "pm10" ;
                unit = "ug/m3";
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                type = "temperature" ;
                unit = "℃";
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                type = "humidity";
                unit = "%";
                break;
            case 13:
            case 14:
                type = "noise";
                unit = "分贝";
                break;
            case 15:
            case 16:
                type = "wind_speed";
                unit = "m/s" ;
                break;
            case 19:
            case 20:
                type = "wind_force";
                unit = "级" ;
                break;
            default:
                return new ResultDTO<>(false,null,"alarmId 有误");
        }
       // 查询  -1小时 ~ + 1小时 的数据
       List<MonitorInfoVO> monitorInfoVOList = baseMapper.getMonitorInfo2zhgd(deviceNo,unit,alarmId,projectEnvironmentMonitor.getId(),type,start,end,month);


        return new ResultDTO<>(true,integralPoint(start,monitorInfoVOList),"成功");
    }
    /**
     过滤下
     */
    private List<MonitorInfoVO>  integralPoint( String start,List<MonitorInfoVO> monitorInfoVOList){
        List<MonitorInfoVO> results= new LinkedList<>();
        Map<String,MonitorInfoVO> map = new TreeMap<>();
        Date dateStart = DateUtil.parse(start.substring(0,15)+"0:00","yyyy-MM-dd HH:mm:ss");

        long step  = 1000*60*5;
        MonitorInfoVO nullInfo = monitorInfoVOList.get(0);
;        for(int i=0;i<24;i++,dateStart = new Date(dateStart.getTime()+step)){
            boolean has =false;
            Date newDate = dateStart;
            for(MonitorInfoVO monitorInfoVO:monitorInfoVOList){
                if(Math.abs(dateStart.getTime() - monitorInfoVO.getTime().getTime())<1000*30*5){
                    MonitorInfoVO temp = monitorInfoVO;
                    temp.setTime(newDate);
                    map.put(DateUtil.format(dateStart,"yyyy-MM-dd HH:mm:ss"),temp);
                    has = true;
                }
            }
            if(!has){

                MonitorInfoVO temp = new MonitorInfoVO();
                BeanUtils.copyProperties(nullInfo,temp);
                temp.setValue("");
                temp.setTime(newDate);
                map.put(DateUtil.format(dateStart,"yyyy-MM-dd HH:mm:ss"),temp);
            }
        }

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {

            String key = iter.next();
            MonitorInfoVO infoVO  = map.get(key);
            infoVO.setTime(DateUtil.parse(key,"yyyy-MM-dd HH:mm:ss"));
            results.add(infoVO);
        }
        return results;
    }

}
