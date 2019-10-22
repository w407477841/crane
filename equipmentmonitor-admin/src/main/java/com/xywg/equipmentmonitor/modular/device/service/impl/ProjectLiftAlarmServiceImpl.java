package com.xywg.equipmentmonitor.modular.device.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectLiftAlarmMapper;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftAlarm;
import com.xywg.equipmentmonitor.modular.device.service.ProjectLiftAlarmService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftAlarmVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftVO;
import com.xywg.equipmentmonitor.modular.device.vo.RealTimeMonitoringTowerVo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
@Service
public class ProjectLiftAlarmServiceImpl extends ServiceImpl<ProjectLiftAlarmMapper, ProjectLiftAlarm> implements ProjectLiftAlarmService {
	@Autowired
	private ProjectLiftAlarmMapper projectLiftAlarmMapper;
	/**
     * 列表
     * @param request
     * @return
     */
      @Override
      public ResultDTO<DataDTO<List<ProjectLiftAlarm>>> getPageList(RequestDTO<ProjectLiftAlarm> request) {
          Page<ProjectLiftAlarm> page = new Page<ProjectLiftAlarm>(request.getPageNum(), request.getPageSize());
          List<ProjectLiftAlarm> list = projectLiftAlarmMapper.selectPageList(page);
          return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
      }
      
      
       
      
/*0：正常；
      1：重量报警；
      2：重量预警；
      3：速度报警；
      4：前门报警；
      5：后门报警：*/
      
      

  	  /**
            * 预警
       * @param request
       * @return
       */
        @Override
        public ResultDTO<ProjectLiftAlarmVO> countEarlyWarning(ProjectLiftAlarmVO c) {
        	 Calendar ca=java.util.Calendar.getInstance();
				SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
				SimpleDateFormat  cal2=new SimpleDateFormat("yyyyMMdd");
        	String tableName = "t_project_lift_alarm"+"_"+cal.format(ca.getTime());
        	Map<String,Object> map = new HashMap<String,Object>();
    		map.put("tableName", tableName);
    		map.put("id", c.getId());
    		if(c.getDeviceTimeEnd() != null || c.getDeviceTimeBegin() !=null) {
    			c =judgeTime(c);
        		map.put("deviceTimeBegin", cal2.format(c.getDeviceTimeBegin()));
        		map.put("deviceTimeEnd", Integer.parseInt(cal2.format(c.getDeviceTimeEnd()))+ 1);
    		}
    		
    		
    		ProjectLiftAlarmVO res = new ProjectLiftAlarmVO();
    		List<ProjectLiftAlarmVO> result= baseMapper.countEarlyWarning(map);
    		List<ProjectLiftAlarmVO> list ;
    		int alarmWeight = 0;
    		for(int i=0;i<result.size();i++ ) {
    			
    			if(result.get(i).getAlarmId() == 2) {
    				alarmWeight =alarmWeight +result.get(i).getCountStatus();
    			}
    		}
    		
    		res.setLiftId(c.getId());
    		res.setAlarmWeight(alarmWeight);
    		res.setAlarmAll(alarmWeight);
            return new ResultDTO<ProjectLiftAlarmVO>(true,res);
        }
        
    
        
   	  /**
         * 报警
    * @param request
    * @return
    */
     @Override
     public ResultDTO<ProjectLiftAlarmVO> countCallWarning(ProjectLiftAlarmVO c) {
     	 Calendar ca=java.util.Calendar.getInstance();
				SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
				SimpleDateFormat  cal2=new SimpleDateFormat("yyyyMMdd");
     	String tableName = "t_project_lift_alarm"+"_"+cal.format(ca.getTime());
     	Map<String,Object> map = new HashMap<String,Object>();
 		map.put("tableName", tableName);
 		map.put("id", c.getId());
 		
 		if(c.getDeviceTimeEnd() != null || c.getDeviceTimeBegin() !=null) {
			c =judgeTime(c);
    		map.put("deviceTimeBegin", cal2.format(c.getDeviceTimeBegin()));
    		map.put("deviceTimeEnd", Integer.parseInt(cal2.format(c.getDeviceTimeEnd()))+ 1);
		}
 		ProjectLiftAlarmVO res = new ProjectLiftAlarmVO();
 		List<ProjectLiftAlarmVO> result= baseMapper.countEarlyWarning(map);
 		
 		int alarmWeight = 0;
 		int alarmSpeed = 0;
 		int alarmFrontDoor = 0;
 		int alarmBackDoor = 0;
 		for(int i=0;i<result.size();i++ ) {
 			
 			if(result.get(i).getAlarmId() == 1) {
 				alarmWeight =alarmWeight +result.get(i).getCountStatus();
 			}
 			if(result.get(i).getAlarmId() == 3) {
 				alarmSpeed =alarmSpeed +result.get(i).getCountStatus();
 			}
 			if(result.get(i).getAlarmId() == 4) {
 				alarmFrontDoor =alarmFrontDoor +result.get(i).getCountStatus();
 			}
 			if(result.get(i).getAlarmId() == 5) {
 				alarmBackDoor =alarmBackDoor +result.get(i).getCountStatus();
 			}
 		}
 		res.setLiftId(c.getId());
 		res.setAlarmWeight(alarmWeight);
 		res.setAlarmSpeed(alarmSpeed);
 		res.setAlarmFrontDoor(alarmFrontDoor);
 		res.setAlarmBackDoor(alarmBackDoor);
 		res.setAlarmAll(alarmWeight+alarmSpeed+alarmFrontDoor+alarmBackDoor);
         return new ResultDTO<ProjectLiftAlarmVO>(true,res);
     }
        
