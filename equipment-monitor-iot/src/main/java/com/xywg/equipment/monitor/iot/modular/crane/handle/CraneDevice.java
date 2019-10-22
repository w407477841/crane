package com.xywg.equipment.monitor.iot.modular.crane.handle;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.base.dto.AlarmDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import com.xywg.equipment.monitor.iot.modular.base.dto.ResultDTO;
import com.xywg.equipment.monitor.iot.modular.base.factory.AlarmDTOFactory;
import com.xywg.equipment.monitor.iot.modular.base.factory.BaseFactory;
import com.xywg.equipment.monitor.iot.modular.base.handler.BaseDevice;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneAlarmMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneDetailMapper;
import com.xywg.equipment.monitor.iot.modular.crane.dto.CurrentCraneData;
import com.xywg.equipment.monitor.iot.modular.crane.factory.CraneAlarmFactory;
import com.xywg.equipment.monitor.iot.modular.crane.model.*;


import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneHeartbeatService;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneOriginalDataService;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import com.xywg.equipment.monitor.iot.modular.crane.service.impl.ProjectCraneServiceImpl;
import com.xywg.equipment.monitor.iot.netty.aop.ZbusProducerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.xywg.equipment.monitor.iot.core.util.OriDataUtil.TD_LOGGER;

/**
* @author: wangyifei
* Description:  塔吊数据处理类
* Date: 10:45 2018/8/27
*/
@Component
public class CraneDevice extends BaseDevice {

	@Autowired
	private ProjectCraneDetailMapper projectCraneDetailMapper;
	@Autowired
	private ProjectCraneAlarmMapper projectCraneAlarmMapper;

	@Autowired
	private IProjectCraneService   projectCraneService;
	@Autowired
	private IProjectCraneOriginalDataService projectLiftOriginalDataService;
	@Autowired
	private IProjectCraneHeartbeatService    projectCraneHeartbeatService;

	@Autowired
	private 	RedisUtil   redisUtil;
	@Autowired
	private IProjectInfoService  projectInfoService;
		@Override
	public boolean isHeartbeat(String data) {
			if(data.startsWith("qzjht:")){
				ProjectCraneHeartbeat  heartbeat  = new ProjectCraneHeartbeat();
								// 查询设备阈值
				heartbeat.setDeviceNo(data.split(":")[1]);
				ProjectCrane crane =  projectCraneService.selectOne(data.split(":")[1]);
				if(crane == null){
					// 压根没有这个设备
					throw new RuntimeException("数据库中没有"+data.split(":")[1]+"该设备");
				}
				if(0==crane.getStatus()){
					throw new RuntimeException(""+crane.getDeviceNo()+"该设备未启用");
				}
				heartbeat.setCraneId(crane.getId());
				heartbeat.setCreateTime(new Date());
				heartbeat.setDeviceNo(crane.getDeviceNo());
				if(!redisUtil.exists(this.xywgProerties.getRedisHead()+":"+"head:"+data)){
					// 不存在 说明是开机
					// 上一条是关机

					projectCraneHeartbeatService.doOpenBusiness(heartbeat);
					//1.修改数据库 online 字段
					ProjectCrane updCrane = new ProjectCrane();
					updCrane.setId(crane.getId());
					updCrane.setIsOnline(1);
					projectCraneService.updateById(updCrane);
					//删除 设备信息缓存
					String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+heartbeat.getDeviceNo();
					redisUtil.remove(deviceKey);
					}else{
					projectCraneHeartbeatService.updateEndTime(heartbeat);

				}

				redisUtil.setSec(this.xywgProerties.getRedisHead()+":"+"head:"+data,1,46L);


				return true;
			}

		return false;
	}

	@Override
	public void insertOrigin(String data) {
		TD_LOGGER.info(data);
		/*
		ProjectCraneOriginalData projectLiftOriginalData = new ProjectCraneOriginalData();
		projectLiftOriginalData.setDeviceTime(new Date());
		projectLiftOriginalData.setOriginalData(data);
		projectLiftOriginalDataService.insert(projectLiftOriginalData);
		*/
	}

