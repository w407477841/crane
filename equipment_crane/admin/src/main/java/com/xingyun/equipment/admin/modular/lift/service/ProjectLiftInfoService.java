package com.xingyun.equipment.admin.modular.lift.service;

import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftDetail;
import com.xingyun.equipment.admin.modular.device.vo.ProjectLiftVO;
import org.springframework.stereotype.Service;

import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.lift.model.ProjectLiftAlarmInfo;
import com.xingyun.equipment.admin.modular.lift.vo.ProjectLiftAlarmCountVO;
import com.xingyun.equipment.admin.modular.lift.vo.ProjectLiftListVO;
import com.xingyun.equipment.admin.modular.lift.vo.ProjectLiftOrgVO;

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