  	  /**
      *违章
 * @param request
 * @return
 */
  @Override
  public ResultDTO<ProjectLiftAlarmVO> countViolation(ProjectLiftAlarmVO c) {
  	 Calendar ca=java.util.Calendar.getInstance();
				SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
				SimpleDateFormat  cal2=new SimpleDateFormat("yyyyMMdd");
  	String tableName = "t_project_lift_alarm"+"_"+cal.format(ca.getTime());
  	Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName", tableName);
		map.put("id", c.getId());
		if(c.getDeviceTimeEnd() != null || c.getDeviceTimeBegin() !=null) {
			c =judgeTime(c);
    		map.put("deviceTimeBegin", cal2.format(c.getDeviceTimeBegin()));
    		map.put("deviceTimeEnd", Integer.parseInt(cal2.format(c.getDeviceTimeEnd()))+ 1);
		}
		ProjectLiftAlarmVO res = new ProjectLiftAlarmVO();
		List<ProjectLiftAlarmVO> result= baseMapper.countEarlyWarning(map);
		
		int alarmWeight = 0;
		for(int i=0;i<result.size();i++ ) {
			
			if(result.get(i).getAlarmId() == 1) {
				alarmWeight =alarmWeight +result.get(i).getCountStatus();
			}
		}
		res.setLiftId(c.getId());
		res.setAlarmWeight(alarmWeight);
		res.setAlarmAll(alarmWeight);
      return new ResultDTO<ProjectLiftAlarmVO>(true,res);
  }


  /**
   * 判断时间
   */

	@Override
	public ProjectLiftAlarmVO judgeTime(ProjectLiftAlarmVO c) {
		if(c.getDeviceTimeEnd() == null && c.getDeviceTimeBegin() !=null) {
			 Calendar ca=java.util.Calendar.getInstance();
			
			 
			c.setDeviceTimeEnd(ca.getTime());
		}
		if(c.getDeviceTimeEnd() != null && c.getDeviceTimeBegin() ==null) {
			c.setDeviceTimeBegin(c.getDeviceTimeEnd());
		}
		// TODO Auto-generated method stub
		return c;
	}

	/*0：正常；
    1：重量报警；
    2：重量预警；
    3：速度报警；
    4：前门报警；
    5：后门报警：*/
/**
 * 查看详情
 */

@Override
public ResultDTO<DataDTO<List<ProjectLiftAlarmVO>>> earlyWarningDetail(RequestDTO<ProjectLiftAlarmVO> request) {
	ProjectLiftAlarmVO c = request.getBody();
	Calendar ca=java.util.Calendar.getInstance();
		SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
		SimpleDateFormat  cal2=new SimpleDateFormat("yyyyMMdd");
      String tableName = "t_project_lift_alarm"+"_"+cal.format(ca.getTime());
    
	Map<String,Object> map = new HashMap<String,Object>();
	map.put("tableName", tableName);
	map.put("id",c.getId());
    map.put("alarmType", c.getAlarmType());
	if(c.getDeviceTimeEnd() != null || c.getDeviceTimeBegin() !=null) {
		c =judgeTime(c);
		map.put("deviceTimeBegin", cal2.format(c.getDeviceTimeBegin()));
	map.put("deviceTimeEnd", Integer.parseInt(cal2.format(c.getDeviceTimeEnd()))+ 1);
	}
	Page<ProjectLiftAlarmVO> page = new Page<ProjectLiftAlarmVO>(request.getPageNum(), request.getPageSize());
     
	 List<ProjectLiftAlarmVO> list = baseMapper.earlyWarningDetail(page,map);
     return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
	
}


/**
 * 实时监控查询
 * @param request
 * @return
 */
 
@Override
public ResultDTO<DataDTO<List<ProjectLift>>> realTimeMonitoring(RequestDTO<ProjectLift> request) {
	Calendar ca=java.util.Calendar.getInstance();
	SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
    String tableName = "t_project_lift_detail"+"_"+cal.format(ca.getTime());
	Page<ProjectLift> page = new Page<ProjectLift>(request.getPageNum(), request.getPageSize());
    EntityWrapper<ProjectLift> wrapper=new EntityWrapper<>();
    ProjectLift c= request.getBody();
    wrapper.eq("a.is_del",0);
    String deviceNo=request.getBody().getDeviceNo();
    wrapper.like(StrUtil.isNotBlank(deviceNo),"device_no",deviceNo);
    if(c.getProjectId() != null) {
    	wrapper.eq("a.project_id",c.getProjectId());
    }
    wrapper.in("a.org_id", Const.orgIds.get());
    if(c.getIsOnline() != null) {
    	wrapper.last("  AND   IFNULL((SELECT IF (      (     UNIX_TIMESTAMP(CURRENT_TIMESTAMP()) - UNIX_TIMESTAMP(tt.end_time)    ) > 45,0,1    )  FROM t_project_lift_heartbeat tt where tt.lift_id =a.id  ORDER BY id DESC  LIMIT 1),0) = " +c.getIsOnline());
    
    }
    Map<String,Object> map = new HashMap<String,Object>();
	map.put("tableName", tableName);
	map.put("deviceNo",deviceNo);
    
    
    List<ProjectLift> list = baseMapper.realTimeMonitoring(page,wrapper,map);
    return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
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

      
  
      
      
      
}
