package com.xywg.equipment.monitor.iot.modular.watermeter.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeter;
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
public interface ProjectWaterMeterMapper extends BaseMapper<ProjectWaterMeter> {
    @Select("select t.dissipation,t.id,t.project_id as projectId,t.device_no as deviceNo from t_project_water_meter t where device_no = #{deviceNo}")
    ProjectWaterMeter getBaseInfo(@Param("deviceNo") String deviceNo);
}
