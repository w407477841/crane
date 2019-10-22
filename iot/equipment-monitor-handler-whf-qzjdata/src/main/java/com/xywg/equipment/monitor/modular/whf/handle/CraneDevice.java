package com.xywg.equipment.monitor.modular.whf.handle;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.AliConfig;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.ProducerUtil;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.*;
import com.xywg.equipment.monitor.modular.whf.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.modular.whf.factory.BaseFactory;
import com.xywg.equipment.monitor.modular.whf.factory.CraneAlarmFactory;
import com.xywg.equipment.monitor.modular.whf.model.*;
import com.xywg.equipment.monitor.modular.whf.service.*;
import com.xywg.equipment.monitor.modular.whf.service.impl.ProjectCraneServiceImpl;
import com.xywg.equipment.monitor.modular.whf.service.impl.PushService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author: wangyifei
* Description:  塔吊数据处理类
* Date: 10:45 2018/8/27
*/
@Component
public class CraneDevice {

	@Autowired
	private IProjectCraneService projectCraneService;
	@Autowired
	private IProjectCraneOriginalDataService projectCraneOriginalDataService;
	@Autowired
	private IProjectCraneDetailService projectCraneDetailService;
	@Autowired
	private IProjectCraneAlarmService projectCraneAlarmService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IProjectInfoService projectInfoService;
	@Autowired
	private PushService pushService;
	@Autowired
	XywgProerties xywgProerties;
	@Autowired
	ZbusProducerHolder zbusProducerHolder;
	@Autowired
	IProjectCraneHeartbeatService projectCraneHeartbeatService;
	@Autowired
	ProducerUtil  producerUtil;
	@Autowired
	AliConfig aliConfig;

	@Transactional(rollbackFor = Exception.class)
	public void insertData(ProjectCraneDetail detail,String serverTopic) throws Exception {
		//获取设备信息
		ProjectCrane crane =  projectCraneService.selectOne(detail.getDeviceNo());
		if(crane == null){
			// 压根没有这个设备
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","crane")));
			throw new RuntimeException("数据库中没有"+detail.getDeviceNo()+"该设备");
		}
		if(0==crane.getStatus()){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","crane")));
			throw new RuntimeException("" + detail.getDeviceNo() + "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(crane.getProjectId());
		if(projectInfo==null){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(detail.getDeviceNo(),"01","crane")));
			throw new RuntimeException("设备"+detail.getDeviceNo()+"不在任何项目下");
		}
		detail.setCraneId(crane.getId());
		CurrentCraneData currentMonitorData =CurrentCraneData.factory(detail,crane);
		projectCraneDetailService.createDetail(detail, BaseFactory.getTableName(ProjectCraneDetail.class));
		// 比对 阈值
		List<ProjectCraneAlarm> alarms = CraneAlarmFactory.factory(crane,detail);
		List<AlarmDTO>   alarmInfo   = new ArrayList<>();
		for(ProjectCraneAlarm alarm:alarms){
			projectCraneAlarmService.createAlarm(alarm,BaseFactory.getTableName(ProjectCraneAlarm.class));
			alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"塔机设备",detail,crane.getName()));
			pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"塔机设备",detail,crane.getName()),projectInfo.getUuid());
		}
		//推送实时数据
		this.push(redisUtil,crane.getDeviceNo(),"crane",projectInfo.getUuid(),currentMonitorData,""+crane.getProjectId());
		//推送报警数据
		if(alarmInfo.size()>0){
			ResultDTO redisData =  ResultDTO.factory(alarmInfo,20);
			//放入缓存
			this.push(redisUtil,crane.getDeviceNo(),"crane_alarm",projectInfo.getUuid(),redisData,""+crane.getProjectId());
		}
	}

	public ProjectCraneDetail insertOriginData(String data) {
		ProjectCraneDetail  detail =  JSONUtil.toBean(data,ProjectCraneDetail.class) ;
		ProjectCraneOriginalData originalData  =  new ProjectCraneOriginalData();
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
		System.out.println("订阅路径："+topic);
		// 推送至 websocket
		RemoteDTO remoteDTO = RemoteDTO.factory(topic,JSONUtil.toJsonStr(data));
		if(aliConfig.isOpen()){
			zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
		}else{
			if(!deviceType.endsWith("alarm")){
				zbusProducerHolder.sendWebsocketMessage(JSONUtil.toJsonStr(remoteDTO));
			}
		}

	}

	/**
	 * 上线
	 * @param ht
	 */
	public void online(Map<String,Object> ht) {


		ProjectCraneHeartbeat heartbeat = new ProjectCraneHeartbeat();
		String deviceNo = (String)ht.get("deviceNo");
		Long time = (Long) ht.get("deviceTime");
		String serverTopic = (String) ht.get("serverTopic");
		heartbeat.setDeviceNo(deviceNo);
		// 获取 设备信息

		ProjectCrane crane =  projectCraneService.selectOne(deviceNo);
		if(crane == null){
			// 压根没有这个设备
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","crane")));
			throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
		}
		if(0==crane.getStatus()){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","crane")));
			throw new RuntimeException("" +deviceNo + "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(crane.getProjectId());
		if(projectInfo==null){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","crane")));
			throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
		}
		heartbeat.setCraneId(crane.getId());
		heartbeat.setCreateTime(new Date(time));
		projectCraneHeartbeatService.doOpenBusiness(heartbeat);
		// 此处设备为在线
		//1.修改数据库 online 字段
		ProjectCrane updCrane = new ProjectCrane();
		updCrane.setId(crane.getId());
		updCrane.setIsOnline(1);
		projectCraneService.updateById(updCrane);
		//删除 设备信息缓存
		String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
		redisUtil.remove(deviceKey);
	}

	public Map<String,Object>  insertOrigin(String data) {
		Map<String,Object> ht =  JSONUtil.toBean(data,Map.class) ;
		ProjectCraneOriginalData originalData  =  new ProjectCraneOriginalData();
		originalData.setOriginalData(data);
		originalData.setDeviceTime(new Date((Long)ht.get("deviceTime")));
		return ht;
	}

	/**
	 * 下线
	 */
	public void offline(String data,String serverTopic){
		String deviceNo =  data.split(":")[1];

		ProjectCrane crane =  projectCraneService.selectOne(deviceNo);
		if(crane == null){
			// 压根没有这个设备
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","crane")));
			throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
		}
		if(0==crane.getStatus()){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","crane")));
			throw new RuntimeException("" +deviceNo + "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(crane.getProjectId());
		if(projectInfo==null){
			producerUtil.sendCtrlMessage(serverTopic,JSONUtil.toJsonStr(ControllerDTO.factory(deviceNo,"01","crane")));
			throw new RuntimeException("设备"+deviceNo+"不在任何项目下");
		}
		// 更新 受控数据的截止时间
		ProjectCraneHeartbeat  projectCraneHeartbeat = new ProjectCraneHeartbeat();
		projectCraneHeartbeat.setDeviceNo(deviceNo);
		projectCraneHeartbeat.setCreateTime(new Date());
		projectCraneHeartbeatService.updateEndTime(projectCraneHeartbeat);

		//1.修改数据库 online 字段
		ProjectCrane updCrane = new ProjectCrane();
		updCrane.setId(crane.getId());
		updCrane.setIsOnline(0);
		projectCraneService.updateById(updCrane);
		//删除 设备信息缓存
		String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
		redisUtil.remove(deviceKey);

	}
}
