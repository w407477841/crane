package com.xywg.equipment.monitor.modular.whf.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorAlarm;
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
void createAlarm(@Param("t") ProjectEnvironmentMonitorAlarm alarm, @Param("tableName") String tableName);
}
