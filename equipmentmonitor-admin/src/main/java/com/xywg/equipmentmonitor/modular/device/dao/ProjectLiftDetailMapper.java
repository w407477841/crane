package com.xywg.equipmentmonitor.modular.device.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.vo.CurrentLiftDataVO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftDetail;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftHeartbeat;
import com.xywg.equipmentmonitor.modular.station.dto.LiftDetailWeightVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
public interface ProjectLiftDetailMapper extends BaseMapper<ProjectLiftDetail> {
	/**
	 * 查看运行数据
	 * @param map
	 * @return
	 */
	List<ProjectLiftDetail> selectOperationData(Page<ProjectLiftDetail> page,Map<String, Object>map);
	/**
	 * 查看监控状态
	 * @param map
	 * @return
	 */
	List<ProjectLiftHeartbeat> selectMonitorStatus(Page<ProjectLiftHeartbeat> page,Map<String, Object>map);

	/**
	 * 7小时 分表数据
	 * @param param
	 * @return
	 */
	CurrentLiftDataVO getMonitorData(Map<String,Object> param);



	/**
	 *  查询设备的在线状况
	 * @param deviceNo
	 * @param uuid
	 * @return
	 */
	@Select("select is_online from t_project_lift where device_no=#{deviceNo} and is_del =0 and project_id = (select id from t_project_info where  uuid = #{uuid} and is_del = 0 ) order by create_time desc limit 0,1 ")
	Integer getDeviceOnline(@Param("deviceNo") String deviceNo,@Param("uuid") String uuid);

	/**
	 * 切换到图表统计
	 * @param id
	 * @param columnName
	 * @param beginDate
	 * @param endDate
	 * @param month
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

	 List<LiftDetailWeightVO> getWeight(Map<String,Object> param);
}
