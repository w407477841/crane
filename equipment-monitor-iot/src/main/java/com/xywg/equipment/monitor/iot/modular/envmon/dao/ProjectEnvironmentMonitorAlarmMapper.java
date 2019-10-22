package com.xywg.equipment.monitor.iot.modular.envmon.dao;

import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitorAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
public interface ProjectEnvironmentMonitorAlarmMapper extends BaseMapper<ProjectEnvironmentMonitorAlarm> {
void createAlarm(@Param("t") ProjectEnvironmentMonitorAlarm alarm,@Param("tableName") String tableName);
}
