package com.xywg.equipmentmonitor.modular.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.dto.CountAlarmByDeviceNo;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPowerAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
public interface ProjectElectricPowerAlarmMapper extends BaseMapper<ProjectElectricPowerAlarm> {

	List<ProjectElectricPowerAlarm> selectPageList(
			RowBounds rowBounds,
			@Param("ew")EntityWrapper<RequestDTO<ProjectElectricPowerAlarm>> ew);

	List<ProjectElectricPowerAlarm> getAlarmByDateTime(
			@Param("alarm")CountAlarmByDeviceNo countAlarmByDeviceNo);

	List<ProjectElectricPowerAlarm> getPageAlarmByDateTime(
			Page<ProjectElectricPowerAlarm> page,
			@Param("alarm")CountAlarmByDeviceNo countAlarmByDeviceNo);

/**
* Description: 获取当月项目下 异常设备数
 * @author: wangyifei
*
 * @param uuid  uuid
 * @param month  month
 * @return 数量
* Date: 14:45 2018/10/22
*/
	@Select("SELECT\n" +
			"\tcount(1) count\n" +
			"FROM\n" +
			"\t(\n" +
			"\t\tSELECT\n" +
			"\t\t\t1\n" +
			"\t\tFROM\n" +
			"\t\t\tt_project_electric_power_alarm_${month}\n" +
			"\t\tWHERE\n" +
			"\t\t\telectric_id IN (\n" +
			"\t\t\t\tSELECT\n" +
			"\t\t\t\t\tid\n" +
			"\t\t\t\tFROM\n" +
			"\t\t\t\t\tt_project_electric_power\n" +
			"\t\t\t\tWHERE\n" +
			"\t\t\t\t\tproject_id = (\n" +
			"\t\t\t\t\t\tSELECT\n" +
			"\t\t\t\t\t\t\tid\n" +
			"\t\t\t\t\t\tFROM\n" +
			"\t\t\t\t\t\t\tt_project_info\n" +
			"\t\t\t\t\t\tWHERE\n" +
			"\t\t\t\t\t\t\tuuid = #{uuid}\n" +
			"\t\t\t\t\t)\n" +
			"\t\t\t)\n" +
			"\t\tGROUP BY\n" +
			"\t\t\telectric_id\n" +
			"\t) m")
	int shuiDianExs(@Param("month") String month,@Param("uuid") String uuid);



}
