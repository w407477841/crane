package com.xywg.equipment.monitor.modular.sb.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeterAlarm;
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
