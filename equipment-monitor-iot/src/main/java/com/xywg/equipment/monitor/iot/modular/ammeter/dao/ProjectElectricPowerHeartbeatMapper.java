package com.xywg.equipment.monitor.iot.modular.ammeter.dao;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerDetail;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerHeartbeat;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface ProjectElectricPowerHeartbeatMapper extends BaseMapper<ProjectElectricPowerHeartbeat> {
    @Select("select t.id,t.electric_id as electricId,t.create_time as createTime,t.end_time as endTime,t.is_del isDel,t.status from t_project_electric_power_heartbeat t where t.electric_id = #{eleId} order by t.create_time DESC limit 0,1")
    ProjectElectricPowerHeartbeat getLastInfo(@Param("eleId") int eleId);
}
