package com.xywg.equipmentmonitor.modular.lift.service;

import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftDetail;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftVO;
import org.springframework.stereotype.Service;

import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.lift.model.ProjectLiftAlarmInfo;
import com.xywg.equipmentmonitor.modular.lift.vo.ProjectLiftAlarmCountVO;
import com.xywg.equipmentmonitor.modular.lift.vo.ProjectLiftListVO;
import com.xywg.equipmentmonitor.modular.lift.vo.ProjectLiftOrgVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
@Service
public interface ProjectLiftInfoService  {
	  /**
	   * 查询升降机
	     * @param request
	     * @return
	     */
	
	byte[] getLiftInfo(RequestDTO request);
	  /**
	   * 查询升降机
	     * @param request
	     * @return
	     */
	
	byte[] getAlarmInfo(RequestDTO request);
	
    /**
     * 查询集团下所有升降机(接口)
     * @param request
     * @return
     */
    ResultDTO<ProjectLiftOrgVO> getLiftList(RequestDTO request);
    /**
     * 查询塔吊最近一条数据(接口)
     * @param request
     * @return
     */
    ResultDTO<ProjectLiftListVO> getLiftDetail(RequestDTO request);
    /**
     *查询升降机当月重量报警/预警 数量(接口
     * @param request
     * @return
     */
    ResultDTO<ProjectLiftAlarmCountVO> getLiftAlarmCount(RequestDTO request);

	/**
	 * 返回 固定100条数据
	 * @param uuid
	 * @param deviceNo
	 * @return
	 */
	ResultDTO<ProjectLiftDetail>    getTop100LiftDetail(String uuid,String deviceNo );
	
	/**
	 * 拉取报警信息明细
	 * @param res
	 * @return
	 */
	byte[] getAlarmDetail(RequestDTO<ProjectLiftAlarmInfo> res);
	
}
