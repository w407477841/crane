package com.xywg.equipment.monitor.iot.modular.ammeter.dao;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPower;
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
public interface ProjectElectricPowerMapper extends BaseMapper<ProjectElectricPower> {
    @Select("select t.dissipation,t.id,t.project_id as projectId,t.device_no as deviceNo from t_project_electric_power t where device_no = #{deviceNo}")
    ProjectElectricPower getBaseInfo(@Param("deviceNo") String deviceNo);
}
