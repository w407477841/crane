package com.xywg.equipment.monitor.iot.modular.watermeter.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterHeartbeat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface ProjectWaterMeterHeartbeatMapper extends BaseMapper<ProjectWaterMeterHeartbeat> {
    @Select("select t.id,t.electric_id as electricId,t.create_time as createTime,t.end_time as endTime,t.is_del isDel,t.status from t_project_water_meter_heartbeat t where t.electric_id = #{eleId} order by t.create_time DESC limit 0,1")
    ProjectWaterMeterHeartbeat getLastInfo(@Param("eleId") int eleId);
}
