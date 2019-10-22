package com.xywg.equipment.monitor.iot.modular.watermeter.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterAlarm;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface ProjectWaterMeterAlarmMapper extends BaseMapper<ProjectWaterMeterAlarm> {
    void create(@Param("t") ProjectWaterMeterAlarm alarm, @Param("tableName") String tableName);
}
