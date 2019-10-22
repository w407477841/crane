package com.xingyun.equipment.crane.modular.device.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.crane.modular.device.model.ElectricityTrend;
import com.xingyun.equipment.crane.modular.device.model.ElectricityType;
import com.xingyun.equipment.crane.modular.device.model.ProjectElectricPowerDetail;
import com.xingyun.equipment.crane.modular.device.model.WaterTrend;
import com.xingyun.equipment.crane.modular.device.model.WaterType;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
public interface ProjectElectricPowerDetailMapper extends BaseMapper<ProjectElectricPowerDetail> {

	List<ProjectElectricPowerDetail> selectPageList(
            RowBounds rowBounds, @Param("map") Map<String, Object> map);


	public Double getOneDaydegree(Map<String, Object> param);


	@Select("\tselect sum(amount_used) used from t_project_ammeter_daily where device_id in (\n" +
			"\tselect id from t_project_electric_power where project_id in(\n" +
			"\t\tselect id from t_project_info where uuid = #{uuid}\n" +
			"\t\t) and type = #{type}\n" +
			"\t\t) and statistics_date >='${date}' ")
	Double sum(@Param("uuid") String uuid, @Param("date") String date, @Param("type") int type);

	List<Map<String,Object>> eveSum(@Param("uuid") String uuid, @Param("date") String date, @Param("type") Integer type);

	
	/**
	 * 查询用电分类信息
	 * 
	 * @param map
	 * @return
	 */
	List<ElectricityType> selectElectricityType(Map<String, Object> map);
	
	/**
	 * 按月查询用电量
	 * 
	 * @param map
	 * @return
	 */
	ElectricityType selectElectricityByMonth(Map<String, Object> map);
	
	/**
	 * 查询用电趋势
	 * 
	 * @param map
	 * @return
	 */
	List<ElectricityTrend> selectElectricityTrend(Map<String, Object> map);
	
	/**
	 * 查询用电趋势
	 * 
	 * @param map
	 * @return
	 */
	List<ElectricityTrend> selectLastElectricityTrend(Map<String, Object> map);

	
	/**
	 * 查询用水分类信息
	 * 
	 * @param map
	 * @return
	 */
	List<WaterType> selectWaterType(Map<String, Object> map);
	
	/**
	 * 按月查询用水量
	 * 
	 * @param map
	 * @return
	 */
	WaterType selectWaterByMonth(Map<String, Object> map);
	
	/**
	 * 查询用水趋势
	 * 
	 * @param map
	 * @return
	 */
	List<WaterTrend> selectWaterTrend(Map<String, Object> map);
	
	/**
	 * 查询用水趋势
	 * 
	 * @param map
	 * @return
	 */
	List<WaterTrend> selectLastWaterTrend(Map<String, Object> map);
	
	
	
	/**
	 * 查询用电分类信息
	 * 
	 * @param map
	 * @return
	 */
	List<ElectricityType> selectElectrType(@Param("list") List<String> list);
	
	/**
	 * 按月查询用电量
	 * 
	 * @param map
	 * @return
	 */
	ElectricityType selectElectrByMonth(@Param("date") String date);
	
	/**
	 * 查询用电趋势
	 * 
	 * @param map
	 * @return
	 */
	List<ElectricityTrend> selectElectrTrend(@Param("list") List<String> list, @Param("type") Integer type);
	

	/**
	 * 查询用电分类信息
	 * 
	 * @param map
	 * @return
	 */
	List<WaterType> selectWatType(@Param("list") List<String> list);
	
	/**
	 * 按月查询用电量
	 * 
	 * @param map
	 * @return
	 */
	WaterType selectWatByMonth(@Param("date") String date);
	
	/**
	 * 查询用电趋势
	 * 
	 * @param map
	 * @return
	 */
	List<WaterTrend> selectWatTrend(@Param("list") List<String> list, @Param("type") Integer type);
}