	@Override
	public void offline(String data) {
		String deviceNo =  data.split(":")[1];

		ProjectCrane crane =  projectCraneService.selectOne(deviceNo);
		if(crane == null){
			throw new RuntimeException("数据库中没有"+deviceNo+"该设备");
		}
		if(0==crane.getStatus()){
			throw new RuntimeException("" + deviceNo + "该设备未启用");
		}

		//1.修改数据库 online 字段
		ProjectCrane updCrane = new ProjectCrane();
		updCrane.setId(crane.getId());
		updCrane.setIsOnline(0);
		projectCraneService.updateById(updCrane);
		//删除 设备信息缓存
		String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
		redisUtil.remove(deviceKey);

	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void doBusiness(String data, ZbusProducerHolder producerHolder, MasterProtocolConfigDTO config) {
		//TODO 先模拟
		ProjectCraneDataModel dataModel  =new ProjectCraneDataModel();
		dataModel.setDeviceNo("dltytj");
		dataModel.setWeight("a");
		dataModel.setRang("b");
		dataModel.setHeight("c");
		dataModel.setRotaryAngle("d");
		dataModel.setMoment("e");
		dataModel.setMomentPercentage("f");
		dataModel.setWindSpeed("g");
		dataModel.setTiltAngle("h");

		data = this.removeBlank(data);

		//以上为 模拟 ，后期从配置表中取

		data =  replaceHead(data,  dataModel.getDeviceNo(),"deviceNo");
		data =  replaceKey(data, dataModel.getWeight(), "weight");
		data =  replaceKey(data, dataModel.getRang(), "range");
		data =  replaceKey(data, dataModel.getHeight(), "height");
		data =  replaceKey(data, dataModel.getRotaryAngle(), "rotaryAngle");
		data =  replaceKey(data, dataModel.getMoment(), "moment");
		data =  replaceKey(data, dataModel.getMomentPercentage(), "momentPercentage");
		data =  replaceKey(data, dataModel.getWindSpeed(), "windSpeed");
		data =  replaceKey(data, dataModel.getTiltAngle(), "tiltAngle");
		// 报警字段
		data =  replaceKey(data, "i","status");

		String jsonStr =  "{"+data+"}";
		System.out.println("塔吊"+jsonStr);
		if(!JSONUtil.isJson(jsonStr)){
			// TODO 存入 错误数据
			throw new RuntimeException("数据格式异常");
		}
		ProjectCraneDetail detail  = JSONUtil.toBean(jsonStr,ProjectCraneDetail.class );

		// 查询设备阈值
		ProjectCrane crane =  projectCraneService.selectOne(detail.getDeviceNo());

		if(crane == null){
			// 压根没有这个设备
			throw new RuntimeException("数据库中没有"+detail.getDeviceNo()+"该设备");
		}
		if(0==crane.getStatus()){
			throw new RuntimeException(""+detail.getDeviceNo()+"该设备未启用");
		}
		ProjectInfo projectInfo =  projectInfoService.selectById(crane.getProjectId());
		if(projectInfo==null){
            throw new RuntimeException("设备"+detail.getDeviceNo()+"不在任何项目下");
        }
		detail.setCraneId(crane.getId());
		// 查询实时数据
		projectCraneDetailMapper.createDetail(detail, BaseFactory.getTableName(ProjectCraneDetail.class));

		CurrentCraneData  currentCraneData = CurrentCraneData.factory(
				detail,
				crane
		);


		this.push(redisUtil,detail.getDeviceNo(),"crane",projectInfo.getUuid(),currentCraneData,""+crane.getProjectId());

		//  比对 各项 阈值
		List<ProjectCraneAlarm> alarms  = CraneAlarmFactory.factory(
				crane,
				detail
		);


		List<AlarmDTO>   alarmInfo   = new ArrayList<>();
		//插入报警
		for(ProjectCraneAlarm alarm:alarms){
			projectCraneAlarmMapper.createAlarm(alarm,BaseFactory.getTableName(ProjectCraneAlarm.class));
			alarmInfo.add(AlarmDTOFactory.factory(alarm,projectInfo,"塔吊设备"));
			pushService.pushMob("注意",AlarmDTOFactory.factoryMessage(alarm,projectInfo,"塔吊设备"),projectInfo.getUuid());
		}
		if(alarmInfo.size()>0) {
			ResultDTO redisData = ResultDTO.factory(alarmInfo, 21);
			//放入缓存
			this.push(redisUtil, crane.getDeviceNo(), "crane_alarm", projectInfo.getUuid(), redisData,""+crane.getProjectId());
		}

	}



}
