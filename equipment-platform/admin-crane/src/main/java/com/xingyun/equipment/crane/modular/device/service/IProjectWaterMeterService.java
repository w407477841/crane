package com.xingyun.equipment.crane.modular.device.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeter;
import com.xingyun.equipment.crane.modular.device.model.ProjectWaterMeterAlarm;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterAlarmVo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterDetailVo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterHeartbeatVo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectWaterMeterVo;
import com.xingyun.equipment.crane.modular.device.vo.WaterAlarmVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-27
 */
public interface IProjectWaterMeterService extends IService<ProjectWaterMeter> {
    /**
     * 获取实时监控水表信息
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterVo> selectWaterData(Page<ProjectWaterMeterVo> page, RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception;

    /**
     * 获取监控状态信息
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterHeartbeatVo> selectMonitorStatus(Page<ProjectWaterMeterHeartbeatVo> page, RequestDTO<ProjectWaterMeterHeartbeatVo> requestDTO) throws Exception;

    /**
     * 获取运行数据信息
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterDetailVo> selectRunData(Page<ProjectWaterMeterDetailVo> page, RequestDTO<ProjectWaterMeterDetailVo> requestDTO) throws Exception;

    /**
     * 获取报警条数
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterAlarmVo> selectWarningAlarm(Page<ProjectWaterMeterAlarmVo> page, RequestDTO<ProjectWaterMeterAlarmVo> requestDTO) throws Exception;

    /**
     * 获取报警信息
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterAlarm> selectAlarmData(Page<ProjectWaterMeterAlarm> page, RequestDTO<ProjectWaterMeterAlarmVo> requestDTO) throws Exception;

//    /**
//     * 首页水表接口
//     * @param request
//     * @return
//     */
//    ResultDTO<ProjectWaterMeterInfoVo> getWaterInfo(RequestDTO request) ;
    /**
	 * 首页水表接口
	 * @param orgId
	 * @return
	 * @throws Exception 
	 */
	ResultDTO<Map<String, Object>> getWaterInfo(Integer orgId) throws Exception;
	/**
	 * 首页水表接口1
	 * @param orgId
	 * @return
	 * @throws Exception 
	 */
//	ResultDTO<Map<String, Object>> getWaterInfo1(Integer orgId) throws Exception;

	
    /**
     * 获取列表
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterVo> selectWater(Page<ProjectWaterMeterVo> page, RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception;

    /**
     * 获取不分页列表
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterVo> selectWaterList(RequestDTO<ProjectWaterMeterVo> requestDTO) throws Exception;
    

	/**
	 * 获取水表明细
	 * @param res
	 * @return
	 */
	public byte[] getWaterDetailInfo(RequestDTO<ProjectWaterMeter> res) ;
	
	/**
	 * 报警信息拉取接口
	 * 
	 * @param res
	 * @return
	 */
	byte[] getAlarmInfo(RequestDTO<WaterAlarmVO> res);
	/**
	 * 报警信息明细拉取接口
	 * 
	 * @param res
	 * @return
	 */
	byte[] getAlarmDetail(RequestDTO<WaterAlarmVO> res);

	/**
	 * 启用
	 * @param requestDTO
	 * @return
	 * @throws Exception
	 */
	boolean setUse(RequestDTO<ProjectWaterMeter> requestDTO) throws Exception;

	/**
	 * 单条查询
	 * @param id
	 * @return
	 */
	ProjectWaterMeterVo selectWaterById(Serializable id);
}
