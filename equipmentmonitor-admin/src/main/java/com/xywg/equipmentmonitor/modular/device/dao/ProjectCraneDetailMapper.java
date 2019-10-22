package com.xywg.equipmentmonitor.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCraneDetail;
import com.xywg.equipmentmonitor.modular.station.dto.WeightAndMomentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-12-20
 */
@Mapper
public interface ProjectCraneDetailMapper extends BaseMapper<ProjectCraneDetail> {
	/**
	 * 切换到图表统计
	 * @param id
	 * @param columnName
	 * @param tableName
	 * @param tableName1
	 * @param step
	 * @param total
	 * @return
	 */
	 List<Map<String,Object>> changeToChart(@Param("id")Integer id,@Param("columnName") String columnName,@Param("tableName")String tableName,@Param("tableName1")String tableName1,@Param("step")Integer step,@Param("total")Integer total);
	 /**
	  * 按期间查询图表
	  * @param id
	  * @param columnName
	  * @param tableName
	  * @param tableName1
	  * @param beginDate
	  * @param endDate
	  * @param step
	  * @return
	  */
	 List<Map<String,Object>> changeToChartAuto(@Param("id")Integer id,@Param("columnName") String columnName,@Param("tableName")String tableName,@Param("tableName1")String tableName1,@Param("beginDate")String beginDate,@Param("endDate")String endDate,@Param("step")Integer step);

	/**
	 * 获取实力吊重和力矩数据
	 * @param param
	 * @return
	 */
	 List<WeightAndMomentVO> getWeightAndMomentVO(Map<String,Object> param);

}
