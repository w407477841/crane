package com.xywg.equipment.monitor.iot.modular.envmon.handle;


import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.core.util.OriDataUtil;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.handler.BaseDevice;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.envmon.convert.MonitorConvert;
import com.xywg.equipment.monitor.iot.modular.envmon.dao.ProjectEnvironmentMonitorAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.envmon.dao.ProjectEnvironmentMonitorDetailMapper;
import com.xywg.equipment.monitor.iot.modular.envmon.dto.CurrentMonitorData;
import com.xywg.equipment.monitor.iot.modular.envmon.factory.MonitorAlarmFactory;
import com.xywg.equipment.monitor.iot.modular.envmon.model.*;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentHeartbeatService;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentMonitorOriginalDataService;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentMonitorService;
import com.xywg.equipment.monitor.iot.modular.envmon.service.impl.ProjectEnvironmentMonitorServiceImpl;
import com.xywg.equipment.monitor.iot.netty.aop.ZbusProducerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class EnvironmentMonitorDevice extends BaseDevice {
	private static  final Logger LOGGER = LoggerFactory.getLogger(EnvironmentMonitorDevice.class);



	@Autowired
	ProjectEnvironmentMonitorDetailMapper detailMapper;
	@Autowired
	IProjectEnvironmentMonitorService   projectEnvironmentMonitorService;
	@Autowired
	ProjectEnvironmentMonitorAlarmMapper  alarmMapper;
	@Autowired
	IProjectEnvironmentMonitorOriginalDataService liftOriginalDataService;
	@Autowired
	RedisUtil    redisUtil;
	@Autowired
	IProjectEnvironmentHeartbeatService   projectEnvironmentHeartbeatService;
	@Autowired
    IProjectInfoService   projectInfoService;
	@Autowired
	ZbusProducerHolder   zbusProducerHolder  ;


	@Override
	public boolean isHeartbeat(String data) {
		if(data.startsWith("ycht")){
			ProjectEnvironmentHeartbeat  heartbeat = new ProjectEnvironmentHeartbeat();
			heartbeat.setDeviceNo(data.split(":")[1]);
			// 获取 设备信息

			ProjectEnvironmentMonitor  monitor =  projectEnvironmentMonitorService.selectOne(heartbeat.getDeviceNo());
			if(monitor == null){
				throw new RuntimeException("数据库中没有"+heartbeat.getDeviceNo()+"该设备");
			}
			if(0==monitor.getStatus()){
				throw new RuntimeException("" + heartbeat.getDeviceNo() + "该设备未启用");
			}



			heartbeat.setMonitorId(monitor.getId());
			heartbeat.setCreateTime(new Date());
			if(!redisUtil.exists(this.xywgProerties.getRedisHead()+":"+"head:"+data)){
				//不存在 说明是开机
				//上一条数据是关机
				projectEnvironmentHeartbeatService.doOpenBusiness(heartbeat);
				//1.修改数据库 online 字段
				ProjectEnvironmentMonitor updMonitor = new ProjectEnvironmentMonitor();
				updMonitor.setId(monitor.getId());
				updMonitor.setIsOnline(1);
				projectEnvironmentMonitorService.updateById(updMonitor);
				//删除 设备信息缓存
				String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+heartbeat.getDeviceNo();
				redisUtil.remove(deviceKey);
				}else{
				projectEnvironmentHeartbeatService.updateEndTime(
						heartbeat
				);

			}
			redisUtil.setSec(this.xywgProerties.getRedisHead()+":"+"head:"+data,1,46L);
			return true;
		}


		return false;
	}

	@Override
	public void insertOrigin(String data) {
        OriDataUtil.YC_LOGGER.info(data);
        /*
		ProjectEnvironmentMonitorOriginalData originalData  =  new ProjectEnvironmentMonitorOriginalData();
		originalData.setOriginalData(data);
		originalData.setDeviceTime(new Date());
		liftOriginalDataService.insert(originalData);*/
	}

	@Override
	public void offline(String data) {
		String deviceNo =  data.split(":")[1];
		ProjectEnvironmentMonitor monitor =  projectEnvironmentMonitorService.selectOne(deviceNo);
		if(monitor == null){
			throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
		}
		if(0==monitor.getStatus()){
			throw new RuntimeException("" + deviceNo + "该设备未启用");
		}
		//1.修改数据库 online 字段
		ProjectEnvironmentMonitor updMonitor = new ProjectEnvironmentMonitor();
		updMonitor.setId(monitor.getId());
		updMonitor.setIsOnline(0);
		projectEnvironmentMonitorService.updateById(updMonitor);
		//删除 设备信息缓存
		String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
		redisUtil.remove(deviceKey);
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void doBusiness(String data, ZbusProducerHolder producerHolder, MasterProtocolConfigDTO config) {
		//模拟配置
		ProjectEnvironmentMonitorDataModel dataModel  = new ProjectEnvironmentMonitorDataModel();
		//设置 编号配对key
		dataModel.setDeviceNo("sdsyr");
		//设置 pm2.5配对字段
		dataModel.setPm25("a");
		//设置pm10 配对字段
		dataModel.setPm10("b");
		//设置温度 配对字段
		dataModel.setTemperature("c");
		//设置适度 配对字段
		dataModel.setHumidity("d");
		//设置噪音 配对字段
		dataModel.setNoise("e");
		//设置风速
		dataModel.setWindSpeed("j");
		//设置 风向
		dataModel.setWindDirection("k");
		//去除 :,  或  :结尾的数据
		data  = this.removeBlank(data);

		data =  this.replaceHead(data,dataModel.getDeviceNo(),"deviceNo");
		data =  this.replaceKey(data,dataModel.getPm25(),"pm25");
		data =  this.replaceKey(data,dataModel.getPm10(),"pm10");
		data =  this.replaceKey(data,dataModel.getTemperature(),"temperature");
		data =  this.replaceKey(data,dataModel.getHumidity(),"humidity");
		data =  this.replaceKey(data,dataModel.getNoise(),"noise");
		data =  this.replaceKey(data,dataModel.getWindSpeed(),"windSpeed");
		data =  this.replaceKey(data,dataModel.getWindDirection(),"windDirection");

		String jsonStr = "{"+data+"}";
		try {
			zbusProducerHolder.sendDispatchMessage(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(jsonStr);
		if(!JSONUtil.isJson(jsonStr)){
			// TODO 存入 错误数据
			throw new RuntimeException("数据格式异常");
		}

		ProjectEnvironmentMonitorDetail detail =	  JSONUtil.toBean(jsonStr,ProjectEnvironmentMonitorDetail.class);

		// 获取 设备信息

		ProjectEnvironmentMonitor  monitor =  projectEnvironmentMonitorService.selectOne(detail.getDeviceNo());

		if(monitor == null){
			// 压根没有这个设备
			throw new RuntimeException("数据库中没有"+detail.getDeviceNo()+"该设备");
		}
		if(0==monitor.getStatus()){
			throw new RuntimeException("" + detail.getDeviceNo() + "该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(monitor.getProjectId());
		if(projectInfo==null){
			throw new RuntimeException("设备"+detail.getDeviceNo()+"不在任何项目下");
		}
		detail.setMonitorId(monitor.getId());
		detail = MonitorConvert.convert(detail);
		CurrentMonitorData   currentMonitorData =CurrentMonitorData.factory(detail,monitor);

		this.push(redisUtil,monitor.getDeviceNo(),"monitor",projectInfo.getUuid(),currentMonitorData,""+monitor.getProjectId());

		detailMapper.createDetail(detail,BaseFactory.getTableName(ProjectEnvironmentMonitorDetail.class));

		// 比对 阈值
		List<ProjectEnvironmentMonitorAlarm> alarms = MonitorAlarmFactory.factory(monitor,detail);

		List<AlarmDTO>   alarmInfo   = new ArrayList<>();
		for(ProjectEnvironmentMonitorAlarm alarm:alarms){
			alarmMapper.createAlarm(alarm,BaseFactory.getTableName(ProjectEnvironmentMonitorAlarm.class));
			alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"扬尘设备"));
			pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"扬尘设备"),projectInfo.getUuid());


		}
		if(alarmInfo.size()>0){
			ResultDTO redisData =  ResultDTO.factory(alarmInfo,20);
			//放入缓存
			this.push(redisUtil,monitor.getDeviceNo(),"monitor_alarm",projectInfo.getUuid(),redisData,""+monitor.getProjectId());
		}

	}



}
