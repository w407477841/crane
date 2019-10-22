package com.xywg.equipment.monitor.modular.whf.handle;


import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.ProducerUtil;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.netty.NettyClientConfig;
import com.xywg.equipment.monitor.modular.whf.convert.MonitorConvert;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectEnvironmentMonitorDetailMapper;
import com.xywg.equipment.monitor.modular.whf.dto.*;
import com.xywg.equipment.monitor.modular.whf.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.modular.whf.factory.BaseFactory;
import com.xywg.equipment.monitor.modular.whf.factory.MonitorAlarmFactory;
import com.xywg.equipment.monitor.modular.whf.model.*;
import com.xywg.equipment.monitor.modular.whf.service.*;
import com.xywg.equipment.monitor.modular.whf.service.impl.ProjectEnvironmentMonitorServiceImpl;
import com.xywg.equipment.monitor.modular.whf.service.impl.PushService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author: wangyifei
* Description:
* Date: 16:45 2018/10/25
*/
@Component
public class EnvironmentMonitorDevice{
	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentMonitorDevice.class);
	@Autowired
	IProjectEnvironmentMonitorService projectEnvironmentMonitorService;
	@Autowired
	IProjectEnvironmentMonitorOriginalDataService liftOriginalDataService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IProjectEnvironmentHeartbeatService projectEnvironmentHeartbeatService;
	@Autowired
	IProjectEnvironmentMonitorDetailService   projectEnvironmentMonitorDetailService;
	@Autowired
	IProjectEnvironmentMonitorAlarmService   projectEnvironmentMonitorAlarmService;

	@Autowired
	IProjectInfoService    projectInfoService;

	@Autowired
	XywgProerties   xywgProerties;
	@Autowired
	ZbusProducerHolder   zbusProducerHolder;
	@Autowired
	PushService    pushService;

	@Autowired
	private ProducerUtil producerUtil;
	@Transactional(rollbackFor = Exception.class)
	public void insertData(ProjectEnvironmentMonitorDetail detail,String serverTopic) throws Exception {

		//获取设备信息
		ProjectEnvironmentMonitor  monitor =  projectEnvironmentMonitorService.selectOne(detail.getDeviceNo());
		if(monitor == null){
			// 压根没有这个设备
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","monitor")));
			throw new RuntimeException("数据库中没有"+detail.getDeviceNo()+"该设备");
		}
		if(0==monitor.getStatus()){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","monitor")));
			throw new RuntimeException("" + detail.getDeviceNo() + "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(monitor.getProjectId());
		if(projectInfo==null){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","monitor")));
			throw new RuntimeException("设备"+detail.getDeviceNo()+"不在任何项目下");
		}
		// 做个采集日志
		detail.setMonitorId(monitor.getId());
		detail = MonitorConvert.convert(detail);
		CurrentMonitorData currentMonitorData =CurrentMonitorData.factory(detail,monitor);


		projectEnvironmentMonitorDetailService.createDetail(detail,BaseFactory.getTableName(ProjectEnvironmentMonitorDetail.class));

		// 比对 阈值
		List<ProjectEnvironmentMonitorAlarm> alarms = MonitorAlarmFactory.factory(monitor,detail);

		List<AlarmDTO>   alarmInfo   = new ArrayList<>();
		for(ProjectEnvironmentMonitorAlarm alarm:alarms){
			projectEnvironmentMonitorAlarmService.createAlarm(alarm,BaseFactory.getTableName(ProjectEnvironmentMonitorAlarm.class));
			alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"扬尘设备",detail,monitor.getName()));
			pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"扬尘设备",detail,monitor.getName()),projectInfo.getUuid());


		}
		//推送实时数据
		this.push(redisUtil,monitor.getDeviceNo(),"monitor",projectInfo.getUuid(),currentMonitorData,""+monitor.getProjectId());
		//推送报警数据
		if(alarmInfo.size()>0){
			ResultDTO redisData =  ResultDTO.factory(alarmInfo,20);
			//放入缓存
			this.push(redisUtil,monitor.getDeviceNo(),"monitor_alarm",projectInfo.getUuid(),redisData,""+monitor.getProjectId());
		}
