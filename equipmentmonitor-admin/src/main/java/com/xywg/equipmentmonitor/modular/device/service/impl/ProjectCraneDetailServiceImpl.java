package com.xywg.equipmentmonitor.modular.device.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.DateUtils;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectCraneDetailMapper;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCraneDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectCraneDetailService;
import com.xywg.equipmentmonitor.modular.station.dto.WeightAndMomentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-12-20
 */
@Service
public class ProjectCraneDetailServiceImpl extends ServiceImpl<ProjectCraneDetailMapper, ProjectCraneDetail> implements ProjectCraneDetailService {


	/**
	 * 获取实力吊重和力矩数据
	 * @param param
	 * @return
	 */
	@Override
	public List<WeightAndMomentVO> getWeightAndMomentVO(Map<String, Object> param) {
		List<WeightAndMomentVO> list = baseMapper.getWeightAndMomentVO(param);
		return list;
	}
    @Override
    public ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate){
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
        tableName ="t_project_crane_detail_"+yearMonth;
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
        		tableName1="t_project_crane_detail_"+yearMonth1;
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
           		tableName1="t_project_crane_detail_"+yearMonth1;
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
              		tableName1="t_project_crane_detail_"+yearMonth1;
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
            	tableName1="t_project_crane_detail_"+eYear+eMonth;
            	if(Integer.valueOf(bYear+bMonth).equals(Integer.valueOf(eYear+eMonth))) {
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
        	
        
        }
        return new ResultDTO(true,DataDTO.factory(dataList,dataList.size()));
    }
}
