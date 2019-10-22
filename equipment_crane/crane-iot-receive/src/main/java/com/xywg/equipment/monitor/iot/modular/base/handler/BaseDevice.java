package com.xywg.equipment.monitor.iot.modular.base.handler;

import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xingyun.crane.cache.RedisUtil;;
import com.xywg.equipment.monitor.iot.modular.alipush.service.PushService;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
* @author: wangyifei
* Description:
* Date: 14:55 2018/9/3
*/

public abstract class BaseDevice {
	@Autowired
	protected  XywgProerties xywgProerties;
	@Autowired
	protected SimpMessageSendingOperations simpMessageSendingOperations;
	@Autowired
	protected PushService  pushService;


	protected String replaceKey(String data, String oldkey,String newKey){
		return data.replace(","+oldkey+":",","+ newKey+":");
	}  
	protected String replaceHead(String data, String oldkey,String newKey){
		return data.replace(oldkey+":", newKey+":");
	}


	protected String removeBlank(String data){

		data  = data.replace(":,",":0,");
		if(data.endsWith(":")){
			data += "0";
		}
		return data;
		}



    public  void handler(String data,  MasterProtocolConfigDTO config){

		if(!isHeartbeat(data)){




		try{
			doBusiness(data,config);
		}catch (Exception e){
e.printStackTrace();
		}
	}
		try{
			insertOrigin(data);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 *  是否是心跳数据
	 * @param data
	 * @return
	 */
	public abstract boolean isHeartbeat(String data);

	/**
	 *  插入原始数据
	 * @param data
	 */
	public abstract void insertOrigin(String data);


	public abstract  void offline(String data);

	/**
	 *
	 * @param data  原始数据
	 * @param config  协议配置
	 */
	@Transactional(rollbackFor = {Exception.class})
	public abstract void doBusiness(String data,  MasterProtocolConfigDTO config);

	/**
	 * 实时数据 推入redis
	 * @author:  wangyifei
	 * @param redisUtil 工具
	 * @param deviceNo  设备编号
	 * @param deviceType  设备类型
	 * @param uuid   项目编号
	 * @param data   数据
	 */
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
				simpMessageSendingOperations.convertAndSend(tempTopic,data);
			}
		}

		System.out.println("订阅路径："+topic);
		// 推送至 websocket
		simpMessageSendingOperations.convertAndSend(topic,data);

	}
}
