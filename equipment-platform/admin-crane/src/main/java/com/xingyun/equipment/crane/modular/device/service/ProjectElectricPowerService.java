package com.xingyun.equipment.crane.modular.device.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectElectricPower;
import com.xingyun.equipment.crane.modular.device.vo.ElectricAlarmVO;

/**
 * <p>
 *  电表设备管理
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
public interface ProjectElectricPowerService extends IService<ProjectElectricPower> {
	/**
	 * 分页查询
	 * @param res
	 * @return
	 */
	ResultDTO<DataDTO<List<ProjectElectricPower>>> selectPageList(
            RequestDTO<ProjectElectricPower> res);
	/**
	 * 电表地图用
	 * @param res
	 * @return
	 */
	ResultDTO<List<ProjectElectricPower>> selectListMap(
            RequestDTO<ProjectElectricPower> res);
	/**
	 * 电表设备合计
	 * @param deviceNo
	 * @param projectId
	 * @return
	 */
	ResultDTO<Object> countDevice(String deviceNo, Integer projectId);
	/**
	 * 单条
	 * @param res
	 * @return
	 */
	ResultDTO<ProjectElectricPower> selectInfo(
            RequestDTO<ProjectElectricPower> res);
	/**
	 * 新增
	 * @param res
	 * @return
	 * @throws Exception 
	 */
	ResultDTO<Object> insertInfo(RequestDTO<ProjectElectricPower> res) throws Exception;
	/**
	 * 启用
	 * @param res
	 * @return
	 * @throws Exception 
	 */
	ResultDTO<Object> updateStatus(RequestDTO<ProjectElectricPower> res) throws Exception;
	/**
	 * 编辑
	 * @param res
	 * @return
	 * @throws Exception 
	 */
	ResultDTO<Object> updateInfo(RequestDTO<ProjectElectricPower> res) throws Exception;
	/**
	 * 获取部门下所有电表
	 * @param orgId
	 * @return
	 */
	List<ProjectElectricPower> selectByOrgId(Integer orgId);
	/**
	 * 首页电表接口
	 * @param orgId
	 * @return
	 * @throws Exception 
	 */
	ResultDTO<Map<String, Object>> getElectricInfo(Integer orgId) throws Exception;

	/**
	 * 获取电表明细
	 * @param res
	 * @return
	 */
	public byte[] getElectricDetailInfo(RequestDTO<ProjectElectricPower> res) ;
	
	/**
	 * 报警信息拉取接口
	 * 
	 * @param res
	 * @return
	 */
	byte[] getAlarmInfo(RequestDTO<ElectricAlarmVO> res);
	/**
	 * 报警信息明细拉取接口
	 * 
	 * @param res
	 * @return
	 */
	byte[] getAlarmDetail(RequestDTO<ElectricAlarmVO> res);
}
