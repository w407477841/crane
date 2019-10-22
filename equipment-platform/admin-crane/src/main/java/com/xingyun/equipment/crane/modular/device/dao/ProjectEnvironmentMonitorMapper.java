package com.xingyun.equipment.crane.modular.device.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.crane.modular.device.vo.MonitorAlarmVO;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Mapper
public interface ProjectEnvironmentMonitorMapper extends BaseMapper<ProjectEnvironmentMonitor> {

	/**
	 * 分页查询
	 * @param rowBounds
	 * @param wrapper
	 * @return
	 */
	List<ProjectEnvironmentMonitor> selectPageList(RowBounds rowBounds,
                                                   @Param("ew") Wrapper<RequestDTO<ProjectEnvironmentMonitor>> wrapper);

	/**
	 * 不分页查询，地图用
	 * @param wrapper
	 * @return
	 */
	List<ProjectEnvironmentMonitor> selectListMap(@Param("ew") Wrapper<RequestDTO<ProjectEnvironmentMonitor>> wrapper);
	/**
	 * 判重
	 * @param deviceNo
	 * @param projectId
	 * @return
	 */
	Long countDevice(@Param("deviceNo") String deviceNo, @Param("projectId") Integer projectId);
	
	/**
	 * 智慧工地绿色施工拉取数据
	 * @param page
	 * @param map
	 * @return
	 */
	 List<MonitorAlarmVO> getEnvironmentInfo(Page<MonitorAlarmVO> page, Map<String, Object> map);
	 /**
	  * 拉取报警信息接口
	  * @param page
	  * @param map
	  * @return
	  */
	 List<MonitorAlarmVO> getAlarmInfo(Page<MonitorAlarmVO> page, Map<String, Object> map);
	 /**
	  * 拉取报警信息详情接口
	  * @param map
	  * @return
	  */
	 List<MonitorAlarmVO> getAlarmDetail(Page<MonitorAlarmVO> page, Map<String, Object> map);
	 /**
	  * 新增占用
	  * @param specification
	  * @param manufactor
	  * @return
	  */
	 int plusCallTimes(@Param("specification") String specification, @Param("manufactor") String manufactor);
	 /**
	  * 减少占用
	  * @param specification
	  * @param manufactor
	  * @return
	  */
	 int minusCallTimes(@Param("specification") String specification, @Param("manufactor") String manufactor);

	/**
	 * 获取部门下所有扬尘设备
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<ProjectEnvironmentMonitor> selectByOrgId(Map<String, Object> map) throws Exception;

	/**
	 * 根据设备号获取标准信息
	 * @param deviceNo
	 * @return
	 */
	@Select("select t.device_no as deviceNo,t.humidity_max as humidityMax,t.humidity_min as humidityMin,t.noise,t.pm10,t.pm25,t.temperature_max as temperatureMax,t.temperature_min as temperatureMin,t.wind_force as windForce,t.wind_speed as windSpeed from t_project_environment_monitor t where t.device_no = #{deviceNo}")
	ProjectEnvironmentMonitor getEnvByDeviceNo(@Param("deviceNo") String deviceNo);

	/**
	 * 根据设备编号判重
	 * @param id
	 * @param deviceNo
	 * @return
	 */
	List<ProjectEnvironmentMonitor> checkByDeviceNo(@Param("id") Integer id, @Param("deviceNo") String deviceNo);
}
