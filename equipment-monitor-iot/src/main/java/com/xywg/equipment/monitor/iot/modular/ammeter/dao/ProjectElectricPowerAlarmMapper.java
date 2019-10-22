package com.xywg.equipment.monitor.iot.modular.ammeter.dao;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
