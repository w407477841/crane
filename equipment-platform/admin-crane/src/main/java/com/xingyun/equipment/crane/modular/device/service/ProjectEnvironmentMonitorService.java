package com.xingyun.equipment.crane.modular.device.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dto.ProjectEnvironmentMonitorDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.crane.modular.device.vo.MonitorAlarmVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public interface ProjectEnvironmentMonitorService extends IService<ProjectEnvironmentMonitor> {

	/**
	 * 一览
	 * 
	 * @param res
	 * @return
	 */
	ResultDTO<DataDTO<List<ProjectEnvironmentMonitor>>> selectPageList(RequestDTO<ProjectEnvironmentMonitor> res);

	/**
	 * 地图用
	 * @param res
	 * @return
	 */
	ResultDTO<List<ProjectEnvironmentMonitor>> selectListMap(RequestDTO<ProjectEnvironmentMonitor> res);
	
	/**
	 * 单条
	 * 
	 * @param res
	 * @return
	 */
	ResultDTO<ProjectEnvironmentMonitorDTO> selectInfo(RequestDTO<ProjectEnvironmentMonitor> res);

	/**
	 * 判重
	 * 
	 * @param deviceNo
	 * @param projectId
	 * @return
	 */
	ResultDTO<Object> countDevice(String deviceNo, Integer projectId);

	/**
	 * 新增
	 * 
	 * @param res
	 * @return
	 */
	ResultDTO<Object> insertInfo(RequestDTO<ProjectEnvironmentMonitorDTO> res);

	/**
	 * 编辑
	 * 
	 * @param res
	 * @return
	 */
	ResultDTO<Object> updateInfo(RequestDTO<ProjectEnvironmentMonitorDTO> res);

	/**
	 * 启用
	 * 
	 * @param res
	 * @return
	 */
	ResultDTO<Object> updateStatus(RequestDTO<ProjectEnvironmentMonitor> res);

	/**
	 * 设备平台给智慧工地用
	 * 
	 * @param res
	 * @return
	 */
	byte[] getEnvironmentInfo(RequestDTO<MonitorAlarmVO> res);

	/**
	 * 报警信息拉取接口
	 * 
	 * @param res
	 * @return
	 */
	byte[] getAlarmInfo(RequestDTO<MonitorAlarmVO> res);

	/**
	 * 获取部门下所有扬尘设备
	 * @param orgIds
	 * @return
	 * @throws Exception
	 */
	List<ProjectEnvironmentMonitor> selectByOrgId(List<Integer> orgIds) throws Exception;


	/**
	 * 给大屏展示用
	 * @param deviceNo
	 * @return
	 */
	Map<String,Object> getEnvironmentInfoForScreen(String deviceNo);
	/**
	 * 设备平台给智慧工地用
	 * 
	 * @param res
	 * @return
	 */
	byte[] getAlarmDetail(RequestDTO<MonitorAlarmVO> res);


	/**
	 *
	 * @param uuid
	 * @return
	 */
	List<Map<String,Object>> getDeviceOnlineStatus(String uuid, int type);

}
