package com.xingyun.equipment.admin.modular.device.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftAlarm;
import com.xingyun.equipment.admin.modular.device.vo.ProjectLiftAlarmVO;
import com.xingyun.equipment.admin.modular.device.vo.RealTimeMonitoringTowerVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
@Service
public interface ProjectLiftAlarmService extends IService<ProjectLiftAlarm> {
	/**
	 * 查询列表
	 * @param request
	 * @return
	 */
    ResultDTO<DataDTO<List<ProjectLiftAlarm>>> getPageList(RequestDTO<ProjectLiftAlarm> request);
    /**
     * 查询预警个数
     * @param c
     * @return
     */
	ResultDTO<ProjectLiftAlarmVO> countEarlyWarning(ProjectLiftAlarmVO c);
	  /**
	   * 查询预警个数
	     * @param id
	     * @return
	     */
	
	ResultDTO<ProjectLiftAlarmVO> countCallWarning(ProjectLiftAlarmVO c);
	 /**
	   * 查询违章个数
	     * @param id
	     * @return
	     */
	ResultDTO<ProjectLiftAlarmVO> countViolation(ProjectLiftAlarmVO c);
	/**
	 * 判断时间
	 * @param c
	 * @return
	 */
	
	ProjectLiftAlarmVO judgeTime(ProjectLiftAlarmVO c);
	/**
	 *查询警告详情 
	 * @param c
	 * @return
	 */
	ResultDTO<DataDTO<List<ProjectLiftAlarmVO>>> earlyWarningDetail(RequestDTO<ProjectLiftAlarmVO> c);
	
	/**
	 * 查询列表
	 * @param request
	 * @return
	 */
    ResultDTO<DataDTO<List<ProjectLift>>> realTimeMonitoring(RequestDTO<ProjectLift> request);
    /**
     * 发送短信
     * @param realTimeMonitoringTowerVo
     * @return
     * @throws Exception
     */
    boolean insertMessage(RealTimeMonitoringTowerVo realTimeMonitoringTowerVo) throws Exception;

	
	
}