//
//		if(monitor.getNeedDispatch() != null && monitor.getNeedDispatch() > 0) {
//			// 转发
//			NettyClientConfig.client.sendMsg(dispatchStr);
//			LOGGER.info("转发数据成功...");
//		}


	}

	public ProjectEnvironmentMonitorDetail insertOriginData(String data) {
		ProjectEnvironmentMonitorDetail  detail =  JSONUtil.toBean(data,ProjectEnvironmentMonitorDetail.class) ;
		ProjectEnvironmentMonitorOriginalData originalData  =  new ProjectEnvironmentMonitorOriginalData();
		originalData.setOriginalData(data);
		originalData.setDeviceTime(detail.getDeviceTime());
		return detail;
	}



	public  void push(RedisUtil redisUtil,String deviceNo,String deviceType,String uuid,Object data,String projectId){
		String key  = xywgProerties.getRedisHead()+":current:"+uuid+":"+deviceType+":"+deviceNo;
		String topic = "";
		if(deviceType.endsWith("alarm")){
			topic="/topic/current/"+deviceType+"/"+uuid;
		}else{
			topic="/topic/current/"+deviceType+"/"+uuid+"/"+deviceNo;
			redisUtil.set(key, JSONUtil.toJsonStr(data));
			if(StringUtils.isNotEmpty(projectId)){
				String tempKey  = xywgProerties.getRedisHead()+":current:"+projectId+":"+deviceType+":"+deviceNo;
				String tempTopic="/topic/current/"+deviceType+"/"+projectId+"/"+deviceNo;
				redisUtil.set(tempKey, JSONUtil.toJsonStr(data));
				RemoteDTO remoteDTO = RemoteDTO.factory(tempTopic,JSONUtil.toJsonStr(data));
				zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));

			}
		}
		// 推送至 websocket
		RemoteDTO remoteDTO = RemoteDTO.factory(topic,JSONUtil.toJsonStr(data));
		zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));

	}

	/**
	 * 上线
	 * @param ht
	 */
	public void online(Map<String,Object> ht) {


		ProjectEnvironmentHeartbeat heartbeat = new ProjectEnvironmentHeartbeat();
		String deviceNo = (String)ht.get("deviceNo");
		Long time = (Long) ht.get("deviceTime");
		String serverTopic = (String) ht.get("serverTopic");
		heartbeat.setDeviceNo(deviceNo);
		// 获取 设备信息

		//获取设备信息
		ProjectEnvironmentMonitor  monitor =  projectEnvironmentMonitorService.selectOne(heartbeat.getDeviceNo());
		if(monitor == null){
			// 压根没有这个设备
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(heartbeat.getDeviceNo(),"01","monitor")));
			throw new RuntimeException("数据库中没有"+heartbeat.getDeviceNo()+"该设备");
		}
		if(0==monitor.getStatus()){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(heartbeat.getDeviceNo(),"01","monitor")));
			throw new RuntimeException("" + heartbeat.getDeviceNo() + "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(monitor.getProjectId());
		if(projectInfo==null){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(heartbeat.getDeviceNo(),"01","monitor")));
			throw new RuntimeException("设备"+heartbeat.getDeviceNo()+"不在任何项目下");
		}
		heartbeat.setMonitorId(monitor.getId());
		heartbeat.setCreateTime(new Date(time));
		projectEnvironmentHeartbeatService.doOpenBusiness(heartbeat);

		//1.修改数据库 online 字段
		ProjectEnvironmentMonitor updMonitor = new ProjectEnvironmentMonitor();
		updMonitor.setId(monitor.getId());
		updMonitor.setIsOnline(1);
		projectEnvironmentMonitorService.updateById(updMonitor);
		//删除 设备信息缓存
		String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
		redisUtil.remove(deviceKey);

	}

	public Map<String,Object>  insertOrigin(String data) {
		Map<String,Object> ht =  JSONUtil.toBean(data,Map.class) ;
		ProjectEnvironmentMonitorOriginalData originalData  =  new ProjectEnvironmentMonitorOriginalData();
		originalData.setOriginalData(data);
		originalData.setDeviceTime(new Date((Long)ht.get("deviceTime")));
		return ht;
	}

	/**
	 * 下线
	 */
	public void offline(String data,String serverTopic){
		String deviceNo =  data.split(":")[1];

		//获取设备信息
		ProjectEnvironmentMonitor  monitor =  projectEnvironmentMonitorService.selectOne(deviceNo);
		if(monitor == null){
			// 压根没有这个设备
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","monitor")));
			throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
		}
		if(0==monitor.getStatus()){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","monitor")));
			throw new RuntimeException("" + deviceNo+ "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(monitor.getProjectId());
		if(projectInfo==null){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","monitor")));
			throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
		}
		// 更新 受控数据的截止时间
		ProjectEnvironmentHeartbeat projectEnvironmentHeartbeat = new ProjectEnvironmentHeartbeat();
		projectEnvironmentHeartbeat.setDeviceNo(deviceNo);
		projectEnvironmentHeartbeat.setCreateTime(new Date());
		projectEnvironmentHeartbeatService.updateEndTime(projectEnvironmentHeartbeat);

		//1.修改数据库 online 字段
		ProjectEnvironmentMonitor updMonitor = new ProjectEnvironmentMonitor();
		updMonitor.setId(monitor.getId());
		updMonitor.setIsOnline(0);
		projectEnvironmentMonitorService.updateById(updMonitor);
		//删除 设备信息缓存
		String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
		redisUtil.remove(deviceKey);

	}
}
