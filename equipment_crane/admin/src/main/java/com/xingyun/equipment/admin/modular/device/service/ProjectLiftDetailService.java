package com.xingyun.equipment.admin.modular.device.service;

import java.util.List;
import java.util.Map;

import com.xingyun.equipment.admin.core.vo.CurrentLiftDataVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftDetail;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftHeartbeat;
import com.xingyun.equipment.admin.modular.device.vo.ProjectLiftAlarmVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
@Service
public interface ProjectLiftDetailService extends IService<ProjectLiftDetail> {
	
	/**
	 * 查询运行数据
	 * @param request
	 * @return
	 */
    ResultDTO<DataDTO<List<ProjectLiftDetail>>> selectOperationData(RequestDTO<ProjectLiftAlarmVO> request);
    /**
	 * 查询监控状态
	 * @param request
	 * @return
	 */
    ResultDTO<DataDTO<List<ProjectLiftHeartbeat>>> selectMonitorStatus(RequestDTO<ProjectLiftHeartbeat> request);
    /**
	 * 查询运行时间
	 * @param request
	 * @return
	 */
    ResultDTO<DataDTO<List<ProjectLiftHeartbeat>>> selectRunningTime(RequestDTO<ProjectLiftHeartbeat> request);
	/**
	 * 判断时间
	 * @param c
	 * @return
	 */
    ProjectLiftHeartbeat judgeTime(ProjectLiftHeartbeat c);


	/**
	 * 近期数据
	 * @param deviceNo
	 * @param uuid
	 * @return
	 */
	CurrentLiftDataVO getMonitorData(String deviceNo, String uuid);

	ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate);
}
