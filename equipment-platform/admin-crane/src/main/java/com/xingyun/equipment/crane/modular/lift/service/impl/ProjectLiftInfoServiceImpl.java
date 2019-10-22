package com.xingyun.equipment.crane.modular.lift.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xingyun.equipment.Const;
import com.xingyun.equipment.crane.modular.device.model.ProjectLiftDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.StringCompress;
import com.xingyun.equipment.crane.modular.device.vo.ProjectLiftAlarmVO;
import com.xingyun.equipment.crane.modular.device.vo.WaterAlarmVO;
import com.xingyun.equipment.crane.modular.lift.dao.ProjectLiftInfoMapper;
import com.xingyun.equipment.crane.modular.lift.model.ProjectLiftAlarmInfo;
import com.xingyun.equipment.crane.modular.lift.model.ProjectLiftInfo;
import com.xingyun.equipment.crane.modular.lift.service.ProjectLiftInfoService;
import com.xingyun.equipment.crane.modular.lift.vo.ProjectLiftAlarmCountVO;
import com.xingyun.equipment.crane.modular.lift.vo.ProjectLiftAlarmInfoVO;
import com.xingyun.equipment.crane.modular.lift.vo.ProjectLiftInfoVO;
import com.xingyun.equipment.crane.modular.lift.vo.ProjectLiftListVO;
import com.xingyun.equipment.crane.modular.lift.vo.ProjectLiftOrgVO;

import cn.hutool.json.JSONUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caobinbin
 * @since 2018-08-17
 */
@Service
public class ProjectLiftInfoServiceImpl  implements ProjectLiftInfoService {
	
	@Autowired
	protected ProjectLiftInfoMapper projectLiftInfoMapper;
	
	
/**
     *升降机设备列表
* @param request
* @return
*/
 @Override
 public byte[] getLiftInfo(RequestDTO request) {
 	
 	String tableName = "t_project_lift_detail"+"_"+request.getYearMonth();
 	Map<String,Object> map = new HashMap<String,Object>();
 	    int pageIndex = request.getPageNum() - 1;
        int pageSize = request.getPageSize();
        int offSet = pageIndex * pageSize;
        map.put("offSet",offSet);
        map.put("pageSize",pageSize);
        
		map.put("tableName", tableName);
		map.put("uuid", request.getUuid());
		map.put("deviceNo", request.getDeviceNo());
		
	List<ProjectLiftInfo> list = projectLiftInfoMapper.getLiftInfo(map);
	ProjectLiftInfoVO res = new ProjectLiftInfoVO();
	res.setLiftList(list);
	res.setTotal(projectLiftInfoMapper.getLiftInfoCount(map));
	ResultDTO<ProjectLiftInfoVO> resultDTO =new  ResultDTO<ProjectLiftInfoVO>(true,res);
	String resultStr = JSONUtil.toJsonStr(resultDTO);
    return StringCompress.compress(resultStr);
   
 }
 /**
  *升降机警告
* @param request
* @return
*/
@Override
public byte[] getAlarmInfo(RequestDTO request) {
	System.out.println(request);
	String tableName = "t_project_lift_alarm"+"_"+request.getYearMonth();
	String[] uuids = request.getUuids().split(",");;
	Map<String,Object> map = new HashMap<String,Object>();
	 int pageIndex = request.getPageNum() - 1;
     int pageSize = request.getPageSize();
     int offSet = pageIndex * pageSize;
     map.put("offSet",offSet);
     map.put("pageSize",pageSize);
		map.put("tableName", tableName);
		map.put("deviceNo", request.getDeviceNo());
		map.put("alarmId", request.getAlarmId());
		map.put("uuids", uuids);
	
	List<ProjectLiftAlarmInfo> list = projectLiftInfoMapper.getAlarmInfo(map);
	ProjectLiftAlarmInfoVO res = new ProjectLiftAlarmInfoVO();
	res.setTotal(projectLiftInfoMapper.getAlarmInfoCount(map));
	
	
	res.setAlarmList(list);
	ResultDTO<ProjectLiftAlarmInfoVO> resultDTO =new  ResultDTO<ProjectLiftAlarmInfoVO>(true,res);
	String resultStr = JSONUtil.toJsonStr(resultDTO);
    return StringCompress.compress(resultStr);
  
}

/**
 * 智慧工地拉取报警明细信息
 */
@Override
public byte[] getAlarmDetail(RequestDTO<ProjectLiftAlarmInfo> res)  {
	Page<ProjectLiftAlarmInfo> page = new Page<>(res.getPageNum(),res.getPageSize());
	HashMap<String, Object> map = new HashMap<>();
	 map.put("tableName","t_project_lift_alarm_" + res.getYearMonth());
	 map.put("alarmId", res.getAlarmId());
     map.put("deviceNo", res.getDeviceNo());
    List<ProjectLiftAlarmInfo> waters = projectLiftInfoMapper.getAlarmDetail(page,map);
    Map<String,Object> resultMap = new HashMap<>(10);
    resultMap.put("total",page.getTotal());
    resultMap.put("infoList",waters);
    ResultDTO<Map<String,Object>> resultDTO = new ResultDTO<>(true,resultMap);
    String resultStr = JSONUtil.toJsonStr(resultDTO);
    return StringCompress.compress(resultStr);
}
/**
 *  查询集团下所有升降机(接口)
 */
@Override
public ResultDTO<ProjectLiftOrgVO> getLiftList(RequestDTO request) {
		request.setOrgIds(Const.orgIds.get());
		request.setOrgId(null);
	 List<ProjectLiftListVO> list = projectLiftInfoMapper.getLiftList(request);
     ProjectLiftOrgVO res = new ProjectLiftOrgVO();
     res.setDeviceList(list);
     return new ResultDTO<ProjectLiftOrgVO>(true,res);
}

/**
 * 查询塔吊最近一条数据(接口)
 * @param request
 * @return7
 */
@Override
public ResultDTO<ProjectLiftListVO> getLiftDetail(@RequestBody RequestDTO request) {
	Calendar ca=Calendar.getInstance();
	SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
String tableName = "t_project_lift_detail"+"_"+cal.format(ca.getTime());
Map<String,Object> map = new HashMap<String,Object>();
map.put("deviceNo", request.getDeviceNo());
map.put("orgId", request.getOrgId());
map.put("tableName", tableName);
ProjectLiftListVO res = projectLiftInfoMapper.getLiftDetail(map);
        return new ResultDTO<>(true,res);
}


