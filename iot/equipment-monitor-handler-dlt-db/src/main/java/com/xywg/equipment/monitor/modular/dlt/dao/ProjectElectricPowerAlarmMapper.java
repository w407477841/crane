package com.xywg.equipment.monitor.modular.dlt.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPowerAlarm;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface ProjectElectricPowerAlarmMapper extends BaseMapper<ProjectElectricPowerAlarm> {

    void create(@Param("t") ProjectElectricPowerAlarm alarm, @Param("tableName") String tableName);
}
