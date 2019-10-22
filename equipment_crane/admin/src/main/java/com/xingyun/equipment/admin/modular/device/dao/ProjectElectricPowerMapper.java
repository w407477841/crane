package com.xingyun.equipment.admin.modular.device.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPower;
import com.xingyun.equipment.admin.modular.device.vo.ElectricAlarmVO;
import com.xingyun.equipment.admin.modular.device.vo.ProjectElectricPowerVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
public interface ProjectElectricPowerMapper extends BaseMapper<ProjectElectricPower> {
	/**
	 * 分页查询
	 * @param rowBounds
	 * @param ew
	 * @return
	 */
	List<ProjectElectricPower> selectPageList(RowBounds rowBounds,
			@Param("ew")EntityWrapper<RequestDTO<ProjectElectricPower>> ew);
	/**
	 * 合计
	 * @param deviceNo
	 * @param projectId
	 * @return
	 */
	Long countDevice(@Param("deviceNo")String deviceNo, @Param("projectId")Integer projectId);
	/**
	 * 获取部门下所有设备
	 * @param map
	 * @return
	 */
	List<ProjectElectricPower> selectByOrgId(Map<String, Object> map);
	/**
	 * 地图接口
	 * @param ew
	 * @return
	 */
	List<ProjectElectricPower> selectListMap(
			@Param("ew")EntityWrapper<RequestDTO<ProjectElectricPower>> ew);
	/**
	 * 占用+1
	 * @param specification
	 * @param manufactor
	 */
	void plusCallTimes(@Param("specification")String specification, @Param("manufactor")String manufactor);
	/**
	 * 查询异常表数量
	 * @param month
	 * @param ids
	 * @return
	 */
	Integer selectAbnormal(@Param("month")String month,@Param("ids")List<Integer> ids);

	/**
	 * 占用-1
	 * @param specification
	 * @param manufactor
	 */
	void minusCallTimes(@Param("specification")String specification, @Param("manufactor")String manufactor);

	/**
	 * 根据月份算电量(new)
	 * @param statisticsDate
	 * @param ids
	 * @return
	 */
	BigDecimal getAmountByMonth(@Param("statisticsDate")String statisticsDate, @Param("ids")List<Integer> ids);
	/**
	 * 根据日期算电量(new)
	 * @param beginDay
	 * @param endDay
	 * @param ids 
	 * @return
	 */
	BigDecimal getAmountByDays(@Param("beginDay")String beginDay, @Param("endDay")String endDay, @Param("ids")List<Integer> ids);
	/**
	 * 根据设备算电量
	 * @param beginDay
	 * @param endDay
	 * @param id
	 * @return
	 */
	BigDecimal getAmountByDevice(@Param("beginDay")String beginDay, @Param("endDay")String endDay, @Param("id")Integer id);



	/**
	 * 智慧工地绿色施工拉取数据
	 * @param page
	 * @param map
	 * @return
	 */
	 List<ProjectElectricPowerVO> getElecDetailInfo(Page<ProjectElectricPower> page,Map<String,Object> map);
	 
	 
	 /**
	  * 拉取报警信息接口
	  * @param page
	  * @param map
	  * @return
	  */
	 List<ElectricAlarmVO> getAlarmInfo(Page<ElectricAlarmVO> page,Map<String,Object> map);
	 /**
	  * 拉取报警信息详情接口
	  * @param map
	  * @return
	  */
	 List<ElectricAlarmVO> getAlarmDetail(Page<ElectricAlarmVO> page,Map<String,Object> map);

	/**
	 * 判重
	 * @param deviceNo
	 * * @param id
	 * @return
	 */
	List<ProjectElectricPower> checkByDeviceNo(@Param("deviceNo")String deviceNo,@Param("id")Integer id);
}