		/**
		 * 查询升降机当月重量报警/预警 数量(接口
		 * @param request
		 * @return
		 */
		@Override
		public ResultDTO<ProjectLiftAlarmCountVO> getLiftAlarmCount(@RequestBody RequestDTO request) {
			Calendar ca=Calendar.getInstance();
			SimpleDateFormat  cal=new SimpleDateFormat("yyyyMM");
		String tableName = "t_project_lift_alarm"+"_"+cal.format(ca.getTime());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceNo", request.getDeviceNo());
		map.put("orgId", request.getOrgId());
		map.put("tableName", tableName);
		List<ProjectLiftAlarmVO> result = projectLiftInfoMapper.getLiftAlarmCount(map);
		
		List<ProjectLiftAlarmVO> list ;
		int alarmWeight = 0;
		int alarm = 0;
		for(int i=0;i<result.size();i++ ) {
			
			if(result.get(i).getAlarmId() == 2) {
				alarmWeight =alarmWeight +result.get(i).getCountStatus();
			}
			if(result.get(i).getAlarmId() ==1|result.get(i).getAlarmId() == 3
					|result.get(i).getAlarmId() == 4|result.get(i).getAlarmId() == 5
					) {
				alarm =alarm +result.get(i).getCountStatus();
			}
		}
		
		
	
	
		ProjectLiftAlarmCountVO res = new ProjectLiftAlarmCountVO();
		res.setEarlyWarning(alarmWeight);
		res.setAlarm(alarm);
		        return new ResultDTO<>(true,res);
		}

	@Override
	public ResultDTO<ProjectLiftDetail> getTop100LiftDetail(String uuid,String deviceNo) {


		return new ResultDTO(true, projectLiftInfoMapper.getTop100LiftDetail(uuid,deviceNo));
	}

}
